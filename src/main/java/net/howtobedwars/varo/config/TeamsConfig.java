package net.howtobedwars.varo.config;

import lombok.Getter;
import net.howtobedwars.varo.team.VaroTeam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamsConfig extends Config {

    @Getter
    private final List<VaroTeam> teams;

    private TeamsConfig(File file) {
        super(file.getName());
        this.teams = new ArrayList<>();
    }

    public void add(VaroTeam team) {
        teams.add(team);
    }

    public void remove(VaroTeam team) {
        teams.remove(team);
    }

    public void update(VaroTeam team) {
        remove(team);
        add(team);
    }

    public static TeamsConfig create(File file) {
        return new TeamsConfig(file);
    }
}
