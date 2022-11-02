package net.howtobedwars.varo.config;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BroadcastsConfig extends Config {

    @Getter
    private final Map<String, String> messages;

    private BroadcastsConfig(File file) {
        super(file.getName());
        this.messages = new HashMap<>();
    }

    public static BroadcastsConfig create(File file) {
        return new BroadcastsConfig(file);
    }

    public void add(String key, String message) {
        messages.put(key, message);
    }

    public void remove(String key) {
        messages.remove(key);
    }

    public boolean contains(String key) {
        return messages.containsKey(key);
    }
}
