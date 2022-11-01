package net.howtobedwars.varo.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class MainConfig extends Config {

    @Getter
    @SerializedName("zeitProTag")
    private final int maxOnlineTime;

    @Getter
    @Setter
    @SerializedName("spielerProTeam")
    private int playerTeamLimit;

    @Getter
    @SerializedName("antiCombatLogZeit")
    private final int antiCombatLogTime;

    @Getter
    @SerializedName("startCountdown")
    private final int countdownTime;

    @Getter
    @SerializedName("schutzzeit")
    private final int protectionTime;

    @Getter
    @SerializedName("zeitVorbeiNachricht")
    private final String timeOverMessage;

    @Getter
    @SerializedName("todesNachricht")
    private final String deathMessage;

    @Getter
    @SerializedName("teamHitSchaden")
    private final boolean teamHitDamage;

    @Getter
    @SerializedName("teamRodSchaden")
    private final boolean teamRodDamage;

    private MainConfig(File file) {
        super(file.getName());
        this.maxOnlineTime = 30 * 60;
        this.playerTeamLimit = 2;
        this.antiCombatLogTime = 30;
        this.countdownTime = 31;
        this.protectionTime = 30;
        this.timeOverMessage = "§cDie 30 Minuten sind abgelaufen.\nMorgen kannst du erneut auf den Server joinen.";
        this.deathMessage = "§cDu bist gestorben.\nDu kannst nicht erneut auf den Server joinen.";
        this.teamHitDamage = false;
        this.teamRodDamage = false;
    }

    public static MainConfig create(File file) {
        return new MainConfig(file);
    }
}
