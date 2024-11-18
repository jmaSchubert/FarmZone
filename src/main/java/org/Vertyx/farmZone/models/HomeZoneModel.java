package org.Vertyx.farmZone.models;

import jdk.jfr.Percentage;
import org.bukkit.Location;

public class HomeZoneModel {
    private String name;
    final private Location center;
    private double radius;

    public HomeZoneModel(String name, Location center, double radius) {
        this.name = name;
        this.center = center;
        this.radius = radius;
    }

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

//    @Override
//    public String toString() {
//        return "HomeZoneModel{" + "name='" + name + '\'' + ", center=" + center + ", radius=" + radius + '}';
//    }
}
