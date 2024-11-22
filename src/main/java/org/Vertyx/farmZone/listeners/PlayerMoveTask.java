package org.Vertyx.farmZone.listeners;

import org.Vertyx.farmZone.FarmZoneMain;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.models.PlayerInfo;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.time.LocalDate;

public class PlayerMoveTask implements Runnable{
    final private FarmZoneMain plugin;
    final private FarmZoneManager manager;
    final private BossBar bossBar;

    public PlayerMoveTask(FarmZoneMain plugin, FarmZoneManager manager)
    {
        this.plugin = plugin;
        this.manager = manager;
        this.bossBar = manager.getBossBar();
    }

    public void updatePlayerInfo(Player player) {

        PlayerInfo playerInfo = manager.getPlayerInfo(player);
        if (playerInfo == null) {
            playerInfo = new PlayerInfo(player.getUniqueId(), false, player.getLocation(), 0, 0);
            manager.setPlayerInfo(player.getUniqueId(), playerInfo);
        }

        if (LocalDate.now().isAfter(manager.getLastFarmzoneUpdate())) {
            manager.resetFarmzoneTimer();
            manager.setLastFarmzoneUpdate(LocalDate.now());
            player.sendMessage("[!] Timeout has been reset!");
        }

        Location playerLocation = player.getLocation();
        boolean currentlyInFarmzone = manager.locationInFarmzone(playerLocation);
        long currentTime = System.currentTimeMillis();

        if (currentlyInFarmzone) {
            // player enters the farmzone
            if (!playerInfo.inFarmzone) {
                playerInfo.inFarmzone = true;
                playerInfo.exitHomezone = currentTime;
                bossBar.setTitle("You're in the Farmzone!");
                bossBar.addPlayer(player);
            }

            playerInfo.lastCoordinatesInFarmzone = playerLocation;
            long timeSpentInCurrentSession = currentTime - playerInfo.exitHomezone;

            if (playerInfo.timeSpentInFarmzone + timeSpentInCurrentSession > FarmZoneManager.MAX_FARMZONE_TIME) {
                // daily time exceeded
                player.teleport(manager.getFirstHomezone().getPreferredHome(playerInfo));
                player.sendMessage("[!] Daily farmzone time exceeded!");

            } else {
                // player has leftover time
                long leftoverTime = FarmZoneManager.MAX_FARMZONE_TIME - (playerInfo.timeSpentInFarmzone + timeSpentInCurrentSession);
                bossBar.setProgress((double)leftoverTime/FarmZoneManager.MAX_FARMZONE_TIME);
                // display leftovertime on critical time stamps (30min, 15min, 5min, 1min, 5s, 4s, 3s, 2s, 1s)
                if (leftoverTime <= 15 * 60 * 1000 && leftoverTime > 14 * 60 * 1000) { player.sendMessage("[!] Remaining time: 15min"); }
                else if (leftoverTime <= 1 * 60 * 1000 && leftoverTime > 59 * 1000) { player.sendMessage("[!] Remaining time: 1min"); }
                else if (leftoverTime <= 10 * 1000 && leftoverTime > 9 * 1000) { player.sendMessage("[!] Remaining time: 10sec"); }
            }
        } else {
            // player returnes to homezone
            if (playerInfo.inFarmzone) {
                playerInfo.inFarmzone = false;
                long timeSpentInCurrentSession = currentTime - playerInfo.exitHomezone;
                playerInfo.timeSpentInFarmzone += timeSpentInCurrentSession;
                player.sendMessage("[!] Returned to homezone");
                bossBar.removePlayer(player);

            } else {
                // player remains in homezone
//                player.sendMessage("Still in homezone");
            }
        }

        // Aktualisiere die PlayerInfo im Manager
        manager.setPlayerInfo(player.getUniqueId(), playerInfo);
    }


    @Override
    public void run() {
        if (!manager.getActiveHomezones().isEmpty())
        {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                updatePlayerInfo(player);
            }
        }

    }
}
