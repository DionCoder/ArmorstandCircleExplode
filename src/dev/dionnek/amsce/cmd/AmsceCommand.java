package dev.dionnek.amsce.cmd;


import dev.dionnek.amsce.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AmsceCommand implements CommandExecutor, Listener {
    private final Main plugin;
    public AmsceCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You cannot execute this command.");
            return false;
        }

        Player p = (Player) sender;
        Location center = p.getPlayer().getLocation();
        createExplodeCircle(center);
        return true;
    }

    private void createExplodeCircle(Location center) {
        double radius = 3;
        double increment = Math.PI / 5;
        List<ArmorStand> armorStands = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double angle = i * increment;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location location = new Location(center.getWorld(), x, center.getY(), z);
            ArmorStand armorStand = (ArmorStand) center.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(true);
            armorStands.add(armorStand);

            new BukkitRunnable() {
                int ticks = 0;

                @Override
                public void run() {
                    ticks++;
                    double y = center.getY() + (ticks * 0.5);
                    location.setY(y);
                    armorStand.teleport(location);

                    if (ticks >= 20) {
                        location.getWorld().createExplosion(location, 4);
                        armorStand.remove();
                        armorStands.remove(armorStand);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, i * 20, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStands.isEmpty()) {
                    cancel();
                    return;
                }

                ArmorStand armorStand = armorStands.get(0);
                Location location = armorStand.getLocation();
                location.getWorld().createExplosion(location, 4);
                armorStand.remove();
                armorStands.remove(armorStand);
            }
        }.runTaskTimer(plugin, 200, 20L);
    }
}
