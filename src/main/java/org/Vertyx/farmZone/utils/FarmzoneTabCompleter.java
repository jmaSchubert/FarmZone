package org.Vertyx.farmZone.utils;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.Vertyx.farmZone.models.HomeZoneModel;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FarmzoneTabCompleter implements TabCompleter {

    FarmZoneManager manager;

    public FarmzoneTabCompleter(FarmZoneManager manager)
    {
        this.manager = manager;
    }

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
//            suggestions.add("create");
//            suggestions.add("delete");
            suggestions.add("bossbarcolor");
            suggestions.add("showbossbar");
            suggestions.add("hidebossbar");
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
        else if (args[0].equalsIgnoreCase("delete"))
        {
            for (HomeZoneModel homezone : manager.getActiveHomezones().values())
            {
                suggestions.add(homezone.getName());
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
