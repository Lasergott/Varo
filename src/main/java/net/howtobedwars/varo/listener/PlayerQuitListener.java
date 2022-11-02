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
        if (user == null) {
            return;
        }
        if (varo.getVaroGame().getCombatLogCache().asMap().containsKey(player.getUniqueId())) {
            player.setHealth(0);
        }
        CPSCheck cpsCheck = user.getCpsCheck();
        cpsCheck.uncheck();
        user.setCpsCheck(null);
        player.removeMetadata("cps-check", varo);
    }
}
