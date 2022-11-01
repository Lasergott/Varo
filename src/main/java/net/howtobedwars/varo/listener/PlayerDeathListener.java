package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.DeadUsersConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID uuid = victim.getUniqueId();
        if(varo.getVaroGame().getUserRegistry().containsKey(uuid)) {
            DeadUsersConfig deadUsersConfig = varo.getVaroFiles().getDeadUsersConfig();
            deadUsersConfig.addUser(uuid);
            varo.getVaroFiles().saveConfig(deadUsersConfig);
            varo.getVaroGame().getUserRegistry().remove(uuid);

            if(victim.getKiller() != null) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§a" + victim.getName() + " §ewurde von §c" + victim.getKiller() + " §7getötet"));
            } else {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§a" + victim.getName() + " §eist gestorben"));
            }
            victim.kickPlayer(varo.getVaroGame().DEATH_MESSAGE);
        }
    }
}
