package com.github.kevindagame;

import com.github.kevindagame.Model.Arena;
import com.github.kevindagame.Model.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class ArenaHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HashMap<String, Arena> arenas;
    private final File arenasFile;

    public ArenaHandler(File arenasFile) {
        this.arenasFile = arenasFile;
        try {
            Type type = new TypeToken<HashMap<String, Arena>>() {
            }.getType();
            arenas = gson.fromJson(new FileReader(arenasFile), type);
            if (arenas == null) arenas = new HashMap<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveArenasFile() {
        String json = gson.toJson(arenas);
        arenasFile.delete();
        try {
            Files.write(arenasFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.println("Snowballfight: Error on saving arenas!!!!!");
        }
    }

    public boolean addArena(String name, World world, String region) {
        Arena arena = new Arena(region, world.getName());
        arenas.put(name, arena);
        saveArenasFile();
        return true;
    }

    public void removeArena(String arena) {
        arenas.remove(arena);
        saveArenasFile();
    }

    public void addTeam(String arena, Team team) {
        arenas.get(arena).getTeams().add(team);
        saveArenasFile();
    }

    public HashMap<String, Arena> getArenas() {
        return arenas;
    }
}
