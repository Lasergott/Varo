package net.howtobedwars.varo.config;

import lombok.Getter;

import java.io.File;
import java.util.*;

public class DeadUsersConfig extends Config {

    @Getter
    private final Set<UUID> users;

    private DeadUsersConfig(File file) {
        super(file.getName());
        this.users = new HashSet<>();
    }

    public static DeadUsersConfig create(File file) {
        return new DeadUsersConfig(file);
    }

    public void add(UUID uuid) {
        users.add(uuid);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return users.contains(uuid);
    }
}
