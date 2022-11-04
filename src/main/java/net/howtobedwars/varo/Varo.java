package net.howtobedwars.varo;

import net.howtobedwars.varo.command.*;
import net.howtobedwars.varo.game.VaroGame;
import net.howtobedwars.varo.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Varo extends JavaPlugin {

    private VaroFiles varoFiles;
    private VaroGame varoGame;

    @Override
    public void onEnable() {
        this.varoFiles = new VaroFiles(this);
        this.varoGame = new VaroGame(this);
        registerEvents();
        registerCommands();
        getLogger().info("Varo has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Varo has been disabled.");
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerLoginListener(this), this);
        pluginManager.registerEvents(new UserTimeOverListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new SignChangeListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new EntityDamageListener(this), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);
    }

    private void registerCommands() {
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("resetTimeOver").setExecutor(new ResetTimeOverCommand(this));
        getCommand("resetTime").setExecutor(new ResetTimeCommand(this));
        getCommand("addSpawn").setExecutor(new AddSpawnCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("teamchat").setExecutor(new TeamChatCommand(this));
        getCommand("revive").setExecutor(new ReviveCommand(this));
        getCommand("cps").setExecutor(new CPSCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
    }

    public VaroFiles getVaroFiles() {
        return varoFiles;
    }

    public VaroGame getVaroGame() {
        return varoGame;
    }
}
