package com.github.kevindagame.listeners;

import com.github.kevindagame.Game;
import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {
    private SnowBallFight snowBallFight;

    public PlayerMovement(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Game game = snowBallFight.getGame();
        if (game != null) {
            if (game.getRoundStatus() == RoundStatus.BETWEEN || game.getRoundStatus() == RoundStatus.STARTING) {
                if (game.hasPlayer(e.getPlayer())) {
                    Location from = e.getFrom();
                    Location to = e.getTo();
                    if(from.getX() != to.getX() || from.getY() != to.getY() || from.getY() != to.getY() ){
                        e.setCancelled(true);
                    }

                }

            }
        }

    }
}
