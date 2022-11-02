package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.BroadcastsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@AllArgsConstructor
public class BroadcastCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("howtobedwars.varo.broadcast")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if (args.length != 2 && args.length < 3) {
            commandSender.sendMessage("§c/broadcast add <key> <message>");
            commandSender.sendMessage("§c/broadcast remove <key>");
            return true;
        }
        String messageKey = args[1];
        BroadcastsConfig broadcastsConfig = varo.getVaroFiles().getBroadcastsConfig();
        if (args.length == 2) {
            if(args[0].equalsIgnoreCase("remove")) {
                if (!broadcastsConfig.contains(messageKey)) {
                    commandSender.sendMessage("§cEs wurde kein Broadcast mit diesem Key hinzugefügt");
                    return true;
                }
                broadcastsConfig.remove(messageKey);
                varo.getVaroFiles().saveConfig(broadcastsConfig);
                commandSender.sendMessage("§aBroadcast wurde entfernt");
                return false;
            }
        }
        if (broadcastsConfig.contains(messageKey)) {
            commandSender.sendMessage("§cEs wurde bereits ein Broadcast mit diesem Key hinzugefügt");
            return true;
        }
        StringBuilder messageBuilder = new StringBuilder();
        Arrays.stream(args).skip(2).forEach(message -> messageBuilder.append(message).append(" "));
        broadcastsConfig.add(messageKey, messageBuilder.toString().trim());
        varo.getVaroFiles().saveConfig(broadcastsConfig);
        commandSender.sendMessage("§aBroadcast wurde hinzugefügt");
        return false;
    }
}
