package net.howtobedwars.varo.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.howtobedwars.varo.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class UserTimeOverEvent extends Event {

    @Getter
    private final User user;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
