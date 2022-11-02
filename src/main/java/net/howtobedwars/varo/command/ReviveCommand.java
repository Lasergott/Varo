package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.DeadUsersConfig;
import net.howtobedwars.varo.config.TimeOverConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@AllArgsConstructor
public class ReviveCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("howtobedwars.varo.revive")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if (args.length != 1) {
            commandSender.sendMessage("§c/revive <playerUUID>");
            return true;
        }
        DeadUsersConfig deadUsersConfig = varo.getVaroFiles().getDeadUsersConfig();
        String uuid = args[0];
        if (!deadUsersConfig.contains(UUID.fromString(uuid))) {
            commandSender.sendMessage("§cDieser Spieler ist nicht tot, daher kannst du ihn nicht wiederbeleben");
            return true;
        }
        deadUsersConfig.remove(UUID.fromString(uuid));
        varo.getVaroFiles().saveConfig(deadUsersConfig);
        commandSender.sendMessage("§aDer Spieler mit der UUID " + uuid + " wurde wiederbelebet");
        return false;
    }
}
