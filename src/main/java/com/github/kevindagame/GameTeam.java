package com.github.kevindagame;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameTeam {
    private final ChatColor color;
    private final Game game;
    Location spawnLocation;
    private GamePlayer[] players;
    private int wins;
    private int losses;

    public GameTeam(Game game, Location spawnLocation, ChatColor color, int maxPlayers) {
        this.color = color;
        this.game = game;
        this.spawnLocation = spawnLocation;
        players = new GamePlayer[maxPlayers];
        wins = 0;
        losses = 0;
    }

    public boolean addPlayer(Player p) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                GamePlayer gamePlayer = new GamePlayer(p, this);
                players[i] = gamePlayer;
                gamePlayer.getPlayer().teleport(spawnLocation);
                return true;
            }
        }
        return false;

    }

    public void win() {
        wins++;
    }

    public void lose() {
        losses++;
    }

    public GamePlayer[] getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        int count = 0;
        for (GamePlayer p : getPlayers()) {
            if (p != null) count++;
        }
        return count;
    }

    public int getAlivePlayers() {
        int counter = 0;
        for (GamePlayer p : getPlayers()) {
            if (p != null && p.isAlive()) {
                counter++;
            }
        }
        return counter;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName(ChatColor color) {
//        switch (color){
//            case WHITE:
//                return "white";
//            case BLACK:
//            case RED:
//            case AQUA:
//            case BLUE:
//            case DARK_AQUA:
//            case GOLD:
//            case GRAY:
//            case GREEN:
//            case YELLOW:
//            case DARK_RED:
//            case DARK_BLUE:
//            case DARK_GRAY:
//            case DARK_GREEN:
//            case DARK_PURPLE:
//            case LIGHT_PURPLE:
        return color.name().toLowerCase();
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public Game getGame() {
        return game;
    }

    public void removeScoreboard() {
        for (GamePlayer p : getPlayers()) {
            if (p != null) {
                p.clearScoreboard();
            }
        }
    }

    public void revive() {
        for (GamePlayer p : getPlayers()) {
            if (p != null) {
                p.revive();
                p.getPlayer().teleport(spawnLocation);
            }
        }
    }
}
