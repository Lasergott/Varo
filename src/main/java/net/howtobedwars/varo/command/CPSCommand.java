package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.cps.CPSCheck;
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
            if (player.hasMetadata("cps-check")) {
                removeCPSCheck(player);
            } else {
                CPSCheck cpsCheck = new CPSCheck(varo, player, target);
                player.setMetadata("cps-check", new FixedMetadataValue(varo, cpsCheck));
                varo.getVaroGame().getCpsCheckCache().put(target.getUniqueId(), cpsCheck);
                player.sendMessage("§aDu siehst nun die Klicks des Spielers " + cpsCheck.getTarget().getName());
            }
        }
        return false;
    }

    private void removeCPSCheck(Player player) {
        CPSCheck cpsCheck = (CPSCheck) player.getMetadata("cps-check").get(0).value();
        cpsCheck.uncheck();
        player.removeMetadata("cps-check", varo);
        player.sendMessage("§aDu siehst nun nicht mehr die Klicks des Spielers " + cpsCheck.getTarget().getName());
    }
}
