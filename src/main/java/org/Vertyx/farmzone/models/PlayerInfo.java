package org.Vertyx.farmzone.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerInfo {
    public UUID playerID;
    public boolean inFarmzone;
    public Location lastCoordinatesInFarmzone;
    public long exitHomezone;
    public long timeSpentInFarmzone;
    public Location preferredHomeLocation;
    BossBar bossBar;

    public PlayerInfo(UUID playerID, boolean inFarmzone, Location lastCoordinates, long exitHomezone, long timeSpentInFarmzone) {
        this.playerID = playerID;
        this.inFarmzone = inFarmzone;
        this.lastCoordinatesInFarmzone = lastCoordinates;
        this.exitHomezone = exitHomezone;
        this.timeSpentInFarmzone = timeSpentInFarmzone;
        this.preferredHomeLocation = null;
        this.bossBar = Bukkit.createBossBar("You're in the Farmzone!", BarColor.GREEN, BarStyle.SOLID);
    }

    public BossBar getBossBar() { return this.bossBar; }

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