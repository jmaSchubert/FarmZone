package org.Vertyx.farmZone.models;

import org.bukkit.Location;

public class FarmZoneModel {
    private String name;
    private Location center;
    private double radius;

    public FarmZoneModel(String name, Location center, double radius) {
        this.name = name;
        this.center = center;
        this.radius = radius;
    }

    public Location getCenter()
    {
        return center;
    }
}
