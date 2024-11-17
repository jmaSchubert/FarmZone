package org.Vertyx.farmZone.commands;

import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmzoneCommand implements CommandExecutor {
    private final FarmZoneManager manager;

    public FarmzoneCommand(FarmZoneManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0 && manager.getPlayerInfo(player) != null)
        {
            player.sendMessage("Telporting to Farmzone...");
            player.teleport(manager.getPlayerInfo(player).lastCoordinatesInFarmzone);
            return true;
        }

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

            case "delete":
                if (!args[1].isEmpty() && manager.deleteFarmzone(args[1]))
                {
                    sender.sendMessage("Farmzone deleted!");
                } else
                {
                    sender.sendMessage("Invalid command. Usage: /farmzone delete <Name>");
                    return false;
                }
                break;

            case "bossbarcolor":
                manager.setBossBarColor(BarColor.valueOf(args[1]));
                break;

            case "hidebossbar":
                manager.hideBossbar(player);
                break;

            case "showbossbar":
                manager.showBossbar(player);
                break;

            case "reset":
                manager.resetFarmzoneTimer(player);
                break;

            default:
                // send player to last saved position in farmzone
                // ask if their sure to teleport
                sender.sendMessage("Invalid command. Usage: /farmzone [create|delete|bossbarcolor|showbossbar|hidebossbar]");
                return false;
        }

        return true;
    }
}
