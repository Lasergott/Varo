package net.howtobedwars.varo.cps;

import lombok.Getter;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CPSCheck {

    private final BukkitTask bukkitTask;

    @Getter
    private final Player target;

    @Getter
    @Setter
    private int leftCPS;

    @Getter
    @Setter
    private int rightCPS;

    public CPSCheck(Varo varo, Player player, Player target) {
        this.target = target;
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("§eKlicks von " + target.getName() + ": §a" + getLeftCPS() + " §7| §c" + getRightCPS());
                reset();
            }
        }.runTaskTimer(varo, 0, 20);
    }

    public void click(ClickType clickType) {
        if(clickType == ClickType.LEFT) {
            setLeftCPS(getLeftCPS() + 1);
        } else {
            setRightCPS(getRightCPS() + 1);
        }
    }

    public void uncheck() {
        bukkitTask.cancel();
        reset();
    }

    private void reset() {
        setLeftCPS(0);
        setRightCPS(0);
    }
}
