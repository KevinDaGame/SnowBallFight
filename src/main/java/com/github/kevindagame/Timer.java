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

    }
    public void stopBetweenRoundTimer() {
        Bukkit.getScheduler().cancelTask(betweenRoundTaskID);

    }

    public void startRoundTimer() {
        Bukkit.broadcastMessage("starting round");
        game.setRoundStatus(RoundStatus.RUNNING);
        counter = 0;
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
//        if(counter % 10 == 0){
//            Bukkit.broadcastMessage("Time remaining: " + (timePerRound - counter/10) + "seconds");
//        }
    }

    public void betweenRound() {
        game.setRoundStatus(RoundStatus.BETWEEN);
        Runnable runnable = new Runnable() {
            private int counter = 0;
            @Override
            public void run() {
                if(counter >= timeBetweenRound){
                    stopBetweenRoundTimer();

                    startRoundTimer();
                }
                else{
//                    Bukkit.broadcastMessage("Time until next round: " + (timeBetweenRound - counter) + " seconds");
                    counter++;
                }
            }
        };
        betweenRoundTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(snowBallFight, runnable, 0, 20);
    }
}
