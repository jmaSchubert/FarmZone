package org.Vertyx.farmzone.models;

import org.Vertyx.farmzone.FarmzoneMain;
import org.Vertyx.farmzone.managers.FarmzoneManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomezoneModel {
    private String name;
    final private Location center;
    private double radius;
    private Location defaultHomeLocation;
    private FarmzoneManager manager;

    public HomezoneModel(String name, Location center, double radius, FarmzoneManager manager) {
        this.name = name;
        this.center = center;
        this.radius = radius;
        this.manager = manager;
        defaultHomeLocation = center;
    }

    // check if user has preferred home else default
    public Location getPreferredHome(Player player)
    {
        Location spawnLocation = player.getPotentialBedLocation();
        if (spawnLocation == null || manager.locationInFarmzone(spawnLocation))
        {
            if (defaultHomeLocation == null)
            {
                return getCenter();
            }
            return this.defaultHomeLocation;
        }
        else
        {
            return spawnLocation;
        }
    }

    public void setDefaultHomeLocation(Location location) { defaultHomeLocation = location; }

    public Location getDefaultHomeLocation() { return defaultHomeLocation; }

    public Location getCenter()
    {
        return center;
    }

    public double getRadius()
    {
        return radius;
    }

    public String getName()
    {
        return name;
    }
}
