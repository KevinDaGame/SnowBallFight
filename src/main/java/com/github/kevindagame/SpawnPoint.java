package com.github.kevindagame;

public class SpawnPoint {
    int x;
    int y;
    int z;
    String world;

    public SpawnPoint(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z + ", World: " + world;
    }
}
