package com.github.kevindagame;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginConfig {
    private final double snowBallDamage;
    private final int rounds;
    private final int timeBetweenRound;
    private final int timePerRound;
    private final boolean clearInventory;
    private final int maxPlayers;
    private final String afterGameCommand;
    private final String deathCommand;
    private final int snowBallDelay;

    public PluginConfig(File configFile) {
        FileConfiguration reader = new YamlConfiguration();
        try {
            reader.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.rounds = reader.getInt("default amount of rounds");
        this.timePerRound = reader.getInt("default time per round");
        this.timeBetweenRound = reader.getInt("default time between round");
        this.snowBallDamage = reader.getDouble("snowball damage");
        this.clearInventory = reader.getBoolean("clear player inventory on game end");
        this.maxPlayers = reader.getInt("max players per team");
        this.afterGameCommand = reader.getString("command after game");
        this.deathCommand = reader.getString("command after death");
        this.snowBallDelay = reader.getInt("snowball delay");
    }

    public String getAfterGameCommand() {
        return afterGameCommand;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getDefaultRounds() {
        return rounds;
    }

    public int getDefaultTimePerRound() {
        return timePerRound;
    }

    public int getDefaultTimeBetweenRound() {
        return timeBetweenRound;
    }

    public double getSnowBallDamage() {
        return snowBallDamage;
    }

    public boolean getClearInventory() {
        return clearInventory;
    }

    public String getDeathCommand() {
        return deathCommand;
    }

    public int getSnowBallDelay() {
        return snowBallDelay;
    }
}
