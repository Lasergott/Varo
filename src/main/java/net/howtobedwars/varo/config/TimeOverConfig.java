package net.howtobedwars.varo.config;

import lombok.Getter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class TimeOverConfig extends Config {

    @Getter
    private final Set<String> users;

    private TimeOverConfig(File file) {
        super(file.getName());
        this.users = new HashSet<>();
    }

    public void add(String name) {
         users.add(name);
    }

    public void remove(String name) {
        users.remove(name);
    }

    public void clear() {
        users.clear();
    }

    public boolean contains(String name) {
        return users.contains(name);
    }

    public static TimeOverConfig create(File file) {
        return new TimeOverConfig(file);
    }
}
