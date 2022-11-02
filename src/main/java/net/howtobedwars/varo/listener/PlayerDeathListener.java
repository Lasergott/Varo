package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.DeadUsersConfig;
import net.howtobedwars.varo.config.UsersConfig;
import net.howtobedwars.varo.user.ConfigUser;
import net.howtobedwars.varo.user.User;
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
        event.setDeathMessage(null);
        if (varo.getVaroGame().getUserRegistry().containsKey(uuid)) {
            DeadUsersConfig deadUsersConfig = varo.getVaroFiles().getDeadUsersConfig();
            deadUsersConfig.add(uuid);
            varo.getVaroFiles().saveConfig(deadUsersConfig);
            varo.getVaroGame().getUserRegistry().remove(uuid);

            if (victim.getKiller() != null) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§a" + victim.getName() + " §ewurde von §c" + victim.getKiller() + " §7getötet"));
                User killerUser = varo.getVaroGame().getUserRegistry().get(victim.getKiller().getUniqueId());
                if (killerUser == null) {
                    return;
                }
                UsersConfig usersConfig = varo.getVaroFiles().getUsersConfig();
                ConfigUser configUser = killerUser.getConfigUser();
                configUser.setKills(configUser.getKills() + 1);
                usersConfig.update(configUser);
                varo.getVaroFiles().saveConfig(usersConfig);
            } else {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§a" + victim.getName() + " §eist gestorben"));
            }
            victim.kickPlayer(varo.getVaroGame().DEATH_MESSAGE);
        }
    }
}
