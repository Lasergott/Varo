package net.howtobedwars.varo.game;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.MainConfig;
import net.howtobedwars.varo.config.TimeOverConfig;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.tablist.Tablist;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class VaroGame {

    private final Varo varo;
    public final int MAX_ONLINE_TIME;
    public final int PLAYER_TEAM_LIMIT;
    public final int ANTI_COMBAT_LOG_TIME;
    public final int COUNTDOWN_TIME;
    public final int PROTECTION_TIME;
    public final int BROADCAST_TIME;
    public final String TIME_OVER_MESSAGE;
    public final String DEATH_MESSAGE;
    public final boolean TEAM_HIT_DAMAGE;
    public final boolean TEAM_ROD_DAMAGE;
    private final Tablist tablist;

    @Getter
    @Setter
    private boolean starting;

    @Getter
    @Setter
    private boolean protectionTime;

    @Getter
    private final Map<UUID, User> userRegistry;

    @Getter
    private final Cache<UUID, User> combatLogCache;

    @Getter
    private final Cache<UUID, CPSCheck> cpsCheckCache;

    public VaroGame(Varo varo) {
        this.varo = varo;
        MainConfig mainConfig = varo.getVaroFiles().getMainConfig();
        this.MAX_ONLINE_TIME = mainConfig.getMaxOnlineTime();
        this.PLAYER_TEAM_LIMIT = mainConfig.getPlayerTeamLimit();
        this.ANTI_COMBAT_LOG_TIME = mainConfig.getAntiCombatLogTime();
        this.COUNTDOWN_TIME = mainConfig.getCountdownTime();
        this.PROTECTION_TIME = mainConfig.getProtectionTime();
        this.BROADCAST_TIME = mainConfig.getBroadcastTime();
        this.TIME_OVER_MESSAGE = mainConfig.getTimeOverMessage();
        this.DEATH_MESSAGE = mainConfig.getDeathMessage();
        this.TEAM_HIT_DAMAGE = mainConfig.isTeamHitDamage();
        this.TEAM_ROD_DAMAGE = mainConfig.isTeamRodDamage();
        this.starting = false;
        this.protectionTime = false;
        this.tablist = new Tablist(varo);
        this.userRegistry = new HashMap<>();
        this.combatLogCache = CacheBuilder.newBuilder()
                .expireAfterWrite(ANTI_COMBAT_LOG_TIME - 1, TimeUnit.SECONDS)
                .build();
        this.cpsCheckCache = CacheBuilder.newBuilder().build();
        sendBroadcasts();
    }

    private void sendBroadcasts() {
        new BukkitRunnable() {
            int key;
            final List<String> messages = new ArrayList<>(varo.getVaroFiles().getBroadcastsConfig().getMessages().values());
            @Override
            public void run() {
                if(messages.isEmpty()) {
                    cancel();
                }
                if(key >= messages.size()) {
                    key = 0;
                }
                String message = messages.get(key);
                Bukkit.broadcastMessage(message);
                key++;
            }
        }.runTaskTimer(varo, BROADCAST_TIME * 20L, BROADCAST_TIME * 20L);
    }

    private void clearTimeOver() {
        new BukkitRunnable() {
            @Override
            public void run() {
                TimeOverConfig timeOverConfig = varo.getVaroFiles().getTimeOverConfig();
                timeOverConfig.clear();
                varo.getVaroFiles().saveConfig(timeOverConfig);
            }
        }.runTaskLaterAsynchronously(varo, 24 * 60 * 60 * 20);
    }

    public void setTablist(User user) {
        tablist.set(user);
    }

    public void updateTablist() {
        getUserRegistry().values().forEach(user -> tablist.update());
    }
}
