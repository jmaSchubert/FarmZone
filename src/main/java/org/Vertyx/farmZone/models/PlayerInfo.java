package org.Vertyx.farmZone.models;

import org.bukkit.Location;

public class PlayerInfo {
    public boolean inFarmzone;
    public Location lastCoordinatesInFarmzone;
    public long exitHomezone;
    public long timeSpentInFarmzone;

    public PlayerInfo(boolean inFarmzone, Location lastCoordinates, long exitHomezone, long timeSpentInFarmzone) {
        this.inFarmzone = inFarmzone;
        this.lastCoordinatesInFarmzone = lastCoordinates;
        this.exitHomezone = exitHomezone;
        this.timeSpentInFarmzone = timeSpentInFarmzone;
    }
}