package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private final SnowBallFight snowBallFight;
    private final int rounds;
    private final int timePerRound;
    private final int timeBetweenRound;
    private final Arena arena;
    private final int maxPlayers = 5;
    private Timer timer;
    private RoundStatus status;
    private Scoreboard scoreboard;
    private GameTeam[] teams;

    public Game(SnowBallFight snowBallFight, int rounds, int timePerRound, int timeBetweenRound, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.timeBetweenRound = timeBetweenRound;
        this.arena = arena;
        createTeams();
        start();
    }

    public Game(SnowBallFight snowBallFight, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = snowBallFight.getDefaultRounds();
        this.timePerRound = snowBallFight.getDefaultTimePerRound();
        this.timeBetweenRound = snowBallFight.getDefaultTimeBetweenRound();
        this.arena = arena;
        createTeams();
        start();
    }

    private void createTeams() {
        teams = new GameTeam[2];
        teams[0] = new GameTeam(Color.GREEN, maxPlayers);
        teams[1] = new GameTeam(Color.AQUA, maxPlayers);
    }

    public void start() {
        //TODO send start message
        status = RoundStatus.STARTING;
        timer = new Timer(snowBallFight, this, rounds, timePerRound, timeBetweenRound,
                () -> { // after game
                    Bukkit.broadcastMessage("game done");
                    handleGameEnd();
                },
                (t) -> { // after each round
                    Bukkit.broadcastMessage("round done");
                    handleNextRound(t);
                }
        );
        timer.startTimerInitial();
        scoreboard = new Scoreboard(snowBallFight, this);
        for (Player p: Bukkit.getOnlinePlayers()) {
            scoreboard.addPlayer(p);
        }
    }

    private void handleNextRound(Timer timer) {

        timer.betweenRound();
    }

    private void handleGameEnd(){
        snowBallFight.stopGame();
        for (Player p: Bukkit.getOnlinePlayers()) {
            scoreboard.clearScoreBoard(p);
        }

    }

    public void setRoundStatus(RoundStatus status){
        this.status = status;
    }
    public RoundStatus getRoundStatus(){
        return status;
    }

    public Timer getTimer(){
        return timer;
    }

    public String getTimeString(){
        if(status == RoundStatus.BETWEEN) return "Round starts in: " + timer.getSecondsUntilRoundStart() + " seconds";
        else if(status == RoundStatus.RUNNING) return "Round ends in: " + timer.getSecondsUntilRoundEnd() + " seconds";
        return "error";
    }

    public Arena getArena() {
        return arena;
    }

    public ArrayList<GamePlayer> getPlayers() {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (GameTeam t: teams) {
            players.addAll(Arrays.asList(t.getPlayers()));
        }
        return players;
    }

    private GameTeam getTeamIdToJoin(){
        //get minimum playercount
        int min = teams[0].getPlayerCount();
        for (int i = 0; i < teams.length; i++) {
            if(teams[i].getPlayerCount() < min) {
                min = teams[i].getPlayerCount();
            }
        }
        if(min >= maxPlayers) return null;
        //get all teams with minimum playercount
        ArrayList<GameTeam> selectTeams = new ArrayList<>();
        for(int i = 0; i < teams.length; i++){
            if(teams[i].getPlayerCount() == min){
                selectTeams.add(teams[i]);
            }

        }
        Random random = new Random();
        int index = random.nextInt(selectTeams.size());
        return selectTeams.get(index);
    }

    public boolean join(Player p){
        GameTeam team = getTeamIdToJoin();
        if(team == null) return false;
        team.addPlayer(p);
        Bukkit.broadcastMessage("team 1: " + teams[0].getPlayerCount() + "team 2: " + teams[1].getPlayerCount());
        return true;
    }

    public boolean hasPlayer(Player p) {
        for (GamePlayer player: getPlayers()) {
            if(player != null && player.getPlayer().getUniqueId() == p.getUniqueId()) return true;
        }
        return false;
    }
}
