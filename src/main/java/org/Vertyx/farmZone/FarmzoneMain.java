package org.Vertyx.farmZone;

//import org.Vertyx.farmZone.commands.FarmzoneDebugCommands;
import org.Vertyx.farmZone.commands.HomeCommand;
import org.Vertyx.farmZone.listeners.PlayerJoinsServerEvent;
import org.Vertyx.farmZone.listeners.PlayerMoveTask;
import org.Vertyx.farmZone.listeners.PlayerQuitListener;
import org.Vertyx.farmZone.managers.FarmzoneManager;
import org.Vertyx.farmZone.commands.FarmzoneCommand;
import org.Vertyx.farmZone.utils.FarmzoneTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public final class FarmzoneMain extends JavaPlugin {

    private FarmzoneManager manager;
    private File dataFile;
    private BukkitScheduler scheduler;
    BukkitTask task;

    @Override
    public void onEnable() {
        dataFile = new File(getDataFolder(), "data.yml");
        manager = FarmzoneManager.getInstance(dataFile);
        scheduler = this.getServer().getScheduler();

        // Register Commands
        this.getCommand("farmzone").setExecutor(new FarmzoneCommand(manager));
        this.getCommand("home").setExecutor(new HomeCommand(manager, this));
        this.getCommand("farmzone").setTabCompleter(new FarmzoneTabCompleter(manager));

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinsServerEvent(manager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(manager), this);
        task = scheduler.runTaskTimer(this, new PlayerMoveTask(this, manager), 0, 200);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.saveData(dataFile);
        scheduler.cancelTask(task.getTaskId());
    }
}
