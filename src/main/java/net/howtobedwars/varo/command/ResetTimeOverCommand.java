package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.TimeOverConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class ResetTimeOverCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("howtobedwars.varo.timeover.reset")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if (args.length != 1) {
            commandSender.sendMessage("§c/resetTimeOver <player | all>");
            return true;
        }
        TimeOverConfig timeOverConfig = varo.getVaroFiles().getTimeOverConfig();
        if (args[0].equalsIgnoreCase("all")) {
            timeOverConfig.clear();
            varo.getVaroFiles().saveConfig(timeOverConfig);
            commandSender.sendMessage("§aAlle Spieler können nun wieder auf den Server joinen");
            return false;
        }
        String player = args[0];
        if (!timeOverConfig.contains(player)) {
            commandSender.sendMessage("§cDieser Spieler war noch nicht 30 Minuten auf dem Server. Um seine aktuelle Zeit zurückzusetzen, nutze /resetTime <player>");
            return true;
        }
        timeOverConfig.remove(player);
        varo.getVaroFiles().saveConfig(timeOverConfig);
        commandSender.sendMessage("§aDer Spieler " + player + " kann nun wieder auf den Server joinen");
        return false;
    }
}
