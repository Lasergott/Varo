package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.user.User;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@AllArgsConstructor
public class EntityDamageByEntityListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            User victimUser = varo.getVaroGame().getUserRegistry().get(victim.getUniqueId());
            if (victimUser == null) {
                return;
            }
            if(event.getDamager() instanceof Player || event.getDamager() instanceof FishHook) {
                Player attacker;
                boolean playerDamage;
                if (event.getDamager() instanceof Player) {
                    attacker = (Player) event.getDamager();
                    playerDamage = true;
                } else {
                    FishHook projectile = (FishHook) event.getDamager();
                    if (projectile.getShooter() == null) {
                        return;
                    }
                    attacker = (Player) projectile.getShooter();
                    playerDamage = false;
                }
                User attackerUser = varo.getVaroGame().getUserRegistry().get(attacker.getUniqueId());
                if (attackerUser == null) {
                    return;
                }
                if (attackerUser.getTeam().equals(victimUser.getTeam())) {
                    event.setCancelled(playerDamage ? !varo.getVaroGame().TEAM_HIT_DAMAGE : !varo.getVaroGame().TEAM_ROD_DAMAGE);
                } else {
                    if(event.getDamage() >= 0.5) {
                        varo.getVaroGame().getCombatLogCache().put(victim.getUniqueId(), victimUser);
                        victim.sendMessage("§eDu bist im Combatlog. Logge dich nicht aus");
                    }
                }
            }
        }
    }
}
