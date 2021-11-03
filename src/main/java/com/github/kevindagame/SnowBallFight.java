package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.listeners.SnowBallThrow;
import org.bukkit.plugin.java.JavaPlugin;

public class SnowBallFight extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);

        getCommand("snowballfight").setExecutor(new SnowBallFightCommand());
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
