package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.listeners.SnowBallHit;
import com.github.kevindagame.listeners.SnowBallThrow;
import org.bukkit.plugin.java.JavaPlugin;

public class SnowBallFight extends JavaPlugin {
    private Game game;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);
        getServer().getPluginManager().registerEvents(new SnowBallHit(), this);
        getCommand("snowballfight").setExecutor(new SnowBallFightCommand(this));
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }


    public Game getGame(){
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }
}
