package net.howtobedwars.varo.user;

import lombok.*;

import java.util.UUID;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUser {

    private final UUID uuid;
    private final String name;
    private long lastJoin;
    private long lastQuit;
    private int kills;

    public static ConfigUser create(UUID uuid, String name) {
        return new ConfigUser(uuid, name);
    }
}
