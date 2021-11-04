package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Scoreboard {

    private final Game game;
    private final SnowBallFight sbf;
    private ScoreboardManager sbm;
    private final org.bukkit.scoreboard.Scoreboard board;
    private final Objective roundTimer;
    private Team timeUntilRoundStart;
    private Team timeUntilRoundEnd;
    public Scoreboard(SnowBallFight sbf, Game game) {
        this.sbf = sbf;
        this.game = game;
        sbm = Bukkit.getScoreboardManager();
        board = sbm.getNewScoreboard();

        roundTimer = board.registerNewObjective("counter", "dummy","score");
        roundTimer.setDisplaySlot(DisplaySlot.SIDEBAR);

        timeUntilRoundStart = board.registerNewTeam("TURS");
        timeUntilRoundStart.addEntry(ChatColor.RED + "");
        timeUntilRoundStart.setPrefix(game.getTimeString());
        roundTimer.getScore(ChatColor.RED + "").setScore(14);

        Runnable Update = new Runnable() {
            @Override
            public void run() {
                timeUntilRoundStart.setPrefix(game.getTimeString());
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(sbf, Update, 10, 20);
    }

    public void addPlayer(Player p) {
        p.setScoreboard(board);


    }

    public void clearScoreBoard(Player p){
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

}
