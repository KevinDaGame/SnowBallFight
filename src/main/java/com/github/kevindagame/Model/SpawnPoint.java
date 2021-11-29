package com.github.kevindagame.Model;

public class SpawnPoint {
    private int x;
    private int y;
    private int z;
    private String world;

    public SpawnPoint(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z + ", World: " + world;
    }
}
