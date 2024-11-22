package org.Vertyx.farmZone.managers;

import org.Vertyx.farmZone.models.HomeZoneModel;
import org.Vertyx.farmZone.models.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDate;
import java.util.*;


public class FarmZoneManager {

    // Data structure to store player information
    private static FarmZoneManager instance;
    private String firstHomezoneName;
    private Map<UUID, PlayerInfo> playerInfoMap;
    private BossBar bossBar = Bukkit.createBossBar("You're in the Farmzone!", BarColor.GREEN, BarStyle.SOLID);
    private Map<String, HomeZoneModel> activeHomezones;
    private LocalDate lastFarmzoneUpdate;
    public static long MAX_FARMZONE_TIME = 60 * 60 * 1000; // 1 hour in milliseconds


    private FarmZoneManager(File dataFile) {
        activeHomezones = new HashMap<>();
        playerInfoMap = new HashMap<>();
        loadData(dataFile);
    }

    public static synchronized FarmZoneManager getInstance(File dataFile) {
        if (instance == null) {
            instance = new FarmZoneManager(dataFile);
        }
        return instance;
    }

    // Getter and Setter for private datastructures
    public PlayerInfo getPlayerInfo(Player player)
    {
        return playerInfoMap.get(player.getUniqueId());
    }

    public void setPlayerInfo(UUID playerID, PlayerInfo playerInfo)
    {
        try
        {
            if (playerInfoMap.containsKey(playerID)) {
                playerInfoMap.replace(playerID, playerInfo);
            } else {
                playerInfoMap.put(playerID, playerInfo);
            }
        }
        catch (Exception e) {
            System.out.println("SetPlayer error: " + e.getMessage());
        }

    }

    public double getDistance(Player player)
    {
        HomeZoneModel homezone = getFirstHomezone();
        Location playerLocation = player.getLocation();
        Location homeLocation = homezone.getCenter();
        return Math.sqrt(Math.pow(playerLocation.getX() - homeLocation.getX(), 2) + Math.pow(playerLocation.getZ() - homeLocation.getZ(), 2));
    }

    public LocalDate getLastFarmzoneUpdate() { return this.lastFarmzoneUpdate; }

    public void setLastFarmzoneUpdate(LocalDate date) { lastFarmzoneUpdate = date; }

    // Methods for handling player logic
    public boolean locationInFarmzone(Location location)
    {
        if (activeHomezones.isEmpty())
        {
            return false;
        }

        HomeZoneModel homezone = getFirstHomezone();

        if (homezone != null) {
            double distance = Math.sqrt(Math.pow(location.getX() - homezone.getCenter().getX(), 2) + Math.pow(location.getZ() - homezone.getCenter().getZ(), 2));
            return distance > homezone.getRadius();
        }

        return false;
    }

    public HomeZoneModel getFirstHomezone()
    {
        return activeHomezones.get(firstHomezoneName);
    }

    // Methods for commands to execute
    public void createFarmZone(String name, Location center, double radius) {
        if (activeHomezones.isEmpty())
        {
            firstHomezoneName = name;
        }

        HomeZoneModel newHomeZoneModel = new HomeZoneModel(name, center, radius);
        activeHomezones.put(name, newHomeZoneModel);
    }

    public Map<String, HomeZoneModel> getActiveHomezones() { return activeHomezones; }

    public boolean deleteFarmzone(String name)
    {
        if (activeHomezones.isEmpty() || activeHomezones.remove(name) == null)
        {
            return false;
        } else
        {
            activeHomezones.remove(name);
        }

        return true;
    }

    public BossBar getBossBar()
    {
        return this.bossBar;
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

    public void resetFarmzoneTimer()
    {
        for (PlayerInfo info : playerInfoMap.values())
        {
            info.timeSpentInFarmzone = 0;
        }
    }

    public void setExitHomezoneOnJoin(Player player) { getPlayerInfo(player).exitHomezone = System.currentTimeMillis(); }

    // store game data
    public void saveData(File file) {
        YamlConfiguration config = new YamlConfiguration();

        // save FarmzoneTimeout
        config.set("farmzoneTimeoutLong", MAX_FARMZONE_TIME);
        config.set("lastFarmzoneUpdate", lastFarmzoneUpdate.toString());

        // save farmzone
        // TODO save players set bossbar color
        HomeZoneModel homezone = getFirstHomezone();
        if (!activeHomezones.isEmpty()) {
            config.set("farmzones." + homezone.getName() + ".name", homezone.getName());
            config.set("farmzones." + homezone.getName() + ".centerCoords", homezone.getCenter());
            config.set("farmzones." + homezone.getName() + ".defaultHome", homezone.getDefaultHomeLocation());
            config.set("farmzones." + homezone.getName() + ".radius", homezone.getRadius());
        }

        // save playerInfos
        for (Map.Entry<UUID, PlayerInfo> entry : playerInfoMap.entrySet()) {
            UUID playerID = entry.getKey();
            PlayerInfo info = entry.getValue();
            config.set("players." + playerID + ".inFarmzone", info.inFarmzone);
            config.set("players." + playerID + ".lastCoordinatesInFarmzone", info.lastCoordinatesInFarmzone);
            config.set("players." + playerID + ".timeSpentInFarmzone", info.timeSpentInFarmzone);
            config.set("players." + playerID + ".exitHomezone", info.exitHomezone);
            config.set("players." + playerID + ".preferredHome", info.preferredHomeLocation);
        }
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadData(File file) {
        if (!file.exists()) {
            return;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // get FarmzoneTimout constant
        try {
            MAX_FARMZONE_TIME = config.getLong("farmzoneTimeoutLong");
            lastFarmzoneUpdate = LocalDate.parse(config.get("lastFarmzoneUpdate").toString());
        }
        catch (Exception e)
        {
            MAX_FARMZONE_TIME = 90 * 60 * 1000; // default: 90 minutes
            lastFarmzoneUpdate = LocalDate.now();
        }

        // get farmzones
        try {
            String farmzoneKey = config.getConfigurationSection("farmzones").getKeys(false).iterator().next();
            HomeZoneModel homezone = new HomeZoneModel(
                    config.get("farmzones." + farmzoneKey + ".name").toString(),
                    (Location) config.get("farmzones." + farmzoneKey + ".centerCoords"),
                    (Double) config.get("farmzones." + farmzoneKey + ".radius")
            );

            homezone.setDefaultHomeLocation( (Location) config.get("farmzones." + farmzoneKey + ".defaultHome"));
            activeHomezones.put(homezone.getName(), homezone);
            firstHomezoneName = homezone.getName();
        } catch (Exception e) {
            System.out.println("Could not recreate Homezone from File!");
        }


        // get playerInfos
        int i = 0;
        for (String key : config.getConfigurationSection("players").getKeys(false)) {
            try {
                PlayerInfo info = new PlayerInfo(
                        UUID.fromString(key),
                        config.getBoolean("players." + key + ".inFarmzone"),
                        (Location) config.get("players." + key + ".lastCoordinatesInFarmzone"),
                        config.getLong("players." + key + ".exitHomezone"),
                        config.getLong("players." + key + ".timeSpentInFarmzone")
                );
                info.preferredHomeLocation = (Location) config.get("player." + key + ".preferredHome");
                setPlayerInfo(info.playerID, info);
            } catch (Exception e)
            {
                System.out.println("Can not create PlayerInfo of player [ " + ++i + " ]");
            }
        }
    }
}
