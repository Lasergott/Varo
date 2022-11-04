package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.TeamsConfig;
import net.howtobedwars.varo.team.VaroTeam;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

@AllArgsConstructor
public class BlockBreakListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!varo.getVaroGame().isBlockBreak()) {
            event.setCancelled(true);
            return;
        }
        if (varo.getVaroGame().isStarting()) {
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {
            User user = varo.getVaroGame().getUserRegistry().get(player.getUniqueId());
            if (user == null) {
                return;
            }
            VaroTeam team = user.getTeam();
            Optional<VaroTeam> optionalTeam = varo.getVaroFiles().getTeamsConfig().getTeams()
                    .stream()
                    .filter(team1 -> team1.getTeamChest() != null)
                    .filter(team1 -> team1.getTeamChest().equals(block.getLocation()))
                    .findFirst();

            if (optionalTeam.isPresent()) {
                if(!optionalTeam.get().equals(team)) {
                    player.sendMessage("§cDu kannst die Teamchest anderer Teams nicht zerstören");
                    event.setCancelled(true);
                } else {
                    player.sendMessage("§aDu hast eure Teamchest zerstört");
                    updateTeam(user);
                }
            }
        }
    }

    private void updateTeam(User user) {
        Bukkit.getScheduler().runTaskAsynchronously(varo, () -> {
            TeamsConfig teamsConfig = varo.getVaroFiles().getTeamsConfig();
            teamsConfig.getTeams()
                    .stream()
                    .filter(team1 -> team1.getTeamTag().equals(user.getTeam().getTeamTag()))
                    .forEach(team1 -> team1.setTeamChest(null));
            varo.getVaroFiles().saveConfig(teamsConfig);
        });
    }
}
