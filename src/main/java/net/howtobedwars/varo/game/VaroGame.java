package net.howtobedwars.varo.game;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.MainConfig;
import net.howtobedwars.varo.tablist.Tablist;
import net.howtobedwars.varo.user.User;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class VaroGame {

    private final Varo varo;
    public final int MAX_ONLINE_TIME;
    public final int PLAYER_TEAM_LIMIT;
    public final int ANTI_COMBAT_LOG_TIME;
    public final int COUNTDOWN_TIME;
    public final int PROTECTION_TIME;
    public final String TIME_OVER_MESSAGE;
    public final String DEATH_MESSAGE;
    public final boolean TEAM_HIT_DAMAGE;
    public final boolean TEAM_ROD_DAMAGE;

    @Getter
    @Setter
    private boolean starting;

    @Getter
    @Setter
    private boolean protectionTime;

    @Getter
    private final Map<UUID, User> userRegistry;

    @Getter
    private final Cache<UUID, User> combatLog;

    public VaroGame(Varo varo) {
        this.varo = varo;
        MainConfig mainConfig = varo.getVaroFiles().getMainConfig();
        this.MAX_ONLINE_TIME = mainConfig.getMaxOnlineTime();
        this.PLAYER_TEAM_LIMIT = mainConfig.getPlayerTeamLimit();
        this.ANTI_COMBAT_LOG_TIME = mainConfig.getAntiCombatLogTime();
        this.COUNTDOWN_TIME = mainConfig.getCountdownTime();
        this.PROTECTION_TIME = mainConfig.getProtectionTime();
        this.TIME_OVER_MESSAGE = mainConfig.getTimeOverMessage();
        this.DEATH_MESSAGE = mainConfig.getDeathMessage();
        this.TEAM_HIT_DAMAGE = mainConfig.isTeamHitDamage();
        this.TEAM_ROD_DAMAGE = mainConfig.isTeamRodDamage();
        this.starting = false;
        this.protectionTime = false;
        this.userRegistry = new HashMap<>();
        this.combatLog = CacheBuilder.newBuilder()
                .expireAfterWrite(ANTI_COMBAT_LOG_TIME - 1, TimeUnit.SECONDS)
                .build();
    }

    public void setTablist(User user) {
        Tablist tablist = new Tablist(varo, user);
        tablist.set();
    }

    public void removeTablist(User user) {
        user.getTablist().remove();
        user.setTablist(null);
    }

    public void updateTablist() {
        getUserRegistry().values().forEach(user -> {
            if(user.getTablist() != null) {
                user.getTablist().update();
            }
        });
    }
}
