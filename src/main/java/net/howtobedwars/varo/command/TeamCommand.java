package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.MainConfig;
import net.howtobedwars.varo.config.TeamsConfig;
import net.howtobedwars.varo.team.VaroTeam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@AllArgsConstructor
public class TeamCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("howtobedwars.varo.teams")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if (args.length < 2 || args.length > 3) {
            commandSender.sendMessage("§c/team create <teamName> <teamTag>");
            commandSender.sendMessage("§c/team delete <teamName>");
            commandSender.sendMessage("§c/team <addPlayer|removePlayer> <teamName> <playerName>");
            commandSender.sendMessage("§c/team setPlayerLimit <limit>");
            return true;
        }
        String teamName = args[1];
        TeamsConfig teamsConfig = varo.getVaroFiles().getTeamsConfig();
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                Optional<VaroTeam> optionalTeam = teamsConfig.getTeams().stream()
                        .filter(team -> team.getTeamName().equals(teamName))
                        .findFirst();
                if (!optionalTeam.isPresent()) {
                    commandSender.sendMessage("§cDieses Team wurde nicht hinzugefügt");
                    return true;
                }
                VaroTeam team = optionalTeam.get();
                teamsConfig.remove(team);
                varo.getVaroFiles().saveConfig(teamsConfig);
                commandSender.sendMessage("§aTeam " + teamName + " wurde entfernt");
                return false;
            }
            if (args[0].equalsIgnoreCase("setPlayerLimit")) {
                int playerLimit;
                try {
                    playerLimit = Integer.parseInt(args[1]);
                    MainConfig mainConfig = varo.getVaroFiles().getMainConfig();
                    mainConfig.setPlayerTeamLimit(playerLimit);
                    varo.getVaroFiles().saveConfig(mainConfig);
                    commandSender.sendMessage("§aDas Limit an Spielern pro Team wurde auf " + playerLimit + " gesetzt");
                    return false;
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("§cBitte gib eine valide Zahl an");
                    return true;
                }
            }
        } else {
            if (args[0].equalsIgnoreCase("addPlayer") || args[0].equalsIgnoreCase("removePlayer")) {
                Optional<VaroTeam> optionalTeam = teamsConfig.getTeams().stream()
                        .filter(team -> team.getTeamName().equals(teamName))
                        .findFirst();

                if (!optionalTeam.isPresent()) {
                    commandSender.sendMessage("§cDieses Team wurde nicht hinzugefügt");
                    return true;
                }
                VaroTeam team = optionalTeam.get();
                String player = args[2];
                if (args[0].equalsIgnoreCase("addPlayer")) {
                    if (team.contains(player)) {
                        commandSender.sendMessage("§cDieses Spieler ist bereits im Team eingetragen");
                        return true;
                    }
                    if (team.getPlayers().size() >= varo.getVaroGame().PLAYER_TEAM_LIMIT) {
                        commandSender.sendMessage("§cEs sind bereits " + varo.getVaroGame().PLAYER_TEAM_LIMIT + " Spieler im Team eingetragen");
                        return true;
                    }
                    team.addPlayer(player);
                    teamsConfig.update(team);
                    varo.getVaroFiles().saveConfig(teamsConfig);
                    commandSender.sendMessage("§aSpieler " + player + " ist nun in Team " + teamName);
                    return false;

                }
                if (args[0].equalsIgnoreCase("removePlayer")) {
                    if (!team.contains(player)) {
                        commandSender.sendMessage("§cDieses Spieler wurde nicht im Team eingetragen");
                        return true;
                    }
                    team.removePlayer(player);
                    teamsConfig.update(team);
                    varo.getVaroFiles().saveConfig(teamsConfig);
                    commandSender.sendMessage("§aSpieler " + player + " ist nun nicht mehr in Team " + teamName);
                    return false;
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                Optional<VaroTeam> optionalTeam = teamsConfig.getTeams().stream()
                        .filter(team -> team.getTeamName().equals(teamName))
                        .findFirst();
                if (optionalTeam.isPresent()) {
                    commandSender.sendMessage("§cDieses Team wurde bereits hinzugefügt");
                    return true;
                }
                String teamTag = args[2];
                if(teamTag.length() > 5) {
                    commandSender.sendMessage("§cDer Kürzel des Teams darf maximal 5 Zeichen lang sein");
                    return true;
                }
                VaroTeam team = VaroTeam.create(teamName, teamTag);
                teamsConfig.add(team);
                varo.getVaroFiles().saveConfig(teamsConfig);
                commandSender.sendMessage("§aTeam " + teamName + " wurde hinzugefügt");
            }
        }
        return false;
    }
}
