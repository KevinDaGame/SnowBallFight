package com.github.kevindagame;


import com.github.kevindagame.Language.Lang;
import com.github.kevindagame.Model.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final SnowBallFight snowBallFight;
    private final String afterGameCommand;
    private final double snowBallDamage;
    private final int timeBetweenRound;
    private final Arena arena;
    private final PluginConfig config;
    private final String deathCommand;
    private int rounds;
    private int timePerRound;
    private int maxPlayers;
    private Timer timer;
    private RoundStatus status;
    private GameTeam[] teams;
    private int clearInvTimer;
    private int gameStopTimer;

    public Game(SnowBallFight snowBallFight, Arena arena, PluginConfig config, int rounds, int timePerRound, int playersPerTeam) {
        this(config, snowBallFight, arena);
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.maxPlayers = playersPerTeam;
        createTeams();
    }

    public Game(SnowBallFight snowBallFight, Arena arena, PluginConfig config) {
        this(config, snowBallFight, arena);
        createTeams();
    }

    private Game(PluginConfig config, SnowBallFight sbf, Arena arena) {
        this.config = config;
        this.snowBallFight = sbf;
        this.arena = arena;

        this.afterGameCommand = config.getAfterGameCommand();
        this.deathCommand = config.getDeathCommand();
        this.rounds = config.getDefaultRounds();
        this.timeBetweenRound = config.getDefaultTimeBetweenRound();
        this.timePerRound = config.getDefaultTimePerRound();
        this.maxPlayers = config.getMaxPlayers();
        this.snowBallDamage = config.getSnowBallDamage();
        setRoundStatus(RoundStatus.STARTING);
    }

    public SnowBallFight getMain() {
        return snowBallFight;
    }

    public GameTeam[] getTeams() {
        return teams;
    }

    public RoundStatus getRoundStatus() {
        return status;
    }

    public void setRoundStatus(RoundStatus status) {
        this.status = status;
    }

    public Arena getArena() {
        return arena;
    }

    public String getTimeString() {
        if (status == RoundStatus.BETWEEN) return "Round starts in: " + timer.getSecondsUntilRoundStart() + " seconds";
        else if (status == RoundStatus.RUNNING) {
            return "Round ends in: " + (int) timer.getSecondsUntilRoundEnd() / 60 + "M " + timer.getSecondsUntilRoundEnd() % 60 + "S";
        } else if (status == RoundStatus.STARTING) return "The game will start soon!";
        else if (status == RoundStatus.FINISHED) return "The game has finished";
        return "error";
    }

    public ArrayList<GamePlayer> getPlayers() {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (GameTeam t : teams) {
            for (GamePlayer p : t.getPlayers()) {
                if (p != null) {
                    players.add(p);
                }
            }
        }
        return players;
    }

    public GamePlayer getPlayer(Player player) {
        ArrayList<GamePlayer> players = getPlayers();
        for (GamePlayer p : players) {
            if (p.getPlayer().getUniqueId() == player.getUniqueId()) {
                return p;
            }
        }
        return null;
    }

    private GameTeam getTeamIdToJoin() {
        //get minimum playercount
        int min = teams[0].getPlayerCount();
        for (GameTeam team : teams) {
            if (team.getPlayerCount() < min) {
                min = team.getPlayerCount();
            }
        }
        if (min >= maxPlayers) return null;
        //get all teams with minimum playercount
        ArrayList<GameTeam> selectTeams = new ArrayList<>();
        for (GameTeam team : teams) {
            if (team.getPlayerCount() == min) {
                selectTeams.add(team);
            }

        }
        Random random = new Random();
        int index = random.nextInt(selectTeams.size());
        return selectTeams.get(index);
    }

    public GameTeam getOpposingTeam(GameTeam team) {
        if (team.getName().equals(teams[0].getName())) {
            return teams[1];
        }
        return teams[0];
    }

    private void createTeams() {
        List<Team> tempTeams = arena.getTeams();
        teams = new GameTeam[tempTeams.size()];
        for (int i = 0; i < tempTeams.size(); i++) {
            Team team = arena.getTeams().get(i);
            SpawnPoint spawnPoint = team.getSpawnPoint();
            teams[i] = new GameTeam(this, new Location(Bukkit.getWorld(spawnPoint.getWorld()), spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), spawnPoint.getYaw(), spawnPoint.getPitch()), ChatColor.valueOf(team.getColor()), maxPlayers);
        }
    }

    public void start() {
        //TODO send start message
        timer = new Timer(snowBallFight, this, rounds, timePerRound, timeBetweenRound,
                () -> { // after game
                    endOfRoundCheck();
                    handleGameEnd();
                },
                (t) -> { // after each round
                    if (timer.getRoundsRan() != 1) {
                        endOfRoundCheck();

                    }
                    reviveTeams();
                    handleNextRound(t);
                }
        );
        timer.startTimerInitial();
        reviveTeams();
    }

    private void reviveTeams() {
        for (GameTeam team : teams) {
            team.revive();
        }
    }

    private void handleNextRound(Timer timer) {

        timer.betweenRound();
    }

    private void handleGameEnd() {
        setRoundStatus(RoundStatus.FINISHED);
        GameTeam winnerCandidate = teams[0];
        boolean tie = false;
        for (int i = 1; i < teams.length; i++) {
            if (teams[i].getWins() > winnerCandidate.getWins()) winnerCandidate = teams[i];
            else if (teams[i].getWins() == winnerCandidate.getWins()) tie = true;
        }
        if (tie) {
            Lang.broadcastMessage("The game ended in a tie!");
        } else {
            Lang.gameWinner(winnerCandidate);
//            Lang.broadcastMessage("Team " + winnerCandidate.getColor() + winnerCandidate.getColor().name() + ChatColor.RESET + " has won the game with " + winnerCandidate.getWins() + " wins!");
        }
        gameStopTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(snowBallFight, () -> {
            snowBallFight.stopGame();
            for (GameTeam t : teams) {
                t.removeScoreboard();
            }

        }, 200);
        clearInvTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(snowBallFight, () -> {
            for (GamePlayer p : getPlayers()) {
                if (config.getClearInventory()) {
                    p.getPlayer().getInventory().clear();
                }
            }
        }, 20);

    }

    public boolean join(Player p) {
        GameTeam team = getTeamIdToJoin();
        if (team == null) return false;
        team.addPlayer(p);
        return true;
    }

    public boolean hasPlayer(Player p) {
        for (GamePlayer player : getPlayers()) {
            if (player != null && player.getPlayer().getUniqueId() == p.getUniqueId()) return true;
        }
        return false;
    }

    public void killPlayer(Player victim, Player killer) {
        Lang.broadcastMessage(victim.getDisplayName() + " was frozen by " + killer.getDisplayName());
        GamePlayer gameVictim = getPlayer(victim);
        GamePlayer gamekiller = getPlayer(killer);
        gameVictim.die();
        gamekiller.addKill();
        Bukkit.getScheduler().scheduleSyncDelayedTask(snowBallFight, () -> {
            victim.performCommand(deathCommand);
        }, 10);
        roundWonCheck();
    }

    private void endOfRoundCheck() {
        int team1Players = teams[0].getAlivePlayers();
        int team2Players = teams[1].getAlivePlayers();
        if (team1Players == team2Players) {
            Lang.broadcastMessage("It is a tie");
        } else if (team1Players > team2Players) {
            Lang.roundWinner(teams[0]);
            teams[0].win();
            teams[1].lose();
        } else {
            Lang.roundWinner(teams[1]);
            teams[1].win();
            teams[0].lose();
        }
    }

    private void roundWonCheck() {
        ArrayList<GameTeam> alive = new ArrayList<>();

        for (GameTeam team : teams) {
            if (team.getAlivePlayers() > 0) {
                alive.add(team);
            }
        }
        if (alive.size() == 1) {
            timer.afterRound();

        }
    }

    public void checkHit(Player shooter, Player hitEntity) {
        GamePlayer gameShooter = getPlayer(shooter);
        GamePlayer gameHitEntity = getPlayer(hitEntity);
        if (gameShooter != null && gameHitEntity != null) {
            if (gameShooter.getTeam() != gameHitEntity.getTeam()) {
                hitEntity.damage(snowBallDamage, shooter);
            }
        }
    }


    public void stop() {
        if (timer != null) {
            timer.stopRoundTimer();
            timer.stopBetweenRoundTimer();
        }
        if (Bukkit.getScheduler().isQueued(gameStopTimer)) Bukkit.getScheduler().cancelTask(gameStopTimer);
        if (Bukkit.getScheduler().isQueued(clearInvTimer)) Bukkit.getScheduler().cancelTask(clearInvTimer);
        for (GameTeam t : teams) {
            t.removeScoreboard();
        }
        for (GamePlayer p : getPlayers()) {
            if (config.getClearInventory()) {
                p.getPlayer().getInventory().clear();
            }
            p.getPlayer().setHealth(20);
            p.getPlayer().performCommand(afterGameCommand);
        }
    }

    public void removePlayer(Player p) {
        GamePlayer player = getPlayer(p);
        player.getTeam().removePlayer(player);
        player.clearScoreboard();
        for (GameTeam team : teams) {
            if (team.getPlayerCount() == 0) {
                Lang.broadcastMessage("Game ended prematurely since a team is empty");
                handleGameEnd();
            }
        }
    }
}
