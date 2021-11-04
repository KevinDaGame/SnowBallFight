package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.commands.SnowBallFightTabCompleter;
import com.github.kevindagame.listeners.SnowBallHit;
import com.github.kevindagame.listeners.SnowBallThrow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SnowBallFight extends JavaPlugin {
    private Game game;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HashMap<String, Arena> arenas;
    private File arenasFile;
    private File configFile;
    private FileConfiguration config;
    WorldGuard worldGuard = WorldGuard.getInstance();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);
        getServer().getPluginManager().registerEvents(new SnowBallHit(this, 5), this);
        getCommand("snowballfight").setExecutor(new SnowBallFightCommand(this));
        getCommand("snowballfight").setTabCompleter(new SnowBallFightTabCompleter(this));

        arenasFile = new File(getDataFolder(), "arenas.json");
        configFile = new File(getDataFolder(), "config.yml");

        if (!arenasFile.exists()) saveResource(arenasFile.getName(), false);
        if (!configFile.exists()) saveResource(configFile.getName(), false);

        config = new YamlConfiguration();
        try{
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            Type type = new TypeToken<HashMap<String, Arena>>(){}.getType();
            arenas = gson.fromJson(new FileReader(arenasFile), type);
            if(arenas == null) arenas = new HashMap<>();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find Arena's file. Stopping plugin");
            Bukkit.getPluginManager().disablePlugin(this);
        }
//        ArrayList<SpawnPoint> spawns = new ArrayList<>();
//        spawns.add(new SpawnPoint(10, 0, 10, "world"));
//        spawns.add(new SpawnPoint( 1, 10, 10,"world"));
//        arenas.put("arena1", new Arena("region1", spawns));
//        arenas.put("arena2", new Arena("region1", spawns));
//        arenas.put("arena3", new Arena("region1", spawns));
//        arenas.put("arena4", new Arena("region1", spawns));
//        arenas.put("arena5", new Arena("region1", spawns));
    }
    @Override
    public void onDisable() {
    }

    private void updateArenasFile(){
        String json = gson.toJson(arenas);
        arenasFile.delete();
        try {
            Files.write(arenasFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE); // java.nio.Files
        } catch (IOException e) {
            System.out.println("Snowballfight: Error on saving arenas!!!!!");
        }
    }

    public boolean addArena(String name, World world, String region){
        Arena arena = new Arena(region, world.getName());
        arenas.put(name, arena);
        updateArenasFile();
        return true;
    }
    public Game getGame(){
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void stopGame() {
        this.game = null;
    }

    public int getDefaultRounds() {
        return config.getInt("default amount of rounds");
    }

    public int getDefaultTimePerRound() {
        return config.getInt("default time per round");
    }

    public int getDefaultTimeBetweenRound() {
        return config.getInt("default time between round");
    }

    public HashMap<String, Arena> getArenas() {
        return arenas;
    }

    public WorldGuard getWorldGuard(){
        return worldGuard;
    }

    public void removeArena(String arena) {
        arenas.remove(arena);
        updateArenasFile();
    }
}
