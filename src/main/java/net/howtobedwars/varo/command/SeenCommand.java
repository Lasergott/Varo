package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.user.ConfigUser;
import net.howtobedwars.varo.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class SeenCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("howtobedwars.varo.seen")) {
            commandSender.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if(args.length != 1) {
            commandSender.sendMessage("§c/seen <playerName>");
            return true;
        }
        String name = args[0];
        Optional<ConfigUser> optionalConfigUser = varo.getVaroFiles().getUsersConfig().get(name);
        if(!optionalConfigUser.isPresent()) {
            commandSender.sendMessage("§cDieser Spieler war noch nie auf dem Server");
            return true;
        }
        ConfigUser configUser = optionalConfigUser.get();
        String lastJoin = "§a" + (configUser.getLastJoin() == 0 ? "§c/" : new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Date(configUser.getLastJoin())));
        String lastQuit = "§c" + (configUser.getLastQuit() == 0 ? "§c/" : new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Date(configUser.getLastQuit()))) + " " + (configUser.getLastQuit() < configUser.getLastJoin() ? "(Aktuell online)" : "");
        commandSender.sendMessage("§eInfos zu " + name + ":");
        commandSender.sendMessage("§eZuletzt gejoined: " + lastJoin);
        commandSender.sendMessage("§eZuletzt gequittet: " + lastQuit);
        return false;
    }
}
