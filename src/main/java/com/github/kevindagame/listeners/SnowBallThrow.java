package com.github.kevindagame.listeners;

import com.github.kevindagame.Game;
import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class SnowBallThrow implements Listener {

    private final SnowBallFight snowBallFight;
    private Game game;
    private final long delay = 10;

    public SnowBallThrow(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @EventHandler
    public void onSnowBallThrow(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof Snowball){
            if(event.getEntity().getShooter() instanceof Player){
                Player p = (Player) event.getEntity().getShooter();
                ItemStack i;
                if(p.getInventory().getItemInMainHand().getType() == Material.SNOWBALL){
                    i = p.getInventory().getItemInMainHand();
                }
                else{
                    return;
                }

                if(i.containsEnchantment(Enchantment.ARROW_INFINITE)){
                    if(snowBallFight.getGame() == null){
                        p.sendMessage("no game running!");
                        event.setCancelled(true);
                        return;
                    }
                    if(snowBallFight.getGame().getRoundStatus() != RoundStatus.RUNNING){
                        event.setCancelled(true);
                        return;
                    }
                        event.getEntity().setMetadata("sbf", new FixedMetadataValue(snowBallFight, true));
                        ItemStack item = new ItemStack(Material.SNOWBALL, 1);
                        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(snowBallFight, new Runnable() {
                            @Override
                            public void run() {
                                p.getInventory().setItemInMainHand(item);
                            }
                        }, delay);

                }
                else{
                    return;
                }
            }
        }

    }
}
