package com.github.kevindagame.Model;

import com.github.kevindagame.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameTeam {
    private final ChatColor color;
    private final Game game;
    final Location spawnLocation;
    private final GamePlayer[] players;
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

    public String getName() {
        return color.name().substring(0, 1).toUpperCase() + color.name().substring(1).toLowerCase();
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
                Bukkit.getScheduler().scheduleSyncDelayedTask(game.getMain(), () -> {
                    p.revive();
                    p.getPlayer().teleport(spawnLocation);
                    p.getPlayer().setHealth(20);
                    p.getPlayer().setFoodLevel(20);
                    if(!p.getPlayer().getInventory().contains(Material.SNOWBALL)){
                        p.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
                    }

                }, 20);
            }
        }
    }

    public Color getArmourColor() {
        switch (color) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case YELLOW:
                return Color.YELLOW;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case GRAY:
                return Color.GRAY;
            case GOLD:
                return Color.ORANGE;
        }
        return null;
    }

    public boolean removePlayer(GamePlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                players[i] = null;
                return true;
            }
        }
        return false;
    }
}
