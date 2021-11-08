package com.github.kevindagame.commands;

import com.github.kevindagame.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SnowBallFightCommand implements CommandExecutor {
    private final SnowBallFight snowBallFight;

    public SnowBallFightCommand(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            showHelp(commandSender);
        }
        switch (args[0]) {
            case "create":
                if (args.length == 1) {
                    commandSender.sendMessage("You did not specify any arguments, specify at least an arena!");
                    return true;
                }
                if (args.length == 2) {
                    Arena arena = snowBallFight.getArenas().get(args[1].toLowerCase());
                    if (arena != null) {
                        snowBallFight.setGame(new Game(snowBallFight, arena));
                        commandSender.sendMessage("succesfully created game");
                        return true;
                    } else {
                        commandSender.sendMessage("There is no arena with that name!");
                        return true;
                    }
                }
            case "start":
                if (snowBallFight.getGame() != null) {
                    if (snowBallFight.getGame().getRoundStatus() == RoundStatus.STARTING) {
                        if (snowBallFight.getGame().getPlayers().size() >= 2) {
                            snowBallFight.getGame().start();
                            return true;

                        }
                        commandSender.sendMessage("This game needs 2 players to start");
                        return true;
                    }
                    commandSender.sendMessage("Sorry, this game has already been started");
                    return true;
                }
                commandSender.sendMessage("You need to create a game first before you can start it!");
                return true;
            case "give":
                if (commandSender instanceof Player) {
                    ItemStack snowball = new ItemStack(Material.SNOWBALL, 1);
                    ((Player) commandSender).getInventory().addItem(snowball);

                }
                return true;
            case "join":
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    if (snowBallFight.getGame() == null) {
                        commandSender.sendMessage("There is no current game!");
                        return true;
                    }
                    if (snowBallFight.getGame().hasPlayer(p)) {
                        commandSender.sendMessage("you already joined you dumbass");
                        return true;
                    }
                    if (!snowBallFight.getGame().join(p)) {
                        commandSender.sendMessage("Sorry, the game is full!");
                        return true;
                    }
                    commandSender.sendMessage("Successfully joined game!");
                    return true;

                }
            case "arena":
                if (args.length == 1) {
                    showArenaHelp(commandSender);
                    return true;
                }
                switch (args[1]) {
                    case "create":
                        if (args.length < 5) {
                            commandSender.sendMessage("You are missing an argument! Correct usage:");
                            showArenaCreateHelp(commandSender);
                            return true;
                        } else if (args.length > 5) {
                            commandSender.sendMessage("You have given too many arguments! Correct usage:");
                            showArenaCreateHelp(commandSender);
                            return true;
                        }
                        World world = Bukkit.getWorld(args[3]);
                        if (world == null) {
                            commandSender.sendMessage("There is no world with this name!");
                            return true;
                        }
                        if (snowBallFight.getArenas().containsKey(args[2].toLowerCase())) {
                            commandSender.sendMessage("There is already an arena with that name!");
                            return true;
                        }
                        if (!snowBallFight.getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).hasRegion(args[4])) {
                            commandSender.sendMessage("There is no region with this name!");
                            return true;
                        }
                        if (snowBallFight.addArena(args[2].toLowerCase(), world, args[4])) {
                            commandSender.sendMessage("successfully created arena with name: " + args[2].toLowerCase());
                            return true;
                        }
                    case "teams":
                        if(args[2] != null){
                            switch(args[2]){
                                case "add":
                                    if(!(commandSender instanceof Player)){
                                        commandSender.sendMessage("Sorry, only players can create teams");
                                        return true;
                                    }
                                    if(args.length == 5){
                                        Location l = ((Player) commandSender).getLocation();
                                        snowBallFight.addTeam(args[3], new Team(args[4], new SpawnPoint(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName())));
                                        return true;
                                    }
                                    else{
                                        commandSender.sendMessage("You gave incorrect arguments!");
                                    }
                            }
                        }
                        else{
                            commandSender.sendMessage("you need to provide an argument!");
                        }
                    case "remove":
                        if (args.length == 2) {
                            commandSender.sendMessage("You need to specify an arena name!");
                            return true;
                        } else if (args.length > 3) {
                            commandSender.sendMessage("You have given too many arguments!");
                            return true;
                        }
                        if (!snowBallFight.getArenas().containsKey(args[2].toLowerCase())) {
                            commandSender.sendMessage("There is no arena with that name!");
                            return true;
                        }
                        snowBallFight.removeArena(args[2]);
                        commandSender.sendMessage("Successfully removed arena " + args[2].toLowerCase());
                        return true;
                    case "list":
                        sendArenasList(commandSender);
                        return true;
                    case "info":
                        if (args.length == 2) {
                            commandSender.sendMessage("You need to specify an arena name!");
                            return true;
                        } else if (args.length > 3) {
                            commandSender.sendMessage("You have given too many arguments!");
                            return true;
                        }
                        if (!snowBallFight.getArenas().containsKey(args[2].toLowerCase())) {
                            commandSender.sendMessage("There is no arena with that name!");
                            return true;
                        }
                        sendArenaInfo(args[2], commandSender);
                        return true;

                }

        }
        return true;
    }

    private void sendArenaInfo(String arenaName, CommandSender commandSender) {
        Arena arena = snowBallFight.getArenas().get(arenaName);
        commandSender.sendMessage("Name: " + arenaName);
        commandSender.sendMessage("World: " + arena.getWorld());
        commandSender.sendMessage("Region: " + arena.getRegion());
        for (Team s : arena.getSpawns()) {
            commandSender.sendMessage(s.getSpawnPoint().toString());
        }

    }

    private void sendArenasList(CommandSender commandSender) {
        for (String arenaName : snowBallFight.getArenas().keySet()) {
            commandSender.sendMessage(arenaName);
        }
    }

    private void showArenaCreateHelp(CommandSender commandSender) {
    }

    private void showHelp(CommandSender commandSender) {
    }

    private void showArenaHelp(CommandSender commandSender) {

    }
}
