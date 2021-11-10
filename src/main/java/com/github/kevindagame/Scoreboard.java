package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private Team border1;
    private Team border2;
    private Team team1Players;
    private Team team2Players;
    private Team team1PlayersRemaining;
    private Team team2PlayersRemaining;

    public Scoreboard(GameTeam team, GamePlayer player) {
        this.sbf = team.getGame().getMain();
        this.team = team;
        this.player = player;

        sbm = Bukkit.getScoreboardManager();
        board = sbm.getNewScoreboard();

        objective = board.registerNewObjective("counter", "dummy", ChatColor.translateAlternateColorCodes('&', "&aSnow&fball&c fight"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        teamTeam = board.registerNewTeam("TEAM");
        border1 = board.registerNewTeam("BORDER1");
        timeUntilRoundStart = board.registerNewTeam("TURS");
        team1Players = board.registerNewTeam("T1P");
        team2Players = board.registerNewTeam("T2P");
        team1PlayersRemaining = board.registerNewTeam("T1PR");
        team2PlayersRemaining = board.registerNewTeam("T2PR");
        kills = board.registerNewTeam("KILLS");
        wins = board.registerNewTeam("WINS");
        losses = board.registerNewTeam("LOSSES");
        border2 = board.registerNewTeam("BORDER2");

        teamTeam.addEntry(ChatColor.DARK_RED + "");
        border1.addEntry(ChatColor.BLACK + "");
        timeUntilRoundStart.addEntry(ChatColor.YELLOW + "");
        team1Players.addEntry(ChatColor.AQUA + "");
        team2Players.addEntry(ChatColor.BLUE + "");
        team1PlayersRemaining.addEntry(ChatColor.DARK_AQUA + "");
        team2PlayersRemaining.addEntry(ChatColor.DARK_BLUE + "");
        kills.addEntry(ChatColor.DARK_GRAY + "");
        wins.addEntry(ChatColor.DARK_GREEN + "");
        losses.addEntry(ChatColor.LIGHT_PURPLE + "");
        border2.addEntry(ChatColor.GRAY + "");

        player.getPlayer().setScoreboard(board);

        updateValues();

        objective.getScore(ChatColor.BLACK + "").setScore(15);
        objective.getScore(ChatColor.DARK_RED + "").setScore(14);
        objective.getScore(ChatColor.YELLOW + "").setScore(13);
        objective.getScore(ChatColor.AQUA + "").setScore(12);
        objective.getScore(ChatColor.BLUE + "").setScore(11);
        objective.getScore(ChatColor.DARK_AQUA + "").setScore(10);
        objective.getScore(ChatColor.DARK_BLUE + "").setScore(9);
        objective.getScore(ChatColor.DARK_GRAY + "").setScore(8);
        objective.getScore(ChatColor.DARK_GREEN + "").setScore(7);
        objective.getScore(ChatColor.LIGHT_PURPLE + "").setScore(6);
        objective.getScore(ChatColor.GRAY + "").setScore(5);

        Runnable Update = new Runnable() {
            @Override
            public void run() {
                updateValues();
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(sbf, Update, 10, 20);
    }

    private void updateValues() {
        teamTeam.setPrefix("Your team: " + team.getColor() + team.getColor().name() + "");
        border1.setPrefix(ChatColor.translateAlternateColorCodes('&', "&c&m---&a&m---&f&m---&c&m---&a&m---&f&m---"));
        timeUntilRoundStart.setPrefix(ChatColor.translateAlternateColorCodes('&', team.getGame().getTimeString()));
        GameTeam[] teams = sbf.getGame().getTeams();
        team1Players.setPrefix("Team " + teams[0].getName() + ": " + teams[0].getColor() + teams[0].getPlayerCount());
        team2Players.setPrefix("Team " + teams[1].getName() +": " + teams[1].getColor() + teams[1].getPlayerCount());
        team1PlayersRemaining.setPrefix(ChatColor.translateAlternateColorCodes('&', "Opponents remaining: &c" + sbf.getGame().getOpposingTeam(player.getTeam()).getAlivePlayers()));
        team2PlayersRemaining.setPrefix(ChatColor.translateAlternateColorCodes('&', "Teammates remaining: &a" + player.getTeam().getAlivePlayers()));
        kills.setPrefix(ChatColor.translateAlternateColorCodes('&', "Kills: &c" + player.getKills()));
        wins.setPrefix(ChatColor.translateAlternateColorCodes('&', "Wins: &a" + team.getWins()));
        losses.setPrefix(ChatColor.translateAlternateColorCodes('&', "Losses: &c" + team.getLosses()));
        border2.setPrefix(ChatColor.translateAlternateColorCodes('&', "&c&m---&a&m---&f&m---&c&m---&a&m---&f&m---"));
    }

    public void clearScoreBoard() {
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

}
