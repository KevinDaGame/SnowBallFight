package com.github.kevindagame.commands;

import com.github.kevindagame.Arena;
import com.github.kevindagame.Game;
import com.github.kevindagame.SnowBallFight;
import com.github.kevindagame.SpawnPoint;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
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
            case "start":
                if (args.length == 1) {
                    commandSender.sendMessage("You did not specify any arguments, specify at least an arena!");
                }
                if (args.length == 2) {
                    Arena arena = snowBallFight.getArenas().get(args[1].toLowerCase());
                    if (arena != null) {
                        snowBallFight.setGame(new Game(snowBallFight, arena));
                    } else {
                        commandSender.sendMessage("There is no arena with that name!");
                    }
                }
                return true;
            case "give":
                if (commandSender instanceof Player) {
                    ItemStack snowball = new ItemStack(Material.SNOWBALL, 1);
                    ((Player) commandSender).getInventory().addItem(snowball);

                }
            case "join":
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    if(snowBallFight.getGame().hasPlayer(p)){
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
        for (SpawnPoint s : arena.getSpawns()) {
            commandSender.sendMessage(s.toString());
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
