package org.Vertyx.farmZone.listeners;

import org.Vertyx.farmZone.managers.FarmzoneManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinsServerEvent implements Listener {
    private final FarmzoneManager manager;

    public PlayerJoinsServerEvent(FarmzoneManager manager)
    {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (manager.locationInFarmzone(player.getLocation()))
        {
            manager.getPlayerInfo(player).showBossbar(player);
            manager.setExitHomezoneOnJoin(player);
            player.sendMessage("[!] You're still in the Farmzone!");
        }
    }

}
