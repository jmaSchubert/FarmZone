package org.Vertyx.farmZone.managers;

import org.Vertyx.farmZone.models.FarmZoneModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmZoneManager {

    private List<FarmZoneModel> activeFarmzones;
    private Map<Player, Long> playerOutofZone;

    public FarmZoneManager() {
        activeFarmzones = new ArrayList<>();
        playerOutofZone = new HashMap<>();
    }


    public boolean createFarmZone(String name, Location center, double radius) {
        FarmZoneModel newFarmZoneModel = new FarmZoneModel(name, center, radius);
        activeFarmzones.add(newFarmZoneModel);
        return true;
    }



}
