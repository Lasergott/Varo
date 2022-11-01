package net.howtobedwars.varo.command;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.util.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

@AllArgsConstructor
public class StartCommand implements CommandExecutor {

    private final Varo varo;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("howtobedwars.varo.start")) {
            player.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }
        int i = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            String serializedLocation = varo.getVaroFiles().getSpawnsConfig().getSpawns().get(i);
            Location location = LocationSerializer.deserialize(serializedLocation);
            target.teleport(location);
            i++;
        }
        varo.getVaroGame().setStarting(true);
        new BukkitRunnable() {
            int seconds = varo.getVaroGame().COUNTDOWN_TIME;

            @Override
            public void run() {
                seconds--;
                if (Arrays.asList(30, 15, 10, 5, 4, 3, 2, 1).contains(seconds)) {
                    Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage("§eDas Varo startet in " + seconds + " " + (seconds != 1 ? "Sekunden" : "Sekunde")));
                } else if (seconds == 0) {
                    varo.getVaroGame().setStarting(false);
                    varo.getVaroGame().setProtectionTime(true);
                    Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage("§eDas Varo hat begonnen! Viel Erfolg"));
                    Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage("§eDie Schutzzeit endet in 30 Sekunden"));
                    Bukkit.getScheduler().runTaskLater(varo, () -> {
                        Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage("§eDie Schutzzeit ist vorbei!"));
                        varo.getVaroGame().setProtectionTime(false);
                    }, varo.getVaroGame().PROTECTION_TIME * 20L);
                    cancel();
                }
            }
        }.runTaskTimer(varo, 0, 20);
        return false;
    }
}
