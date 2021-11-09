package com.github.kevindagame.Language;

import com.github.kevindagame.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Lang {

    public static void roundWinner(GameTeam team){
        Bukkit.broadcastMessage("Team " + team.getColor() + team.getColor().name() + ChatColor.RESET + " has won this round");
    }
    public static void gameWinner(GameTeam team){
        Bukkit.broadcastMessage("Team " + team.getColor() + team.getColor().name() + ChatColor.RESET + " has won the game with " + team.getWins() + " wins!");
    }



}
