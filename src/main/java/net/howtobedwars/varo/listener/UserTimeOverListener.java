package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.event.UserTimeOverEvent;
import net.howtobedwars.varo.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class UserTimeOverListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onUserTimeOver(UserTimeOverEvent event) {
        User user = event.getUser();
        varo.getVaroGame().getUserRegistry().remove(user.getUuid());
        user.getPlayer().kickPlayer(varo.getVaroGame().TIME_OVER_MESSAGE);
        varo.getVaroFiles().getTimeOverConfig().add(user.getPlayer().getName());
        varo.getVaroFiles().saveConfig(varo.getVaroFiles().getTimeOverConfig());
    }
}
