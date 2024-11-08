package org.Vertyx.farmZone;

import org.Vertyx.farmZone.listeners.PlayerJoinsServerEvent;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.commands.FarmzoneCommand;
import org.Vertyx.farmZone.utils.FarmzoneTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarmZone extends JavaPlugin {

    private FarmZoneManager manager;

    @Override
    public void onEnable() {
        manager = new FarmZoneManager();

        this.getCommand("farmzone").setExecutor(new FarmzoneCommand(manager));
        this.getCommand("farmzone").setTabCompleter(new FarmzoneTabCompleter());

        getServer().getPluginManager().registerEvents(new PlayerJoinsServerEvent(manager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
