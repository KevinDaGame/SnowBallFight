package com.github.kevindagame.listeners;

import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private final SnowBallFight snowBallFight;

    public PlayerDeath(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.broadcastMessage("event called");
        Player p = event.getEntity();
        if (snowBallFight.getGame() == null || p.getKiller() == null) {
            if (p.getKiller() == null) Bukkit.broadcastMessage("killer is null");
            return;
        }
        if (snowBallFight.getGame().getRoundStatus() == RoundStatus.RUNNING) {
            ProtectedRegion region = snowBallFight.getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getRegion(snowBallFight.getGame().getArena().getRegion());
            Location loc = p.getLocation();
            if (region != null && region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && snowBallFight.getGame().getArena().getWorld().equals(p.getWorld().getName())) {
                snowBallFight.getGame().killPlayer(p, p.getKiller());
            }
        }
    }
}
