package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.commands.SnowBallFightTabCompleter;
import com.github.kevindagame.listeners.PlayerDeath;
import com.github.kevindagame.listeners.SnowBallHit;
import com.github.kevindagame.listeners.SnowBallThrow;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SnowBallFight extends JavaPlugin {
    WorldGuard worldGuard = WorldGuard.getInstance();
    private Game game;
    private PluginConfig config;
    private ArenaHandler arenaHandler;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);
        getServer().getPluginManager().registerEvents(new SnowBallHit(this, 5), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);

        getCommand("snowballfight").setExecutor(new SnowBallFightCommand(this));
        getCommand("snowballfight").setTabCompleter(new SnowBallFightTabCompleter(this));

        File arenasFile = new File(getDataFolder(), "arenas.json");
        if (!arenasFile.exists()) saveResource(arenasFile.getName(), false);
        arenaHandler = new ArenaHandler(arenasFile);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveResource(configFile.getName(), false);
        config = new PluginConfig(configFile);


//        ArrayList<Team> teams = new ArrayList<>();
//        teams.add(new Team("GREEN", new SpawnPoint(10, 0, 10, "world")));
//        teams.add(new Team("AQUA", new SpawnPoint( 1, 10, 10,"world")));
//
//        arenas.put("arena1", new Arena("region1", teams, "world"));
//        arenas.put("arena2", new Arena("region1", teams, "world"));
//        arenas.put("arena3", new Arena("region1", teams, "world"));
//        arenas.put("arena4", new Arena("region1", teams, "world"));
//        arenas.put("arena5", new Arena("region1", teams, "world"));
//        saveArenasFile();
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
