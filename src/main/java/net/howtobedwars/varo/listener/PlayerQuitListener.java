package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = varo.getVaroGame().getUserRegistry().get(player.getUniqueId());
        if(player.hasMetadata("cps-check")) {
            CPSCheck cpsCheck = (CPSCheck) player.getMetadata("cps-check").get(0).value();
            cpsCheck.uncheck();
            player.removeMetadata("cps-check", varo);
        }
        if (user != null) {
            if (varo.getVaroGame().getCombatLogCache().asMap().containsKey(player.getUniqueId())) {
                player.setHealth(0);
            }
        }
    }
}
