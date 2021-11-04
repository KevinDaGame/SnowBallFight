package com.github.kevindagame;

import java.util.ArrayList;

public class Arena {
    String region;
    ArrayList<SpawnPoint> spawns;
    String world;

    public String getRegion() {
        return region;
    }

    public ArrayList<SpawnPoint> getSpawns() {
        return spawns;
    }

    public String getWorld() {
        return world;
    }

    public Arena(String region, String world) {
        this.region = region;
        this.spawns = new ArrayList<>();
        this.world = world;


    }
}
