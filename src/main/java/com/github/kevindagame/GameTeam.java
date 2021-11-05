package com.github.kevindagame;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GameTeam {
    private final Color color;
    private GamePlayer[] players;
    private int wins;
    private int losses;

    public GameTeam(Color color, int maxPlayers) {
        this.color = color;
        players = new GamePlayer[maxPlayers];
        wins = 0;
        losses = 0;
    }

    public boolean addPlayer(Player p) {
        for (int i = 0; i < players.length; i++){
            if(players[i] == null){
            GamePlayer gamePlayer = new GamePlayer(p);
            players[i] = gamePlayer;
            return true;
            }
        }
        return false;

    }

    public void win(){
        wins++;
    }

    public void lose(){
        losses++;
    }

    public GamePlayer[] getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        int count = 0;
        for (GamePlayer p: getPlayers()) {
            if(p != null) count++;
        }
        return count;
    }

    public Color getColor() {
        return color;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}
