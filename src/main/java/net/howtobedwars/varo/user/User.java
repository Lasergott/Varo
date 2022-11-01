package net.howtobedwars.varo.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.event.UserTimeOverEvent;
import net.howtobedwars.varo.tablist.Tablist;
import net.howtobedwars.varo.team.VaroTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class User {

    @Getter
    private final UUID uuid;

    @Getter
    private final Player player;

    @Getter
    private final VaroTeam team;

    @Getter
    @Setter
    private Tablist tablist;

    /* ONLINE TIME IN SECONDS */
    @Getter
    @Setter
    private int onlineTime;

    private User(UUID uuid, VaroTeam team) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.team = team;
    }

    public static User create(UUID uuid, VaroTeam team) {
        return new User(uuid, team);
    }

    private void updateOnlineTime() {
        setOnlineTime(getOnlineTime() + 1);
    }

    public User getUser() {
        return this;
    }

    public void checkForTimeOver(Varo varo) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!getPlayer().isOnline()) {
                    cancel();
                }

                updateOnlineTime();
                if (getOnlineTime() == varo.getVaroGame().MAX_ONLINE_TIME) {
                    if(varo.getVaroGame().getCombatLog().asMap().containsKey(player.getUniqueId())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(!varo.getVaroGame().getCombatLog().asMap().containsKey(player.getUniqueId())) {
                                    Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Varo.class), () -> Bukkit.getPluginManager().callEvent(new UserTimeOverEvent(getUser())));
                                    cancel();
                                }
                            }
                        }.runTaskTimer(JavaPlugin.getPlugin(Varo.class), 0, varo.getVaroGame().ANTI_COMBAT_LOG_TIME * 20L);
                    }
                }
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(Varo.class), 0, 20);
    }
}
