package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.DeadUsersConfig;
import net.howtobedwars.varo.config.TimeOverConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@AllArgsConstructor
public class PlayerLoginListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        TimeOverConfig timeOverConfig = varo.getVaroFiles().getTimeOverConfig();
        if(timeOverConfig.getUsers().contains(player.getName())) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(varo.getVaroGame().TIME_OVER_MESSAGE);
            return;
        }
        DeadUsersConfig deadUsersConfig = varo.getVaroFiles().getDeadUsersConfig();
        if(deadUsersConfig.getUsers().contains(player.getUniqueId())) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(varo.getVaroGame().DEATH_MESSAGE);
            return;
        }
    }
}
