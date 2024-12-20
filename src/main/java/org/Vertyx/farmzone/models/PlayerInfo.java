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
    BossBar bossBar;

    public boolean notified15Min;
    public boolean notified1Min;
    public boolean notified10Sec;

    public PlayerInfo(UUID playerID, boolean inFarmzone, Location lastCoordinates, long exitHomezone, long timeSpentInFarmzone) {
        this.playerID = playerID;
        this.inFarmzone = inFarmzone;
        this.lastCoordinatesInFarmzone = lastCoordinates;
        this.exitHomezone = exitHomezone;
        this.timeSpentInFarmzone = timeSpentInFarmzone;
        this.bossBar = Bukkit.createBossBar("You're in the Farmzone!", BarColor.GREEN, BarStyle.SOLID);
        notified15Min = false;
        notified1Min = false;
        notified10Sec = false;
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