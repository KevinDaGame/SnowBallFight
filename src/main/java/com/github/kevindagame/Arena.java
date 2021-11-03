package com.github.kevindagame;

import org.bukkit.Location;

public class Arena {
    public String region;
    SpawnPoint[] spawns;

    public Arena(String region, SpawnPoint[] spawns) {
        this.region = region;
        this.spawns = spawns;


    }
}
