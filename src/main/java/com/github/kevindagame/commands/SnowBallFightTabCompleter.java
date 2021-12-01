package com.github.kevindagame.commands;

import com.github.kevindagame.SnowBallFight;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

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
        if (args.length == 1) {
            completion.add("give");
            completion.add("help");
            completion.add("start");
            completion.add("create");
            completion.add("join");
            completion.add("arena");
            completion.add("stop");
            return completion;
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    completion.addAll(snowBallFight.getArenaHandler().getArenas().keySet());
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
            if(args[0].equals("create")){
                completion.add("<rounds>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                switch (args[1]) {
                    case "remove":
                        completion.addAll(snowBallFight.getArenaHandler().getArenas().keySet());
                        return completion;
                    case "info":
                    case "teams":
                        completion.add("add");
                        return completion;

                }
            }
        }
        if (args.length == 4) {
            if(args[0].equals("create")){
                completion.add("<minutes>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                if ("teams".equals(args[1])) {
                    if ("add".equals(args[2])) {
                        completion.addAll(snowBallFight.getArenaHandler().getArenas().keySet());
                        return completion;
                    }
                }
            }
        }

        if (args.length == 5) {
            if(args[0].equals("create")){
                completion.add("<teamplayers>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                if ("teams".equals(args[1])) {
                    if ("add".equals(args[2])) {
                        completion.add("WHITE");
                        completion.add("BLACK");
                        completion.add("RED");
                        completion.add("AQUA");
                        completion.add("BLUE");
                        completion.add("GOLD");
                        completion.add("GRAY");
                        completion.add("GREEN");
                        completion.add("YELLOW");

                        return completion;
                    }
                }
            }
        }
        return null;
    }
}
