package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.commands.SnowBallFightTabCompleter;
import com.github.kevindagame.listeners.PlayerDeath;
import com.github.kevindagame.listeners.PlayerLeave;
import com.github.kevindagame.listeners.SnowBallHit;
import com.github.kevindagame.listeners.SnowBallThrow;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SnowBallFight extends JavaPlugin {
    final WorldGuard worldGuard = WorldGuard.getInstance();
    private Game game;
    private PluginConfig config;
    private ArenaHandler arenaHandler;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);
        getServer().getPluginManager().registerEvents(new SnowBallHit(this, 5), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this), this);

        getCommand("snowballfight").setExecutor(new SnowBallFightCommand(this));
        getCommand("snowballfight").setTabCompleter(new SnowBallFightTabCompleter(this));

        File arenasFile = new File(getDataFolder(), "arenas.json");
        if (!arenasFile.exists()) saveResource(arenasFile.getName(), false);
        arenaHandler = new ArenaHandler(arenasFile);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveResource(configFile.getName(), false);
        config = new PluginConfig(configFile);


    }

    @Override
    public void onDisable() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        if (getGame() != null) {
            getGame().stop();
        }
        this.game = game;
    }

    public void stopGame() {
        this.game = null;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public WorldGuard getWorldGuard() {
        return worldGuard;
    }


}
