package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.SpawnsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class AddSpawnCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("howtobedwars.varo.addspawn")) {
            player.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        if(args.length != 0) {
            player.sendMessage("§c/addSpawn");
            return true;
        }
        SpawnsConfig spawnsConfig =  varo.getVaroFiles().getSpawnsConfig();
        spawnsConfig.addSpawn(player.getLocation());
        varo.getVaroFiles().saveConfig(spawnsConfig);
        return false;
    }
}
