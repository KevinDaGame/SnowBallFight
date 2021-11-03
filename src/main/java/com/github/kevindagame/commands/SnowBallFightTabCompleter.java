package com.github.kevindagame.commands;

import com.github.kevindagame.SnowBallFight;
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
        if(args.length == 1){
            completion.add("give");
            completion.add("start");
            return completion;
        }
        return null;
    }
}
