package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@AllArgsConstructor
public class CPSCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("howtobedwars.varo.cps")) {
            player.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if (args.length == 0) {
            if (!player.hasMetadata("cps-check")) {
                player.sendMessage("§c/cps <player>");
                return true;
            }
            removeCPSCheck(player);
            return false;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cDieser Spieler ist zurzeit nicht online");
                return true;
            }
            User user = varo.getVaroGame().getUserRegistry().get(target.getUniqueId());
            if(user == null) {
                player.sendMessage("§cFehler beim CpsCheck: User wurde nicht erstellt");
                return true;
            }
            if(player.hasMetadata("cps-check")) {
                removeCPSCheck(player);
            } else {
                CPSCheck cpsCheck = user.getCpsCheck();
                cpsCheck.setChecker(player);
                player.setMetadata("cps-check", new FixedMetadataValue(varo, cpsCheck));
            }
        }
        return false;
    }

    private void removeCPSCheck(Player player) {
        CPSCheck cpsCheck = (CPSCheck) player.getMetadata("cps-check").get(0).value();
        cpsCheck.setChecker(null);
        player.removeMetadata("cps-check", varo);
        player.sendMessage("§aDu siehst nun nicht mehr die Klicks des Spielers " + cpsCheck.getPlayer().getName());
    }
}
