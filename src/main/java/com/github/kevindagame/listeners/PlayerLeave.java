package com.github.kevindagame.listeners;

import com.github.kevindagame.Game;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {
    private final SnowBallFight snowBallFight;
    public PlayerLeave(SnowBallFight sbf) {
        this.snowBallFight = sbf;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
          Game game = snowBallFight.getGame();
          Player p = e.getPlayer();
          if(game != null){
              if(game.hasPlayer(p)){
                  game.removePlayer(p);
              }
          }
    }
}
