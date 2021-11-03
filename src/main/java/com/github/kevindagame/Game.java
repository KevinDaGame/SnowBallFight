package com.github.kevindagame;

import org.bukkit.Bukkit;

public class Game {
    private final SnowBallFight snowBallFight;
    private final int rounds;
    private final int timePerRound;
    private final int timeBetweenRound;
    private Timer timer;
    private int task;
    private RoundStatus status;

    public Game(SnowBallFight snowBallFight, int rounds, int timePerRound, int timeBetweenRound) {
        this.snowBallFight = snowBallFight;
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.timeBetweenRound = timeBetweenRound;
        start();
    }

    public void start() {
        //TODO send start message
        status = RoundStatus.STARTING;
        timer = new Timer(snowBallFight, this, rounds, timePerRound, timeBetweenRound,
                () -> { // after game
                    Bukkit.broadcastMessage("game done");
                },
                (t) -> { // after each round
                    Bukkit.broadcastMessage("round done");
                    handleNextRound(t);
                }
        );
        timer.startTimerInitial();
    }

    private void handleNextRound(Timer timer) {

        timer.betweenRound();
    }

    public void setRoundStatus(RoundStatus status){
        this.status = status;
    }
    public RoundStatus getRoundStatus(){
        return status;
    }
}
