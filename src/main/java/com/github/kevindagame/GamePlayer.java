package com.github.kevindagame;

import org.bukkit.entity.Player;

public class GamePlayer {
    private final GameTeam team;
    private Player player;
    private int kills;
    private boolean isAlive;
    private final Scoreboard scoreboard;

    public GamePlayer(Player player, GameTeam team) {
        this.player = player;
        this.team = team;
        this.kills = 0;
        this.isAlive = true;
        this.scoreboard = new Scoreboard(team, this);
    }

    public Player getPlayer() {
        return player;
    }

    public GameTeam getTeam() {
        return team;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public void die() {
        isAlive = false;
    }


    public void clearScoreboard() {
        scoreboard.clearScoreBoard();
    }

    public void revive() {
        isAlive = true;
    }
}
