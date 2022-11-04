package net.howtobedwars.varo.listener;

import net.howtobedwars.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private final Varo varo;

    public EntityDamageListener(Varo varo) {
        this.varo = varo;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(varo.getVaroGame().isStarting() || varo.getVaroGame().isProtectionTime() || !varo.getVaroGame().isDamage()) {
                event.setCancelled(true);
            }
        }
    }
}
