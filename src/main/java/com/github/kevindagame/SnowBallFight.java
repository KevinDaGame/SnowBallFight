package com.github.kevindagame;

import com.github.kevindagame.commands.SnowBallFightCommand;
import com.github.kevindagame.commands.SnowBallFightTabCompleter;
import com.github.kevindagame.listeners.SnowBallHit;
import com.github.kevindagame.listeners.SnowBallThrow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SnowBallFight extends JavaPlugin {
    private Game game;
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Map<String, Arena> arenas;
    private File arenasFile;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SnowBallThrow(this), this);
        getServer().getPluginManager().registerEvents(new SnowBallHit(this, 5), this);
        getCommand("snowballfight").setExecutor(new SnowBallFightCommand(this));
        getCommand("snowballfight").setTabCompleter(new SnowBallFightTabCompleter(this));

        arenasFile = new File(getDataFolder(), "arenas.json");

        if (!arenasFile.exists()) saveResource(arenasFile.getName(), false);

        try {
            Type type = new TypeToken<HashMap<String, Arena>>(){}.getType();
            arenas = gson.fromJson(new FileReader(arenasFile), type);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find Arena's file. Stopping plugin");
            Bukkit.getPluginManager().disablePlugin(this);
        }
//        arenas.put("arena1", new Arena("region1", new SpawnPoint[]{new SpawnPoint(10, 0, 10, "world"), new SpawnPoint( 1, 10, 10,"world")}));
//        arenas.put("arena2", new Arena("region1", new SpawnPoint[]{new SpawnPoint(10, 0, 10, "world"), new SpawnPoint( 1, 10, 10,"world")}));
//        arenas.put("arena3", new Arena("region1", new SpawnPoint[]{new SpawnPoint(10, 0, 10, "world"), new SpawnPoint( 1, 10, 10,"world")}));
//        arenas.put("arena4", new Arena("region1", new SpawnPoint[]{new SpawnPoint(10, 0, 10, "world"), new SpawnPoint( 1, 10, 10,"world")}));
//        arenas.put("arena5", new Arena("region1", new SpawnPoint[]{new SpawnPoint(10, 0, 10, "world"), new SpawnPoint( 1, 10, 10,"world")}));
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        String json = gson.toJson(arenas); // Remember pretty printing? This is needed here.
        arenasFile.delete(); // won't throw an exception, don't worry.
        try {
            Files.write(arenasFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE); // java.nio.Files
        } catch (IOException e) {
            System.out.println("Snowballfight: Error on saving arenas!!!!!");
        }
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
}
