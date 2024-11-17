package org.Vertyx.farmZone.listeners;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.models.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final FarmZoneManager manager;

    public PlayerQuitListener(FarmZoneManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerInfo playerInfo = manager.getPlayerInfo(event.getPlayer());
        if (playerInfo != null && playerInfo.inFarmzone) {
            long currentTime = System.currentTimeMillis();
            long timeSpentInCurrentSession = currentTime - playerInfo.exitHomezone;
            playerInfo.timeSpentInFarmzone += timeSpentInCurrentSession;
            playerInfo.exitHomezone = currentTime; // Update exitHomezone to current time
            manager.setPlayerInfo(event.getPlayer().getUniqueId(), playerInfo);
        }
    }
}