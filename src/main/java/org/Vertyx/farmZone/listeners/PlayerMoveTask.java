package org.Vertyx.farmZone.listeners;

import org.Vertyx.farmZone.FarmZoneMain;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.models.PlayerInfo;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayerMoveTask implements Runnable{
    final private FarmZoneMain plugin;
    final private FarmZoneManager manager;

    public PlayerMoveTask(FarmZoneMain plugin, FarmZoneManager manager)
    {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void updatePlayerInfo(Player player) {

        PlayerInfo playerInfo = manager.getPlayerInfo(player);
        if (playerInfo == null) {
            playerInfo = new PlayerInfo(false, player.getLocation(), 0, 0);
            manager.setPlayerInfo(player, playerInfo);
        }

        boolean currentlyInFarmzone = manager.playerInFarmzone(player);
        Location playerLocation = player.getLocation();
        long currentTime = System.currentTimeMillis();

        if (currentlyInFarmzone) {
            // player enters the farmzone
            if (!playerInfo.inFarmzone) {
                playerInfo.inFarmzone = true;
                playerInfo.exitHomezone = currentTime;
                player.sendMessage("You've entered the Farmzone!");
            }

            playerInfo.lastCoordinatesInFarmzone = playerLocation;
            long timeSpentInCurrentSession = currentTime - playerInfo.exitHomezone;

            if (playerInfo.timeSpentInFarmzone + timeSpentInCurrentSession > FarmZoneManager.MAX_FARMZONE_TIME) {
                // daily time exceeded
                player.teleport(manager.activeHomezones.getFirst().getCenter());
                player.sendMessage("Daily farmzone time exceeded!");

            } else {
                // player has leftover time

                // display leftovertime on critical time stamps (30min, 15min, 5min, 1min, 5s, 4s, 3s, 2s, 1s)
                player.sendMessage("Daily farmzone time remaining: " + TimeUnit.MILLISECONDS.toSeconds(FarmZoneManager.MAX_FARMZONE_TIME - (playerInfo.timeSpentInFarmzone + timeSpentInCurrentSession)) + "s");
            }
        } else {
            // player returnes to homezone
            if (playerInfo.inFarmzone) {
                playerInfo.inFarmzone = false;
                long timeSpentInCurrentSession = currentTime - playerInfo.exitHomezone;
                playerInfo.timeSpentInFarmzone += timeSpentInCurrentSession;
                player.sendMessage("Returned to homezone");

            } else {
                // player remains in homezone
//                player.sendMessage("Still in homezone");
            }
        }

        // Aktualisiere die PlayerInfo im Manager
        manager.setPlayerInfo(player, playerInfo);
    }


    @Override
    public void run() {
        if (!manager.activeHomezones.isEmpty())
        {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                updatePlayerInfo(player);
            }
        }
    }
}
