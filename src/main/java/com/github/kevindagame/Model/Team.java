package com.github.kevindagame.Model;

public class Team {
    final String color;
    final SpawnPoint spawnPoint;

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
