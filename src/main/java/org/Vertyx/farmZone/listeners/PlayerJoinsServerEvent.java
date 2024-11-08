package org.Vertyx.farmZone.listeners;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinsServerEvent implements Listener {
    private FarmZoneManager manager;

    public PlayerJoinsServerEvent(FarmZoneManager manager)
    {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.sendMessage("Welcome to the server!");
    }

}
