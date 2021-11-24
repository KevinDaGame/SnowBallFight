package com.github.kevindagame.Language;

import com.github.kevindagame.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lang {

    public static void sendMessage(CommandSender p, String message){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSBF &8┃ &r" + message));

    }
    public static void broadcastMessage(String message){
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSBF &8┃ &r" + message));

    }
}
