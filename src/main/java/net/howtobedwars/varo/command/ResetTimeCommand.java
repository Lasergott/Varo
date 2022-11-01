package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.TimeOverConfig;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ResetTimeCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("howtobedwars.varo.time.reset")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if(args.length != 2 && !args[0].equalsIgnoreCase("resetTime")) {
            commandSender.sendMessage("§c/resetTime <player | all>");
            return true;
        }
        if(args[1].equalsIgnoreCase("all")) {
            varo.getVaroGame().getUserRegistry().values().forEach(user -> user.setOnlineTime(0));
            commandSender.sendMessage("§aDie aktuelle Zeit aller Spieler wurde auf 0 Sekunden zurückgesetzt");
        } else {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                commandSender.sendMessage("§cDieser Spieler ist zurzeit nicht online");
                return true;
            }
            User user = varo.getVaroGame().getUserRegistry().get(target.getUniqueId());
            if(user == null) {
                commandSender.sendMessage("§cEs konnte kein Eintrag für diesen User gefunden werden");
                return true;
            }
            user.setOnlineTime(0);
            commandSender.sendMessage("§aDie aktuelle Zeit des Spielers " + target.getName() + " wurde auf 0 Sekunden zurückgesetzt");
        }
        return false;
    }
}
