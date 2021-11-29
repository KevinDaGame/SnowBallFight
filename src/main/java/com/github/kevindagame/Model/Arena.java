package com.github.kevindagame.Model;

import java.util.ArrayList;

public class Arena {
    private String region;
    private ArrayList<Team> teams;
    private String world;

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

    public String getRegion() {
        return region;
    }

    public ArrayList<Team> getSpawns() {
        return teams;
    }

    public String getWorld() {
        return world;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }
}
