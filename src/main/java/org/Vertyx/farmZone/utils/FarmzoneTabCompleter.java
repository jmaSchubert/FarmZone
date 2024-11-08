package org.Vertyx.farmZone.utils;

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
        if (command.getName().equalsIgnoreCase("farmzone")) {
            if (args.length == 1) {

                suggestions.add("create");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {

                suggestions.add("<Name>");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {

                suggestions.add("10");
                suggestions.add("20");
                suggestions.add("50");
            }
        }


        return suggestions;
    }
}
