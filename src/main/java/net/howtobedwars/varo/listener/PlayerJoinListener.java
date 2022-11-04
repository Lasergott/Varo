package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.UsersConfig;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.team.VaroTeam;
import net.howtobedwars.varo.user.ConfigUser;
import net.howtobedwars.varo.user.User;
import net.howtobedwars.varo.util.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        UsersConfig usersConfig = varo.getVaroFiles().getUsersConfig();
        Optional<ConfigUser> optionalConfigUser = usersConfig.get(uuid);
        ConfigUser configUser;
        if (!optionalConfigUser.isPresent()) {
            final int SPAWN_POINT = usersConfig.getUsers().size();
            String serializedLocation = varo.getVaroFiles().getSpawnsConfig().getSpawns().get(SPAWN_POINT);
            Location location = LocationSerializer.deserialize(serializedLocation);
            player.teleport(location);
            player.setGameMode(GameMode.ADVENTURE);
            configUser = ConfigUser.create(uuid, player.getName());
        } else {
            configUser = optionalConfigUser.get();
        }
        configUser.setLastJoin(System.currentTimeMillis());
        usersConfig.update(configUser);
        varo.getVaroFiles().saveConfig(usersConfig);

        User user = varo.getVaroGame().getUserRegistry().get(uuid);
        if (user == null) {
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
        user.setConfigUser(configUser);
        varo.getVaroGame().updateTablist();
    }
}
