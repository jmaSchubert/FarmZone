package org.Vertyx.farmZone.utils;

import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FarmzoneTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();



        // Only accessable by players
        if (!(sender instanceof Player)) {
            return suggestions;
        }

        // Check if executed Command was /farmzone
        if (args[0].isEmpty())
        {
            suggestions.add("create");
            suggestions.add("bossbarcolor");
        }
        else if (args[0].equalsIgnoreCase("create")) {
            switch (args.length) {
                case 1:
                    suggestions.add("create");
                    break;
                case 2:
                    suggestions.add("<Name>");
                    break;
                case 3:
                    suggestions.add("10");
                    suggestions.add("20");
                    suggestions.add("50");
                    break;
            }
        }
        else if (args[0].equalsIgnoreCase("bossbarcolor"))
        {
            for (BarColor color : BarColor.values())
            {
                suggestions.add(String.valueOf(color));
            }
        }


        return suggestions;
    }
}
