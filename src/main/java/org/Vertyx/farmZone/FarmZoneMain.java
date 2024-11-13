package org.Vertyx.farmZone;

import org.Vertyx.farmZone.commands.FarmzoneDebugCommands;
import org.Vertyx.farmZone.listeners.PlayerJoinsServerEvent;
import org.Vertyx.farmZone.listeners.PlayerMoveTask;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.commands.FarmzoneCommand;
import org.Vertyx.farmZone.utils.FarmzoneTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class FarmZoneMain extends JavaPlugin {

//    private FarmZoneManager manager;

    @Override
    public void onEnable() {
        FarmZoneManager manager = FarmZoneManager.getInstance();
        BukkitScheduler scheduler = this.getServer().getScheduler();

        // Register Commands
        // TODO: create /farmzone delete
        // TODO: create /farmzone list
        // TODO: create /farmzone enable
        // TODO: create /farmzone disable
        // TODO: create /home
        this.getCommand("farmzone").setExecutor(new FarmzoneCommand(manager));
        this.getCommand("farmzone").setTabCompleter(new FarmzoneTabCompleter());
        this.getCommand("debugfz").setExecutor(new FarmzoneDebugCommands(manager));

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinsServerEvent(manager), this);
        scheduler.runTaskTimer(this, new PlayerMoveTask(this, manager), 0, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
