package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.cps.ClickType;
import net.howtobedwars.varo.team.VaroTeam;
import net.howtobedwars.varo.user.User;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

@AllArgsConstructor
public class PlayerInteractListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = varo.getVaroGame().getUserRegistry().get(player.getUniqueId());
        if (user != null) {
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                user.getCpsCheck().click(ClickType.LEFT);
            }
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                user.getCpsCheck().click(ClickType.RIGHT);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                if (block == null) {
                    return;
                }
                if (block.getType() == Material.CHEST) {
                    if (varo.getVaroGame().isStarting()) {
                        event.setCancelled(true);
                        return;
                    }

                    VaroTeam team = user.getTeam();
                    Optional<VaroTeam> optionalTeam = varo.getVaroFiles().getTeamsConfig().getTeams()
                            .stream()
                            .filter(team1 -> team1.getTeamChest() != null)
                            .filter(team1 -> team1.getTeamChest().equals(block.getLocation()))
                            .findFirst();

                    if (optionalTeam.isPresent()) {
                        if (!optionalTeam.get().equals(team)) {
                            player.sendMessage("??cDu kannst die Teamchest anderer Teams nicht ??ffnen");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
