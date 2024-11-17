package org.Vertyx.farmZone.commands;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    FarmZoneManager manager;

    public HomeCommand(FarmZoneManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (manager.getPlayerInfo(player) != null)
        {
            Location homezoneCenter = manager.getFirstHomezone().getCenter();
            if(homezoneCenter == null)
            {
                player.sendMessage("Can't return home, because there is none");
                return false;
            }

            player.sendMessage("Returning Home");
            player.teleport(homezoneCenter);
            return true;
        }

        return false;
    }
}
