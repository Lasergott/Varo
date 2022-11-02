package net.howtobedwars.varo.config;

import lombok.Getter;
import net.howtobedwars.varo.util.LocationSerializer;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpawnsConfig extends Config {

    @Getter
    private final List<String> spawns;

    private SpawnsConfig(File file) {
        super(file.getName());
        this.spawns = new ArrayList<>();
    }

    public static SpawnsConfig create(File file) {
        return new SpawnsConfig(file);
    }

    public void add(Location location) {
        spawns.add(LocationSerializer.serialize(location));
    }
}
