package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class ToggleDamageCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("howtobedwars.varo.togglepvp")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        boolean damage = !varo.getVaroGame().isDamage();
        varo.getVaroGame().setDamage(damage);
        commandSender.sendMessage("§eDer Schaden ist nun " + (damage ? "§aaktiviert" : "§cdeaktiviert"));
        return false;
    }
}
