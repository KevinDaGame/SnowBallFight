package com.github.kevindagame.Model;

public class SpawnPoint {
    private final int x;
    private final int y;
    private final int z;
    private final float pitch;
    private final float yaw;
    private final String world;

    public SpawnPoint(int x, int y, int z, float pitch, float yaw, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public String getWorld() {
        return world;
    }

    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z + ", World: " + world;
    }
}
