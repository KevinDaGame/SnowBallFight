package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Scoreboard {

    private final SnowBallFight sbf;
    private final GameTeam team;
    private final GamePlayer player;
    private final org.bukkit.scoreboard.Scoreboard board;
    private final Objective objective;
    private ScoreboardManager sbm;
    private Team wins;
    private Team timeUntilRoundStart;
    private Team losses;
    private Team kills;
    private Team teamTeam;

    public Scoreboard(GameTeam team, GamePlayer player) {
        this.sbf = team.getGame().getMain();
        this.team = team;
        this.player = player;

        sbm = Bukkit.getScoreboardManager();
        board = sbm.getNewScoreboard();

        objective = board.registerNewObjective("counter", "dummy", "Snowballfight");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        timeUntilRoundStart = board.registerNewTeam("TURS");
        wins = board.registerNewTeam("WINS");
        losses = board.registerNewTeam("LOSSES");
        kills = board.registerNewTeam("KILLS");
        teamTeam = board.registerNewTeam("TEAM");

        timeUntilRoundStart.addEntry(ChatColor.YELLOW + "");
        wins.addEntry(ChatColor.GREEN + "");
        losses.addEntry(ChatColor.RED + "");
        kills.addEntry(ChatColor.DARK_RED + "");
        teamTeam.addEntry(ChatColor.BLACK + "");

        player.getPlayer().setScoreboard(board);

        updateValues();


        objective.getScore(ChatColor.BLACK + "").setScore(15);
        objective.getScore(ChatColor.YELLOW + "").setScore(14);
        objective.getScore(ChatColor.DARK_RED + "").setScore(13);
        objective.getScore(ChatColor.GREEN + "").setScore(12);
        objective.getScore(ChatColor.RED + "").setScore(11);

        Runnable Update = new Runnable() {
            @Override
            public void run() {
                updateValues();
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(sbf, Update, 10, 20);
    }

    private void updateValues() {
        timeUntilRoundStart.setPrefix(team.getGame().getTimeString());
        kills.setPrefix("Kills: " + player.getKills() + "");
        wins.setPrefix("Wins: " + team.getWins() + "");
        losses.setPrefix("Losses: " + team.getLosses() + "");
        teamTeam.setPrefix("Your team: " + team.getColor() + team.getColor().name() + "");
    }

    public void clearScoreBoard() {
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

}
