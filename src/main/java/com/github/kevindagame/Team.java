package com.github.kevindagame;

import org.bukkit.ChatColor;

public class Team {
    String color;
    SpawnPoint spawnPoint;

    public Team(String color, SpawnPoint spawnPoint) {
        this.color = color;
        this.spawnPoint = spawnPoint;
    }

    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }

    public String getColor() {
        return color;
    }
}
