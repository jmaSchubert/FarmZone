package org.Vertyx.farmZone.models;

import org.bukkit.Location;

public class HomeZoneModel {
    private String name;
    final private Location center;
    private double radius;
    private Location defaultHomeLocation;

    public HomeZoneModel(String name, Location center, double radius) {
        this.name = name;
        this.center = center;
        this.radius = radius;
        defaultHomeLocation = center;
    }

    // check if user has preferred home else default
    public Location getPreferredHome(PlayerInfo info)
    {
        if (info == null || info.preferredHomeLocation == null )
        {
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
