package com.github.kevindagame.commands;

import com.github.kevindagame.Game;
import com.github.kevindagame.SnowBallFight;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SnowBallFightCommand implements CommandExecutor {
    private final SnowBallFight snowBallFight;

    public SnowBallFightCommand(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        switch (args[0]) {
            case "start":
                snowBallFight.setGame(new Game(snowBallFight, 2));
                return true;
            case "give":
                if(commandSender instanceof Player){
                ItemStack snowball = new ItemStack(Material.SNOWBALL, 1);
                snowball.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                ((Player) commandSender).getInventory().addItem(snowball);

                }
        }
        return true;
    }
}
