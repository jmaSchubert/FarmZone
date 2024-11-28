package org.Vertyx.farmzone.commands;

import org.Vertyx.farmzone.managers.FarmzoneManager;
import org.Vertyx.farmzone.models.HomezoneModel;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HomeCommand implements CommandExecutor {
    FarmzoneManager manager;
    Plugin plugin;

    public HomeCommand(FarmzoneManager manager, Plugin plugin)
    {
        this.manager = manager;
        this.plugin = plugin;
    }

    private void playParticlesAndSounds(Player player, int ticks) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        double radius = 1.0;
        double x = radius * Math.cos(2 * Math.PI * ticks / 60);
        double z = radius * Math.sin(2 * Math.PI * ticks / 60);
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(x, ticks * 0.05, z), 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Location locationBeforeTeleport = player.getLocation();

        if (manager.getPlayerInfo(player) != null && manager.locationInFarmzone(locationBeforeTeleport))
        {
            HomezoneModel homezone = manager.getFirstHomezone();
            if(homezone == null)
            {
                player.sendMessage("[!] Can't return home, because there is none");
                return false;
            }

            player.sendMessage("[!] Returning Home in 3 seconds. Please stand still...");
            Location homeLocation = homezone.getPreferredHome(manager.getPlayerInfo(player));

            for (int i = 0; i < 60; i++) {
                int ticks = i;
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> playParticlesAndSounds(player, ticks), i);
            }


            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.getLocation().getX() == locationBeforeTeleport.getX() &&
                        player.getLocation().getY() == locationBeforeTeleport.getY() &&
                        player.getLocation().getZ() == locationBeforeTeleport.getZ()) {
                    player.teleport(homeLocation);
                } else {
                    player.sendMessage("[!] You moved! Teleportation cancelled.");
                }
            }, 60L); // 60 ticks = 3 seconds
            return true;
        }

        return false;
    }
}
