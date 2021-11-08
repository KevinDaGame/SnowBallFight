package com.github.kevindagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final SnowBallFight snowBallFight;
    private final int rounds;
    private final int timePerRound;
    private final int snowBallDamage = 5;
    private final int timeBetweenRound;
    private final Arena arena;
    private final int maxPlayers = 5;
    private Timer timer;
    private RoundStatus status;
    private GameTeam[] teams;

    public Game(SnowBallFight snowBallFight, int rounds, int timePerRound, int timeBetweenRound, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = rounds;
        this.timePerRound = timePerRound;
        this.timeBetweenRound = timeBetweenRound;
        this.arena = arena;
        setRoundStatus(RoundStatus.STARTING);
        createTeams();
    }

    public Game(SnowBallFight snowBallFight, Arena arena) {
        this.snowBallFight = snowBallFight;
        this.rounds = snowBallFight.getDefaultRounds();
        this.timePerRound = snowBallFight.getDefaultTimePerRound();
        this.timeBetweenRound = snowBallFight.getDefaultTimeBetweenRound();
        this.arena = arena;
        setRoundStatus(RoundStatus.STARTING);
        createTeams();
    }

    private void createTeams() {
        List<Team> tempTeams = arena.getTeams();
        teams = new GameTeam[tempTeams.size()];
        for(int i = 0; i < tempTeams.size(); i++){
            Team team = arena.getTeams().get(i);
            SpawnPoint spawnPoint = team.getSpawnPoint();
            teams[i] = new GameTeam(this, new Location(Bukkit.getWorld(spawnPoint.world), spawnPoint.x, spawnPoint.y, spawnPoint.z),ChatColor.valueOf(team.getColor()), maxPlayers);
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
                    endOfRoundCheck();
                    reviveTeams();
                    handleNextRound(t);
                }
        );
        timer.startTimerInitial();
    }

    private void reviveTeams() {
        for(GameTeam team:teams){
            team.revive();
        }
    }

    private void handleNextRound(Timer timer) {

        timer.betweenRound();
    }

    private void handleGameEnd() {
        snowBallFight.stopGame();
        setRoundStatus(RoundStatus.FINISHED);
        for (GameTeam t : teams) {
            t.removeScoreboard();
        }
        GameTeam winnerCandidate = teams[0];
        for(int i = 1; i < teams.length; i++){
            if(teams[i].getWins() > winnerCandidate.getWins()) winnerCandidate = teams[i];
        }
        Bukkit.broadcastMessage("The winner is " + winnerCandidate.getColor() + winnerCandidate.getColor().name());

    }

    public RoundStatus getRoundStatus() {
        return status;
    }

    public void setRoundStatus(RoundStatus status) {
        this.status = status;
    }

    public Timer getTimer() {
        return timer;
    }

    public String getTimeString() {
        if (status == RoundStatus.BETWEEN) return "Round starts in: " + timer.getSecondsUntilRoundStart() + " seconds";
        else if (status == RoundStatus.RUNNING) return "Round ends in: " + timer.getSecondsUntilRoundEnd() + " seconds";
        return "error";
    }

    public Arena getArena() {
        return arena;
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
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].getPlayerCount() < min) {
                min = teams[i].getPlayerCount();
            }
        }
        if (min >= maxPlayers) return null;
        //get all teams with minimum playercount
        ArrayList<GameTeam> selectTeams = new ArrayList<>();
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].getPlayerCount() == min) {
                selectTeams.add(teams[i]);
            }

        }
        Random random = new Random();
        int index = random.nextInt(selectTeams.size());
        return selectTeams.get(index);
    }

    public boolean join(Player p) {
        GameTeam team = getTeamIdToJoin();
        if (team == null) return false;
        team.addPlayer(p);
        Bukkit.broadcastMessage("team " + teams[0].getColor() + ": " + teams[0].getPlayerCount() + ChatColor.RESET + " team " + teams[1].getColor() + ": " + teams[1].getPlayerCount());
        return true;
    }

    public boolean hasPlayer(Player p) {
        for (GamePlayer player : getPlayers()) {
            if (player != null && player.getPlayer().getUniqueId() == p.getUniqueId()) return true;
        }
        return false;
    }

    public void killPlayer(Player victim, Player killer) {
        Bukkit.broadcastMessage(victim.getDisplayName() + " was killed by " + killer.getDisplayName());
        GamePlayer gameVictim = getPlayer(victim);
        GamePlayer gamekiller = getPlayer(killer);
        gameVictim.die();
        gamekiller.addKill();

        roundWonCheck();
    }

    private void endOfRoundCheck() {
        int team1Players = teams[0].getAlivePlayers();
        int team2Players = teams[1].getAlivePlayers();
        Bukkit.broadcastMessage(" team 1 has " + team1Players + " players and team 2 has " + team2Players);
        if(team1Players == team2Players){
            Bukkit.broadcastMessage("It is a tie");
        }
        else if(team1Players > team2Players){
            Bukkit.broadcastMessage(teams[0].getColor() + teams[0].getColor().name() + " won!");
            teams[0].win();
            teams[1].lose();
        }
        else if(team2Players > team1Players){
            Bukkit.broadcastMessage(teams[1].getColor() + teams[1].getColor().name() + " won!");
            teams[0].win();
            teams[1].lose();
        }
    }

    private void roundWonCheck() {
        ArrayList<GameTeam> alive = new ArrayList<>();
        ArrayList<GameTeam> dead = new ArrayList<>();
        for (GameTeam team : teams) {
            if (team.getAlivePlayers() > 0) {
                alive.add(team);
            } else {
                dead.add(team);
            }
        }
        if (alive.size() == 1) {
//            alive.get(0).win();
//            dead.get(0).lose();
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

    public SnowBallFight getMain() {
        return snowBallFight;
    }
}
