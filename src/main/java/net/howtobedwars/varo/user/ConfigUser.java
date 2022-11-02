package net.howtobedwars.varo.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUser {

    @Getter
    private final UUID uuid;

    @Getter
    private final String name;

    @Getter
    @Setter
    private long lastJoin;

    @Getter
    @Setter
    private long lastQuit;

    @Getter
    @Setter
    private int kills;

    public static ConfigUser create(UUID uuid, String name) {
        return new ConfigUser(uuid, name);
    }
}
