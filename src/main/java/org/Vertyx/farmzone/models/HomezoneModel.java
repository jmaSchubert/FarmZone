package org.Vertyx.farmzone.models;

import org.bukkit.Location;

public class HomezoneModel {
    private String name;
    final private Location center;
    private double radius;
    private Location defaultHomeLocation;

    public HomezoneModel(String name, Location center, double radius) {
        this.name = name;
        this.center = center;
        this.radius = radius;
        defaultHomeLocation = center;
    }

    // check if user has preferred home else default
    public Location getPreferredHome(PlayerInfo info)
    {
        if (info == null || info.preferredHomeLocation == null)
        {
            if (defaultHomeLocation == null)
            {
                return getCenter();
            }
            return this.defaultHomeLocation;
        }
        else
        {
            return info.preferredHomeLocation;
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
