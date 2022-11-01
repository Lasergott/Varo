package net.howtobedwars.varo.tablist;

import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.team.VaroTeam;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Tablist {

    private final Varo varo;
    private final User user;
    private final Scoreboard scoreboard;

    public Tablist(Varo varo, User user) {
        this.varo = varo;
        this.user = user;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.getObjective("howtobedwars");
        if(objective == null) {
            objective = scoreboard.registerNewObjective("howtobedwars", "varo");
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        if(varo.getVaroFiles().getTeamsConfig().getTeams().size() == 0) {
            Bukkit.getLogger().warning("Error while creating tablist: no team has been added yet");
            return;
        }
        for (VaroTeam team : varo.getVaroFiles().getTeamsConfig().getTeams()) {
            String teamTag = team.getTeamTag();
            Team scoreboardTeam = scoreboard.getTeam(teamTag);
            if(scoreboardTeam == null) {
                scoreboardTeam = scoreboard.registerNewTeam(teamTag);
            }
            scoreboardTeam.setPrefix("§a" + teamTag + " §7| " + "§f");
        }
    }

    public void set() {
        varo.getVaroGame().getUserRegistry().values().forEach(targetUser -> {
            VaroTeam team = targetUser.getTeam();
            if (team == null) {
                return;
            }
            scoreboard.getTeam(team.getTeamTag()).addEntry(targetUser.getPlayer().getName());
        });
        scoreboard.getTeam(user.getTeam().getTeamTag()).addEntry(user.getPlayer().getName());
        user.getPlayer().setScoreboard(scoreboard);
        user.setTablist(this);
    }

    public void remove() {
        scoreboard.getTeam(user.getTeam().getTeamTag()).removeEntry(user.getPlayer().getName());
        user.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        user.setTablist(null);
    }

    public void update() {
        varo.getVaroGame().getUserRegistry().values().forEach(targetUser -> {
            VaroTeam team = targetUser.getTeam();
            if (team == null) {
                return;
            }
            scoreboard.getTeam(team.getTeamTag()).addEntry(targetUser.getPlayer().getName());
        });
    }
}
