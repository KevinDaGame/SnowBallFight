package com.github.kevindagame.Model;

import org.bukkit.Location;

public class Team {
    final String color;
    final SpawnPoint spawnPoint;

    public Team(String color, Location l) {
        this.color = color;
        this.spawnPoint = new SpawnPoint(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getPitch(), l.getYaw(), l.getWorld().getName());
    }

    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }

    public String getColor() {
        return color;
    }
}
