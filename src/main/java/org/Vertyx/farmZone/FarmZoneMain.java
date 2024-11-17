package org.Vertyx.farmZone;

//import org.Vertyx.farmZone.commands.FarmzoneDebugCommands;
import org.Vertyx.farmZone.commands.HomeCommand;
import org.Vertyx.farmZone.listeners.PlayerJoinsServerEvent;
import org.Vertyx.farmZone.listeners.PlayerMoveTask;
import org.Vertyx.farmZone.listeners.PlayerQuitListener;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.commands.FarmzoneCommand;
import org.Vertyx.farmZone.utils.FarmzoneTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

public final class FarmZoneMain extends JavaPlugin {

    private FarmZoneManager manager;
    private File dataFile;

    @Override
    public void onEnable() {
        manager = FarmZoneManager.getInstance();
        BukkitScheduler scheduler = this.getServer().getScheduler();
        dataFile = new File(getDataFolder(), "data.yml");
        manager.loadData(dataFile);

        // Register Commands
        // TODO: create /farmzone delete
        // TODO: create /farmzone list
        // TODO: create /farmzone enable
        // TODO: create /farmzone disable
        // TODO: create /home
        this.getCommand("farmzone").setExecutor(new FarmzoneCommand(manager));
        this.getCommand("home").setExecutor(new HomeCommand(manager));
        this.getCommand("farmzone").setTabCompleter(new FarmzoneTabCompleter(manager));
//        this.getCommand("debugfz").setExecutor(new FarmzoneDebugCommands(manager));

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinsServerEvent(manager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(manager), this);
        scheduler.runTaskTimer(this, new PlayerMoveTask(this, manager), 0, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.saveData(dataFile);
    }
}
