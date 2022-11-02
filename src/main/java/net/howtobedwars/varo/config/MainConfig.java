package net.howtobedwars.varo.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class MainConfig extends Config {

    @Getter
    private final int maxOnlineTime;

    @Getter
    @Setter
    private int playerTeamLimit;

    @Getter
    private final int antiCombatLogTime;

    @Getter
    private final int countdownTime;

    @Getter
    private final int protectionTime;

    @Getter
    private final int broadcastTime;

    @Getter
    private final String timeOverMessage;

    @Getter
    private final String deathMessage;

    @Getter
    private final boolean teamHitDamage;

    @Getter
    private final boolean teamRodDamage;

    private MainConfig(File file) {
        super(file.getName());
        this.maxOnlineTime = 30 * 60;
        this.playerTeamLimit = 2;
        this.antiCombatLogTime = 30;
        this.countdownTime = 31;
        this.protectionTime = 30;
        this.broadcastTime = 3 * 60;
        this.timeOverMessage = "§cDie 30 Minuten sind abgelaufen.\nMorgen kannst du erneut auf den Server joinen.";
        this.deathMessage = "§cDu bist gestorben.\nDu kannst nicht erneut auf den Server joinen.";
        this.teamHitDamage = false;
        this.teamRodDamage = false;
    }

    public static MainConfig create(File file) {
        return new MainConfig(file);
    }
}
