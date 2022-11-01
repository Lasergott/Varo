package net.howtobedwars.varo.listener;

import lombok.AllArgsConstructor;
import net.howtobedwars.varo.Varo;
import net.howtobedwars.varo.config.TeamsConfig;
import net.howtobedwars.varo.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

@AllArgsConstructor
public class SignChangeListener implements Listener {

    private final Varo varo;

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        User user = varo.getVaroGame().getUserRegistry().get(player.getUniqueId());
        if (user != null) {
            if (block.getType() != Material.WALL_SIGN) {
                return;
            }
            Sign materialSign = (Sign) block.getState().getData();
            Block attachedBlock = block.getRelative(materialSign.getAttachedFace());
            if(attachedBlock instanceof DoubleChest || attachedBlock.getType() != Material.CHEST) {
                return;
            }
            if (event.getLine(0).equals(user.getTeam().getTeamTag())) {
                if (user.getTeam().getTeamChest() != null) {
                    player.sendMessage("§cIhr habt bereits eine Teamchest, weshalb ihr keine weitere erstellen könnt");
                    event.setCancelled(true);
                    return;
                }
                user.getTeam().setTeamChest(attachedBlock.getLocation());
                player.sendMessage("§aTeamchest wurde erstellt");
                updateTeam(user, attachedBlock);
            }
        }
    }

    private void updateTeam(User user, Block block) {
        Bukkit.getScheduler().runTaskAsynchronously(varo, () -> {
            TeamsConfig teamsConfig = varo.getVaroFiles().getTeamsConfig();
            teamsConfig.getTeams()
                    .stream()
                    .filter(team1 -> team1.getTeamTag().equals(user.getTeam().getTeamTag()))
                    .forEach(team1 -> team1.setTeamChest(block.getLocation()));
            varo.getVaroFiles().saveConfig(teamsConfig);
        });
    }
}
