package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {
    private final SnowBallFight snowBallFight;
    private final int rounds;
    private final int timePerRound;
    private final int timeBetweenRound;
    private final Arena arena;
    private Timer timer;
    private RoundStatus status;
    private Scoreboard scoreboard;

    public Game(SnowBallFight snowBallFight, int rounds, int timePerRound, int timeBetweenRound, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.timeBetweenRound = timeBetweenRound;
        this.arena = arena;
        start();
    }

    public Game(SnowBallFight snowBallFight, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = snowBallFight.getDefaultRounds();
        this.timePerRound = snowBallFight.getDefaultTimePerRound();
        this.timeBetweenRound = snowBallFight.getDefaultTimeBetweenRound();
        this.arena = arena;
        start();
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
}
