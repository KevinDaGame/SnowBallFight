package com.github.kevindagame;

import java.util.ArrayList;

public class Arena {
    String region;
    ArrayList<Team> teams;
    String world;

    public String getRegion() {
        return region;
    }

    public ArrayList<Team> getSpawns() {
        return teams;
    }

    public String getWorld() {
        return world;
    }

    public Arena(String region, String world) {
        this.region = region;
        this.teams = new ArrayList<>();
        this.world = world;

    }

    public Arena(String region, ArrayList<Team> teams, String world) {
        this.region = region;
        this.teams = teams;
        this.world = world;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }
}
