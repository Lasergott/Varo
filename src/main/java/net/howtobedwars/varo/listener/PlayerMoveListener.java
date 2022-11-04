package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@AllArgsConstructor
public class PlayerMoveListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(varo.getVaroGame().isStarting()) {
            event.setCancelled(true);
        }
    }
}
