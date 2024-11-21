package org.Vertyx.farmZone.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.Vertyx.farmZone.managers.FarmZoneManager;
import org.bukkit.Location;
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

    private void showHelp(Player player) {
        player.sendMessage(Component.text("Farmzone Commands:").color(NamedTextColor.YELLOW));
        player.sendMessage(Component.text("/home").color(NamedTextColor.GOLD)
                .append(Component.text(" - Teleports you to the your preferred home or the default location of the Homezone.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone").color(NamedTextColor.GOLD)
                .append(Component.text(" - Teleports you to your last position in the Farmzone.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone sethome").color(NamedTextColor.GOLD)
                .append(Component.text(" - Sets a personal location for /home or exceeded Farmzone time.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone setdefault").color(NamedTextColor.GOLD)
                .append(Component.text(" - Sets a default location for /home or exceeded Farmzone time for every player.").color(NamedTextColor.WHITE)));
//        player.sendMessage(Component.text("/farmzone create <Name> <radius>").color(NamedTextColor.GOLD)
//                .append(Component.text(" - Creates a new Farmzone with the given name and radius.").color(NamedTextColor.WHITE)));
//        player.sendMessage(Component.text("/farmzone delete <Name>").color(NamedTextColor.GOLD)
//                .append(Component.text(" - Deletes the Farmzone with the given name.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone bossbarcolor <color>").color(NamedTextColor.GOLD)
                .append(Component.text(" - Sets the color of the boss bar.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone showbossbar").color(NamedTextColor.GOLD)
                .append(Component.text(" - Shows the boss bar.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone hidebossbar").color(NamedTextColor.GOLD)
                .append(Component.text(" - Hides the boss bar.").color(NamedTextColor.WHITE)));
//        player.sendMessage(Component.text("/farmzone reset").color(NamedTextColor.GOLD)
//                .append(Component.text(" - Resets the Farmzone timer.").color(NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/farmzone help").color(NamedTextColor.GOLD)
                .append(Component.text(" - Shows this help message.").color(NamedTextColor.WHITE)));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Location currentPlayerLocation = player.getLocation();

        if(args.length == 0 && manager.getPlayerInfo(player) != null)
        {
            player.sendMessage("[!] Telporting to Farmzone...");
            player.teleport(manager.getPlayerInfo(player).lastCoordinatesInFarmzone);
            return true;
        }

        switch (args[0]) {
            case "create":
                // check if issuer has necessary permissions
                if (!player.hasPermission("minecraft.command.gamemode"))
                {
                    player.sendMessage("[!] You do not have the permission to execute this command!");
                    return false;
                }

                // check if the command has the correct number of arguments
                if (args.length != 3) {
                    sender.sendMessage("[!] Invalid number of arguments. Usage: /farmzone create <Name> <radius>");
                    return false;
                }
                // check if the radius is a number
                try {
                    double radius = Double.parseDouble(args[2]);
                    // create the farmzone
                    manager.createFarmZone(args[1], currentPlayerLocation, radius);
                    sender.sendMessage("[!] Farmzone created successfully.");
                } catch (NumberFormatException e) {
                    sender.sendMessage("[!] Invalid radius. Usage: /farmzone create <Name> <radius>");
                    return false;
                }
                break;

            case "delete":
                if (!player.hasPermission("minecraft.command.gamemode"))
                {
                    player.sendMessage("[!] You do not have the permission to execute this command!");
                    return false;
                }

                if (!args[1].isEmpty() && manager.deleteFarmzone(args[1]))
                {
                    sender.sendMessage("[!] Farmzone deleted!");
                } else
                {
                    sender.sendMessage("[!] Invalid command. Usage: /farmzone delete <Name>");
                    return false;
                }
                break;

            case "reset":
                if (!player.hasPermission("minecraft.command.gamemode"))
                {
                    player.sendMessage("[!] You do not have the permission to execute this command!");
                    return false;
                }
                manager.resetFarmzoneTimer(player);
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

            case "sethome":
            case "setHome":
                // check if Location is even in the homezone
                if (!manager.locationInFarmzone(currentPlayerLocation))
                {
                    manager.getFirstHomezone().setDefaultHomeLocation(currentPlayerLocation);
                    player.sendMessage("[!] Set home location to current position!");
                    return true;
                } else
                {
                    player.sendMessage("[!] You're not in the homezone though :shrug:");
                }

                break;

            case "setdefault":
                if (!player.hasPermission("minecraft.command.gamemode"))
                {
                    player.sendMessage("[!] You do not have the permission to execute this command!");
                    return false;
                }

                if (!manager.locationInFarmzone(currentPlayerLocation))
                {
                    manager.getPlayerInfo(player).preferredHomeLocation = currentPlayerLocation;
                    player.sendMessage("[!] Set default home location to current position!");
                    return true;
                } else
                {
                    player.sendMessage("You're not in the homezone though :shrug:");
                }

                manager.getFirstHomezone().setDefaultHomeLocation(currentPlayerLocation);
                break;

            case "distance":
                player.sendMessage("[!] Distanz zum Mittelpunkt: " + manager.getDistance(player));
                break;

            case "help":
                player.sendMessage("Showing help for farmzone commands: ");

            default:
                showHelp(player);
                return false;
        }

        return true;
    }
}
