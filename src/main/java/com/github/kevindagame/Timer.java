package com.github.kevindagame;

import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Timer implements Runnable{
    private final SnowBallFight snowBallFight;
    private final Game game;
    private final int rounds;
    private final int timePerRound;
    private final int timeBetweenRound;
    private final Runnable gameEnd;
    private final Consumer<Timer> everyRound;
    private int roundTaskID;
    private int betweenRoundTaskID;
    private int roundsRan;
    private int counter;
    private int betweenCounter;


    public Timer(SnowBallFight snowBallFight, Game game, int rounds, int timePerRound, int timeBetweenRound, Runnable gameEnd, Consumer<Timer> everyRound) {
        this.snowBallFight = snowBallFight;
        this.game = game;
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.timeBetweenRound = timeBetweenRound;
        this.gameEnd = gameEnd;
        this.everyRound = everyRound;
    }

    public void afterRound(){
        stopRoundTimer();
        if(roundsRan >= rounds){
            gameEnd.run();
            return;
        }
        counter = 0;
        roundsRan += 1;
        everyRound.accept(this);
    }

    public void stopRoundTimer() {
        Bukkit.getScheduler().cancelTask(roundTaskID);
        counter = 0;

    }
    public void stopBetweenRoundTimer() {
        Bukkit.getScheduler().cancelTask(betweenRoundTaskID);
        betweenCounter = 0;

    }

    public void startRoundTimer() {
        Bukkit.broadcastMessage("starting round");
        game.setRoundStatus(RoundStatus.RUNNING);
        roundTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(snowBallFight, this, 2, 2);
    }

    public void startTimerInitial(){
        afterRound();
    }

    @Override
    public void run() {
        counter++;
        if(counter >= timePerRound * 10){
            afterRound();
        }
    }

    public void betweenRound() {
        game.setRoundStatus(RoundStatus.BETWEEN);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(betweenCounter >= timeBetweenRound){
                    stopBetweenRoundTimer();

                    startRoundTimer();
                }
                else{
                    betweenCounter++;
                }
            }
        };
        betweenRoundTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(snowBallFight, runnable, 0, 20);
    }

    public int getSecondsUntilRoundStart(){
        if(betweenCounter != 0){
            return timeBetweenRound - betweenCounter;
        }
        return -1;
    }
    public int getSecondsUntilRoundEnd(){
        if(counter != 0){
            return timePerRound - (counter / 10);
        }
        return -1;
    }
}
