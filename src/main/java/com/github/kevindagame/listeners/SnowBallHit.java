package com.github.kevindagame.listeners;

import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SnowBallHit implements Listener {
    private final long damage;
    private final SnowBallFight snowBallFight;

    public SnowBallHit(SnowBallFight snowBallFight, long damage) {
        this.damage = damage;
        this.snowBallFight = snowBallFight;
    }

    @EventHandler
    public void onSnowBallHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            if (event.getEntity().hasMetadata("sbf") || snowBallFight.getGame() != null) {
                if (snowBallFight.getGame().getRoundStatus() == RoundStatus.RUNNING) {
                    if (event.getHitEntity() != null && event.getHitEntity() instanceof Player && event.getEntity().getShooter() instanceof Player) {
                        snowBallFight.getGame().checkHit((Player) event.getEntity().getShooter(), (Player) event.getHitEntity());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (snowBallFight.getGame() != null) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && snowBallFight.getGame().hasPlayer((Player) event.getEntity())&& snowBallFight.getGame().hasPlayer((Player) event.getDamager()) &&event.getDamage() != snowBallFight.getSnowBallDamage()) {
                event.setCancelled(true);
            }

        }
    }
}
