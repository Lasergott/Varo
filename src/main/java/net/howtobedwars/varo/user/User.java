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

import java.util.Arrays;
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
            final int MAX_ONLINE_TIME = varo.getVaroGame().MAX_ONLINE_TIME;
            final int LAST_TEN_SECONDS = MAX_ONLINE_TIME + 50;
            @Override
            public void run() {
                if (!getPlayer().isOnline()) {
                    cancel();
                }

                updateOnlineTime();
                if (getOnlineTime() >= LAST_TEN_SECONDS) {
                    final int DIFFERENCE = MAX_ONLINE_TIME - getOnlineTime();
                    Bukkit.getScheduler().runTask(varo, () -> player.sendMessage("§eDu wirst in " + DIFFERENCE + " " + (DIFFERENCE > 1 ? "Sekunden" : "Sekunde") + " gekickt"));
                    return;
                }
                if (getOnlineTime() == varo.getVaroGame().MAX_ONLINE_TIME) {
                    if (varo.getVaroGame().getCombatLog().asMap().containsKey(player.getUniqueId())) {
                        Bukkit.getScheduler().runTask(varo, () -> player.sendMessage("§eSolange du im Combatlog bist, musst du weitere 30 Sekunden auf dem Server bleiben"));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!varo.getVaroGame().getCombatLog().asMap().containsKey(player.getUniqueId())) {
                                    Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Varo.class), () -> Bukkit.getPluginManager().callEvent(new UserTimeOverEvent(getUser())));
                                    cancel();
                                }
                            }
                        }.runTaskTimer(JavaPlugin.getPlugin(Varo.class), 0, varo.getVaroGame().ANTI_COMBAT_LOG_TIME * 20L);
                    } else {
                        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Varo.class), () -> Bukkit.getPluginManager().callEvent(new UserTimeOverEvent(getUser())));
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(Varo.class), 0, 20);
    }
}
