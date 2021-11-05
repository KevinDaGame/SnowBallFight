package com.github.kevindagame;

import org.bukkit.entity.Player;

public class GamePlayer {
    private Player player;
    private int kills;

    public GamePlayer(Player player) {
        this.player = player;
        this.kills = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void kill(){
        kills++;
    }

    
}
