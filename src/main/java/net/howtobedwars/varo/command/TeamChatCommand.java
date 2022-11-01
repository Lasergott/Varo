package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.SpawnsConfig;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@AllArgsConstructor
public class TeamChatCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        if(args.length == 0) {
            player.sendMessage("§c/teamchat <message>");
            return true;
        }
        User user = varo.getVaroGame().getUserRegistry().get(player.getUniqueId());
        if(user == null) {
            player.sendMessage("§cFehler beim Senden einer Teamnachricht: User wurde nicht erstellt");
            return true;
        }
        StringBuilder messageBuilder = new StringBuilder();
        Arrays.stream(args).skip(0).forEach(message -> messageBuilder.append(message).append(" "));
        user.getTeam().getPlayers()
                .stream()
                .filter(name -> Bukkit.getPlayer(name) != null)
                .map(Bukkit::getPlayer)
                .forEach(player1 -> player1.sendMessage("§7[§eTeamChat§7] §a" + player.getName() + "§7: §f" + messageBuilder));

        return false;
    }
}
