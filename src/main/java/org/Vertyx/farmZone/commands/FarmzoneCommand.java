package org.Vertyx.farmZone.commands;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmzoneCommand implements CommandExecutor {
    private FarmZoneManager manager;

    public FarmzoneCommand(FarmZoneManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        switch (args[0]) {
            case "create":
                // check if the command has the correct number of arguments
                if (args.length != 3) {
                    sender.sendMessage("Invalid number of arguments. Usage: /farmzone create <Name> <radius>");
                    return false;
                }
                // check if the radius is a number
                try {
                    double radius = Double.parseDouble(args[2]);
                    // create the farmzone
                    manager.createFarmZone(args[1], player.getLocation(), radius);
                    sender.sendMessage("Farmzone created successfully.");
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid radius. Usage: /farmzone create <Name> <radius>");
                    return false;
                }
                break;
            default:
                sender.sendMessage("Invalid command. Usage: /farmzone create <Name> <radius>");
                return false;
        }

        return true;
    }
}
