package net.howtobedwars.varo.team;

import lombok.*;
import net.howtobedwars.varo.util.LocationSerializer;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VaroTeam implements Serializable {

    @Getter
    private final String teamName;

    @Getter
    private final String teamTag;

    @Getter
    private final List<String> players;

    private String teamChest;

    public static VaroTeam create(String teamName, String teamTag) {
        return new VaroTeam(teamName, teamTag, new ArrayList<>());
    }

    public void addPlayer(String name) {
        getPlayers().add(name);
    }

    public void removePlayer(String name) {
        getPlayers().remove(name);
    }

    public boolean contains(String name) {
        return getPlayers().contains(name);
    }

    public Location getTeamChest() {
        return LocationSerializer.deserialize(teamChest);
    }

    public void setTeamChest(Location location) {
        if(location != null) {
            this.teamChest = LocationSerializer.serialize(location);
        } else {
            this.teamChest = null;
        }
    }
}
