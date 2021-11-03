package com.github.kevindagame.listeners;

import com.github.kevindagame.Game;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class SnowBallThrow implements Listener {

    private final SnowBallFight snowBallFight;
    private Game game;

    public SnowBallThrow(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @EventHandler
    public void onSnowBallThrow(ProjectileLaunchEvent event){

    }
}
