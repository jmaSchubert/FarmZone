package org.Vertyx.farmzone;

//import org.Vertyx.farmZone.commands.FarmzoneDebugCommands;
import org.Vertyx.farmzone.commands.HomeCommand;
import org.Vertyx.farmzone.listeners.PlayerJoinsServerEvent;
import org.Vertyx.farmzone.listeners.PlayerMoveTask;
import org.Vertyx.farmzone.listeners.PlayerQuitListener;
import org.Vertyx.farmzone.managers.FarmzoneManager;
import org.Vertyx.farmzone.commands.FarmzoneCommand;
import org.Vertyx.farmzone.utils.FarmzoneTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public final class FarmzoneMain extends JavaPlugin {

    private FarmzoneManager manager;
    public static File playerDataFile;
    private BukkitScheduler scheduler;
    BukkitTask task;

    @Override
    public void onEnable() {
        playerDataFile = new File(getDataFolder(), "data.yml");
        manager = FarmzoneManager.getInstance(playerDataFile);
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
        if(manager.saveData(playerDataFile))
        {
            this.getLogger().info("Saved successfully!");
        }
        else
        {
            this.getLogger().info("Saving failed!");
        }
        scheduler.cancelTask(task.getTaskId());
    }
}
