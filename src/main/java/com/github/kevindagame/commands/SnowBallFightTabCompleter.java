package com.github.kevindagame.commands;

import com.github.kevindagame.SnowBallFight;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SnowBallFightTabCompleter implements TabCompleter {
    private final SnowBallFight snowBallFight;

    public SnowBallFightTabCompleter(SnowBallFight snowBallFight) {
        this.snowBallFight = snowBallFight;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completion = new ArrayList<>();
        Player p = (Player) commandSender;
        if (args.length == 1) {
            completion.add("give");
            completion.add("start");
            completion.add("create");
            completion.add("join");
            completion.add("arena");
            return completion;
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    completion.addAll(snowBallFight.getArenas().keySet());
                    return completion;
                case "arena":
                    completion.add("create");
                    completion.add("remove");
                    completion.add("list");
                    completion.add("info");
                    completion.add("teams");
                    return completion;
            }
        }

        if (args.length == 3) {
            switch (args[0]) {
                case "arena":
                    switch (args[1]) {
                        case "remove":
                        case "info":
                        case "teams":
                            completion.add("add");
                            return completion;

                    }
            }
        }
        if (args.length == 4) {
            switch (args[0]) {
                case "arena":
                    switch (args[1]) {
                        case "teams":
                            switch (args[2]) {
                                case "add":
                                    completion.addAll(snowBallFight.getArenas().keySet());
                                    return completion;
                            }
                    }
            }
        }

        if (args.length == 5) {
            switch (args[0]) {
                case "arena":
                    switch (args[1]) {
                        case "teams":
                            switch (args[2]) {
                                case "add":
                                    for (ChatColor color : ChatColor.values()) {
                                        completion.add(color.name());
                                    }
                                    return completion;
                            }
                    }
            }
        }
        return null;
    }
}
