package org.Vertyx.farmZone.managers;

import org.Vertyx.farmZone.models.HomeZoneModel;
import org.Vertyx.farmZone.models.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FarmZoneManager {

    // Data structure to store player information
    private static FarmZoneManager instance;
    public static final long MAX_FARMZONE_TIME = 60 * 1000; // 1 minute in milliseconds
    public List<HomeZoneModel> activeHomezones;
    private Map<Player, PlayerInfo> playerInfoMap;
    private BossBar bossBar = Bukkit.createBossBar(" ", BarColor.GREEN, BarStyle.SOLID);

    private FarmZoneManager() {
        activeHomezones = new ArrayList<>();
        playerInfoMap = new HashMap<>();
    }

    public static synchronized FarmZoneManager getInstance() {
        if (instance == null) {
            instance = new FarmZoneManager();
        }
        return instance;
    }

    // Getter and Setter for private datastructures
    public PlayerInfo getPlayerInfo(Player player)
    {
        return playerInfoMap.get(player);
    }

    public boolean setPlayerInfo(Player player, PlayerInfo playerInfo)
    {
        try
        {
            if (playerInfoMap.containsKey(player)) {
                playerInfoMap.replace(player, playerInfo);
            } else {
                playerInfoMap.put(player, playerInfo);
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public BossBar getBossBar()
    {
        return this.bossBar;
    }

    // Methods for handling player logic
    public boolean playerInFarmzone(Player player)
    {
        if (activeHomezones.isEmpty())
        {
            player.sendMessage("ERROR: No active farmzones");
            return false;
        }

        Location playerLocation = player.getLocation();

        for (HomeZoneModel homezone : activeHomezones) {
            if (playerLocation.distance(homezone.getCenter()) > homezone.getRadius()) {
                return true;
            }
        }
        return false;
    }

    public boolean playerFarmzoneTimeExceeded(Player player)
    {
        PlayerInfo playerInfo = playerInfoMap.get(player);
        return playerInfo != null && playerInfo.timeSpentInFarmzone > MAX_FARMZONE_TIME;
    }


    // Methods for commands to execute
    public boolean createFarmZone(String name, Location center, double radius) {
        HomeZoneModel newHomeZoneModel = new HomeZoneModel(name, center, radius);
        activeHomezones.add(newHomeZoneModel);
        return true;
    }

    public void setBossBarColor(BarColor color) {
        bossBar.setColor(color);
    }

    public void showBossbar(Player player)
    {
        bossBar.addPlayer(player);
    }

    public void hideBossbar(Player player)
    {
        bossBar.removePlayer(player);
    }
}
