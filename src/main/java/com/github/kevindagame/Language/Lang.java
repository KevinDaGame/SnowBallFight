package com.github.kevindagame.Language;

import com.github.kevindagame.Model.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Lang {

    public static void roundWinner(GameTeam team) {
        Lang.broadcastMessage("Team " + team.getColor() + team.getColor().name() + ChatColor.RESET + " has won this round");
    }

    public static void gameWinner(GameTeam team) {
        Lang.broadcastMessage("Team " + team.getColor() + team.getColor().name() + ChatColor.RESET + " has won the game with " + team.getWins() + " wins!");
    }


    public static void noWinner() {
        Lang.broadcastMessage("The game ended in a tie!");
    }

    public static void sendMessage(CommandSender p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSBF &8┃ &r" + message));

    }
    public static void broadcastMessage(String message){
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSBF &8┃ &r" + message));

    }
}
