package net.howtobedwars.varo.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
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
    @Setter
    private Player player;

    @Getter
    private final VaroTeam team;

    @Getter
    @Setter
    private Tablist tablist;

    /* ONLINE TIME IN SECONDS */
    @Getter
    @Setter
    private int onlineTime;

    @Getter
    @Setter
    private CPSCheck cpsCheck;

    @Getter
    @Setter
    private ConfigUser configUser;

    private User(UUID uuid, VaroTeam team, Player player) {
        this.uuid = uuid;
        this.team = team;
        this.player = player;
    }

    public static User create(UUID uuid, VaroTeam team, Player player) {
        return new User(uuid, team, player);
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

            @Override
            public void run() {
                if (!getPlayer().isOnline()) {
                    cancel();
                }

                updateOnlineTime();
                if (getOnlineTime() < MAX_ONLINE_TIME) {
                    if (getOnlineTime() >= MAX_ONLINE_TIME - 10) {
                        if (getOnlineTime() != MAX_ONLINE_TIME) {
                            final int DIFFERENCE = MAX_ONLINE_TIME - getOnlineTime();
                            Bukkit.getScheduler().runTask(varo, () -> player.sendMessage("§eDu wirst in " + DIFFERENCE + " " + (DIFFERENCE > 1 ? "Sekunden" : "Sekunde") + " gekickt"));
                            return;
                        }
                    }
                } else if (getOnlineTime() == MAX_ONLINE_TIME) {
                    if (varo.getVaroGame().getCombatLogCache().asMap().containsKey(player.getUniqueId())) {
                        Bukkit.getScheduler().runTask(varo, () -> player.sendMessage("§eSolange du im Combatlog bist, musst du weitere 30 Sekunden auf dem Server bleiben"));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!varo.getVaroGame().getCombatLogCache().asMap().containsKey(player.getUniqueId())) {
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
