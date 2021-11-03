package com.github.kevindagame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SnowBallHit implements Listener {
    @EventHandler
    public void onSnowBallHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            if (event.getEntity().hasMetadata("sbf")) {

                if (event.getHitEntity() != null && event.getHitEntity() instanceof Player) {
                    ((Player) event.getHitEntity()).damage(5);
                }
            }
        }
    }

}
