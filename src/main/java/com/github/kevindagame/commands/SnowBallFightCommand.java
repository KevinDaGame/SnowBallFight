package com.github.kevindagame.commands;

import com.github.kevindagame.ArenaHandler;
import com.github.kevindagame.Game;
import com.github.kevindagame.Language.Lang;
import com.github.kevindagame.Model.Arena;
import com.github.kevindagame.Model.SpawnPoint;
import com.github.kevindagame.Model.Team;
import com.github.kevindagame.RoundStatus;
import com.github.kevindagame.SnowBallFight;
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
            return true;
        }
        switch (args[0]) {
            case "help":
                showHelp(commandSender);
                return true;
            case "create":
                if (args.length == 1) {
                    Lang.sendMessage(commandSender, "You did not specify any arguments, specify at least an arena!");
                    showDocs(commandSender);
                    return true;
                }
                if (args.length == 2) {
                    Arena arena = snowBallFight.getArenaHandler().getArenas().get(args[1].toLowerCase());
                    if (arena != null) {
                        snowBallFight.setGame(new Game(snowBallFight, arena, snowBallFight.getPluginConfig()));
                        Lang.sendMessage(commandSender, "succesfully created game");
                    } else {
                        Lang.sendMessage(commandSender, "There is no arena with that name!");
                    }
                    return true;
                }
                if (args.length == 5) {
                    Arena arena = snowBallFight.getArenaHandler().getArenas().get(args[1].toLowerCase());
                    if (arena != null) {
                        if (Integer.parseInt(args[2]) > 0 && Integer.parseInt(args[2]) < 20) {
                            if (Integer.parseInt(args[3]) > 0 && Integer.parseInt(args[3]) < 60) {
                                if (Integer.parseInt(args[4]) > 0 && Integer.parseInt(args[4]) < 10) {

                                    snowBallFight.setGame(new Game(snowBallFight, arena, snowBallFight.getPluginConfig(), Integer.parseInt(args[2]), Integer.parseInt(args[3]) * 60, Integer.parseInt(args[4])));
                                } else {
                                    Lang.sendMessage(commandSender, "A team can have a maximum of 10 players");
                                }
                            } else {
                                Lang.sendMessage(commandSender, "A round can't be longer then one hour");
                            }
                        } else {
                            Lang.sendMessage(commandSender, "You can only make 20 rounds!");
                        }
                        Lang.sendMessage(commandSender, "succesfully created game");
                    } else {
                        Lang.sendMessage(commandSender, "There is no arena with that name!");
                    }
                    return true;
                }
            case "start":
                if (snowBallFight.getGame() != null) {
                    if (snowBallFight.getGame().getRoundStatus() == RoundStatus.STARTING) {
                        if (snowBallFight.getGame().getPlayers().size() >= 2) {
                            snowBallFight.getGame().start();
                            return true;

                        }
                        Lang.sendMessage(commandSender, "This game needs 2 players to start");
                        return true;
                    }
                    Lang.sendMessage(commandSender, "Sorry, this game has already been started");
                    return true;
                }
                Lang.sendMessage(commandSender, "You need to create a game first before you can start it!");
                showDocs(commandSender);
                return true;
            case "stop":
                if (snowBallFight.getGame() != null) {
                    snowBallFight.setGame(null);
                }
                return true;
            case "join":
                Player p = null;
                if (commandSender instanceof Player) {
                    p = (Player) commandSender;
                } else if (args.length == 2) {
                    Player temp = Bukkit.getServer().getPlayer(args[1]);
                    if (temp != null) {
                        p = temp;
                    }
                }
                if (p == null) {
                    Lang.sendMessage(commandSender, "Could not find player");
                    return true;
                }
                if (snowBallFight.getGame() == null) {
                    Lang.sendMessage(p, "There is no current game!");
                    return true;
                }
                if (snowBallFight.getGame().hasPlayer(p)) {
                    Lang.sendMessage(p, "you already joined this game!");
                    return true;
                }
                if (!p.getInventory().isEmpty()) {
                    Lang.sendMessage(p, "You can only join with an empty inventory!");
                    return true;
                }
                if (!snowBallFight.getGame().join(p)) {
                    Lang.sendMessage(p, "Sorry, the game is full!");
                    return true;
                }
                Lang.sendMessage(p, "Successfully joined game!");
                return true;

        case "arena":
        if (args.length == 1) {
            showDocs(commandSender);
            return true;
        }
        ArenaHandler arenaHandler = snowBallFight.getArenaHandler();
        switch (args[1]) {
            case "create":
                if (args.length < 5) {
                    Lang.sendMessage(commandSender, "You are missing an argument! Correct usage:");
                    showArenaCreateHelp(commandSender);
                    return true;
                } else if (args.length > 5) {
                    Lang.sendMessage(commandSender, "You have given too many arguments! Correct usage:");
                    showArenaCreateHelp(commandSender);
                    return true;
                }
                World world = Bukkit.getWorld(args[3]);
                if (world == null) {
                    Lang.sendMessage(commandSender, "There is no world with this name!");
                    return true;
                }
                if (arenaHandler.getArenas().containsKey(args[2].toLowerCase())) {
                    Lang.sendMessage(commandSender, "There is already an arena with that name!");
                    return true;
                }
                if (!snowBallFight.getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).hasRegion(args[4])) {
                    Lang.sendMessage(commandSender, "There is no region with this name!");
                    return true;
                }
                if (arenaHandler.addArena(args[2].toLowerCase(), world, args[4])) {
                    Lang.sendMessage(commandSender, "successfully created arena with name: " + args[2].toLowerCase());
                    return true;
                }
            case "teams":
                if (args[2] != null) {
                    switch (args[2]) {
                        case "add":
                            if (!(commandSender instanceof Player)) {
                                Lang.sendMessage(commandSender, "Sorry, only players can create teams");
                                return true;
                            }
                            if (args.length == 5) {
                                Location l = ((Player) commandSender).getLocation();
                                arenaHandler.addTeam(args[3], new Team(args[4], new SpawnPoint(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getPitch(), l.getYaw(), l.getWorld().getName())));
                                Lang.sendMessage(commandSender, "succesfully created team with color " + args[4] + " at x=" + l.getBlockX() + " y=" + l.getBlockY() + " z=" + l.getBlockZ() + " with pitch=" + l.getPitch() + " and yaw=" + l.getYaw());
                                return true;
                            } else {
                                Lang.sendMessage(commandSender, "You gave incorrect arguments!");
                            }
                    }
                } else {
                    Lang.sendMessage(commandSender, "you need to provide an argument!");
                }
            case "remove":
                if (args.length == 2) {
                    Lang.sendMessage(commandSender, "You need to specify an arena name!");
                    return true;
                } else if (args.length > 3) {
                    Lang.sendMessage(commandSender, "You have given too many arguments!");
                    return true;
                }
                if (!arenaHandler.getArenas().containsKey(args[2].toLowerCase())) {
                    Lang.sendMessage(commandSender, "There is no arena with that name!");
                    return true;
                }
                arenaHandler.removeArena(args[2]);
                Lang.sendMessage(commandSender, "Successfully removed arena " + args[2].toLowerCase());
                return true;
            case "list":
                sendArenasList(arenaHandler, commandSender);
                return true;
            case "info":
                if (args.length == 2) {
                    Lang.sendMessage(commandSender, "You need to specify an arena name!");
                    return true;
                } else if (args.length > 3) {
                    Lang.sendMessage(commandSender, "You have given too many arguments!");
                    return true;
                }
                if (!arenaHandler.getArenas().containsKey(args[2].toLowerCase())) {
                    Lang.sendMessage(commandSender, "There is no arena with that name!");
                    return true;
                }
                sendArenaInfo(arenaHandler, args[2], commandSender);
                return true;

        }

    }
        return true;
}

    private void sendArenaInfo(ArenaHandler arenaHandler, String arenaName, CommandSender commandSender) {
        Arena arena = arenaHandler.getArenas().get(arenaName);
        Lang.sendMessage(commandSender, "Name: " + arenaName);
        Lang.sendMessage(commandSender, "World: " + arena.getWorld());
        Lang.sendMessage(commandSender, "Region: " + arena.getRegion());
        for (Team s : arena.getSpawns()) {
            Lang.sendMessage(commandSender, s.getSpawnPoint().toString());
        }

    }

    private void sendArenasList(ArenaHandler arenaHandler, CommandSender commandSender) {
        for (String arenaName : arenaHandler.getArenas().keySet()) {
            Lang.sendMessage(commandSender, arenaName);
        }
    }

    private void showArenaCreateHelp(CommandSender commandSender) {
        Lang.sendMessage(commandSender, "/sbf arena create <name> <world> <region>");
    }

    private void showHelp(CommandSender commandSender) {

        showDocs(commandSender);
        Lang.sendMessage(commandSender, "Command help:");
        Lang.sendMessage(commandSender, "&7/sbf arena &f - Create or edit arena's");
        Lang.sendMessage(commandSender, "&7/sbf create &f - Create a new game");
        Lang.sendMessage(commandSender, "&7/sbf join &f - Join the current game");
        Lang.sendMessage(commandSender, "&7/sbf start &f - Start the game after creating");
        Lang.sendMessage(commandSender, "&7/sbf stop &f - Stop a created game");
        Lang.sendMessage(commandSender, "&7/sbf help &f - Show this menu");
    }

    private void showDocs(CommandSender commandSender) {
        Lang.sendMessage(commandSender, "Documentation: https://docs.google.com/document/d/1krdCcdG6e2IyK7Z1XVgQv7FJ21heJud2pzx0iiO0dJI/edit?usp=sharing");

    }
}
