package com.github.kevindagame.listeners;

import com.github.kevindagame.Game;
import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class SnowBallThrow implements Listener {

    private final SnowBallFight snowBallFight;
    private final long delay = 10;
    private Game game;

    public SnowBallThrow(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @EventHandler
    public void onSnowBallThrow(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Snowball) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player p = (Player) event.getEntity().getShooter();
                if (p.getInventory().getItemInMainHand().getType() != Material.SNOWBALL) {
                    return;
                }
                if (snowBallFight.getGame() == null) {
                    return;
                }
                ProtectedRegion region = snowBallFight.getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getRegion(snowBallFight.getGame().getArena().getRegion());
                Location loc = p.getLocation();
                if (region != null && region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && snowBallFight.getGame().getArena().getWorld().equals(p.getWorld().getName())) {
                    if (snowBallFight.getGame().getRoundStatus() != RoundStatus.RUNNING) {
                        event.setCancelled(true);
                        return;
                    }
                    event.getEntity().setMetadata("sbf", new FixedMetadataValue(snowBallFight, true));
                    ItemStack item = new ItemStack(Material.SNOWBALL, 1);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(snowBallFight, new Runnable() {
                        @Override

                        public void run() {
                            if (snowBallFight.getGame() != null) {
                                p.getInventory().setItemInMainHand(item);
                            }
                        }
                    }, delay);
                } else {
                    return;
                }

            }
        }

    }
}
