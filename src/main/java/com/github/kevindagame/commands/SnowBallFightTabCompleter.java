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
        if (args.length == 1) {
            return mainHelp();
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    List<String> completion = new ArrayList<>();
                    completion.addAll(snowBallFight.getArenaHandler().getArenas().keySet());
                    return completion;
                case "arena":
                    return arenaHelp();
            }
        }

        if (args.length == 3) {
            if(args[0].equals("create")){
                List<String> completion = new ArrayList<>();
                completion.add("<rounds>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                List<String> completion = new ArrayList<>();
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
                List<String> completion = new ArrayList<>();
                completion.add("<minutes>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                if ("teams".equals(args[1])) {
                    if ("add".equals(args[2])) {
                        List<String> completion = new ArrayList<>();
                        completion.addAll(snowBallFight.getArenaHandler().getArenas().keySet());
                        return completion;
                    }
                }
            }
        }

        if (args.length == 5) {
            if(args[0].equals("create")){
                List<String> completion = new ArrayList<>();
                completion.add("<teamplayers>");
                return completion;
            }
            if ("arena".equals(args[0])) {
                if ("teams".equals(args[1])) {
                    if ("add".equals(args[2])) {
                        return getColors();
                    }
                }
            }
        }
        return null;
    }

    private List<String> getColors() {
        List<String> completion = new ArrayList<>();
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

    private List<String> arenaHelp() {
        List<String> completion = new ArrayList<>();
        completion.add("create");
        completion.add("remove");
        completion.add("list");
        completion.add("info");
        completion.add("teams");
        return completion;
    }

    private List<String> mainHelp() {
        List<String> completion = new ArrayList<String>();
        completion.add("help");
        completion.add("start");
        completion.add("create");
        completion.add("join");
        completion.add("arena");
        completion.add("stop");
        return completion;
    }
}
