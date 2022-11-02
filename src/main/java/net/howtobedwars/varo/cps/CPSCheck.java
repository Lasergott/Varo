package net.howtobedwars.varo.cps;

import lombok.Getter;
import lombok.Setter;
import net.howtobedwars.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CPSCheck {

    @Getter
    private final BukkitTask cpsCheckTask;

    @Getter
    private final BukkitTask cpsLimitCheckTask;

    @Getter
    private final Player player;

    @Getter
    @Setter
    private Player checker;

    @Getter
    @Setter
    private int leftCPS;

    @Getter
    @Setter
    private int rightCPS;

    private CPSCheck(Varo varo, Player player) {
        this.player = player;
        this.cpsCheckTask = runCpsCheck(varo);
        this.cpsLimitCheckTask = runCpsLimitCheck(varo);
    }

    public static CPSCheck create(Varo varo, Player player) {
        return new CPSCheck(varo, player);
    }

    public void click(ClickType clickType) {
        if (clickType == ClickType.LEFT) {
            setLeftCPS(getLeftCPS() + 1);
        } else {
            setRightCPS(getRightCPS() + 1);
        }
    }

    public void uncheck() {
        cpsCheckTask.cancel();
        cpsLimitCheckTask.cancel();
        setChecker(null);
        reset();
    }

    private void reset() {
        setLeftCPS(0);
        setRightCPS(0);
    }

    private BukkitTask runCpsCheck(Varo varo) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }
                if (checker != null && checker.isOnline()) {
                    checker.sendMessage("§eKlicks von " + player.getName() + ": §a" + getLeftCPS() + " §7| §c" + getRightCPS());
                }
                reset();
            }
        }.runTaskTimer(varo, 0, 20);
    }

    private BukkitTask runCpsLimitCheck(Varo varo) {
        return new BukkitRunnable() {
            final int CPS_LIMIT = varo.getVaroGame().CPS_LIMIT;
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }
                if (getLeftCPS() > CPS_LIMIT || getRightCPS() > CPS_LIMIT) {
                    Bukkit.getOnlinePlayers()
                            .stream()
                            .filter(bypass -> bypass.hasPermission("howtobedwars.varo.cps.notify"))
                            .forEach(bypass -> bypass.sendMessage("§e§lDer Spieler §c§l" + player.getName() + " §e§lhatte mehr als " + CPS_LIMIT + " CPS"));
                }
            }
        }.runTaskTimer(varo, 0, 20);
    }
}
