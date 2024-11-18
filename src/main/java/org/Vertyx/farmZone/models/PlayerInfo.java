package org.Vertyx.farmZone.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;

import java.util.UUID;

public class PlayerInfo {
    public UUID playerID;
    public boolean inFarmzone;
    public Location lastCoordinatesInFarmzone;
    public long exitHomezone;
    public long timeSpentInFarmzone;

    public PlayerInfo(UUID playerID, boolean inFarmzone, Location lastCoordinates, long exitHomezone, long timeSpentInFarmzone) {
        this.playerID = playerID;
        this.inFarmzone = inFarmzone;
        this.lastCoordinatesInFarmzone = lastCoordinates;
        this.exitHomezone = exitHomezone;
        this.timeSpentInFarmzone = timeSpentInFarmzone;

    }
}