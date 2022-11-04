package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class ToggleBlockBreakCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("howtobedwars.varo.toggleblockbreak")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        boolean blockBreak = !varo.getVaroGame().isBlockBreak();
        varo.getVaroGame().setBlockBreak(blockBreak);
        commandSender.sendMessage("§eDas Abbauen von Blöcken ist nun " + (blockBreak ? "§aaktiviert" : "§cdeaktiviert"));
        return false;
    }
}
