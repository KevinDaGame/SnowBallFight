package com.github.kevindagame;

import org.bukkit.block.data.type.Snow;

public class Game {
    private final SnowBallFight snowBallFight;
    private final int rounds;

    public Game(SnowBallFight snowBallFight, int rounds) {
        this.snowBallFight = snowBallFight;
        this.rounds = rounds;
    }
}
