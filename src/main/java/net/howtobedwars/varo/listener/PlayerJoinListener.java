package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.team.VaroTeam;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        User user = varo.getVaroGame().getUserRegistry().get(uuid);
        if(user == null) {
            Optional<VaroTeam> optionalTeam = varo.getVaroFiles().getTeamsConfig().getTeams()
                    .stream()
                    .filter(team -> team.contains(player.getName()))
                    .findFirst();

            if (!optionalTeam.isPresent()) {
                Bukkit.broadcastMessage("§cFehler beim erstellen des Users " + player.getName() + ": der User wurde keinem Team zugewiesen");
                return;
            }
            VaroTeam team = optionalTeam.get();
            user = User.create(uuid, team, player);
            varo.getVaroGame().getUserRegistry().put(uuid, user);
            varo.getVaroGame().setTablist(user);
        }
        user.setCpsCheck(CPSCheck.create(varo, player));
        user.setPlayer(player);
        user.checkForTimeOver(varo);
        varo.getVaroGame().updateTablist();
    }
}
