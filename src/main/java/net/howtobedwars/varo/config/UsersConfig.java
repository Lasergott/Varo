package net.howtobedwars.varo.config;

import lombok.Getter;
import net.howtobedwars.varo.user.ConfigUser;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UsersConfig extends Config {

    @Getter
    private Set<ConfigUser> users;

    private UsersConfig(File file) {
        super(file.getName());
    }

    public static UsersConfig create(File file) {
        return new UsersConfig(file);
    }

    public void add(ConfigUser configUser) {
        users.add(configUser);
    }

    public void remove(ConfigUser configUser) {
        users.remove(configUser);
    }

    public void update(ConfigUser configUser) {
        remove(configUser);
        add(configUser);
    }

    public Optional<ConfigUser> get(UUID uuid) {
        return users
                .stream()
                .filter(configUser -> configUser.getUuid().equals(uuid))
                .findFirst();
    }

    public Optional<ConfigUser> get(String name) {
        return users
                .stream()
                .filter(configUser -> configUser.getName().equals(name))
                .findFirst();
    }
}
