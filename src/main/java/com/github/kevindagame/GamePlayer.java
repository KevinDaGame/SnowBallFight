package com.github.kevindagame;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GamePlayer {
    private final GameTeam team;
    private final Scoreboard scoreboard;
    private Player player;
    private int kills;
    private boolean isAlive;

    public GamePlayer(Player player, GameTeam team) {
        this.player = player;
        this.team = team;
        this.kills = 0;
        this.isAlive = true;
        this.scoreboard = new Scoreboard(team, this);
        giveKit();
    }

    private void giveKit() {
        PlayerInventory inv = player.getInventory();
        inv.addItem(new ItemStack(Material.SNOWBALL, 1));

        inv.setHelmet(getArmourPiece(Material.LEATHER_HELMET));
        inv.setChestplate(getArmourPiece(Material.LEATHER_CHESTPLATE));
        inv.setLeggings(getArmourPiece(Material.LEATHER_LEGGINGS));
        inv.setBoots(getArmourPiece(Material.LEATHER_BOOTS));

    }

    private ItemStack getArmourPiece(Material material) {
        ItemStack item = new ItemStack(material, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(team.getArmourColor());
        item.setItemMeta(meta);
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public GameTeam getTeam() {
        return team;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public void die() {
        isAlive = false;
    }


    public void clearScoreboard() {
        scoreboard.clearScoreBoard();
    }

    public void revive() {
        isAlive = true;
    }
}
