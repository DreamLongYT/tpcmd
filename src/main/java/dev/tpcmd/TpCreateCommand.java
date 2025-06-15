package dev.tpcmd;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TpCreateCommand implements CommandExecutor {

    private final TpCmdPlugin plugin;

    public TpCreateCommand(TpCmdPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /tpcreate <name>");
            return true;
        }

        String name = args[0];
        Location loc = player.getLocation();

        File folder = new File(plugin.getDataFolder(), "tpcmd");
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, name + ".txt");
        if (file.exists()) {
            sender.sendMessage("A teleport command with that name already exists.");
            return true;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(loc.getWorld().getName() + "," +
                         loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," +
                         loc.getYaw() + "," + loc.getPitch());
        } catch (IOException e) {
            sender.sendMessage("Error saving teleport location.");
            return true;
        }

        plugin.getCommand(name).setExecutor((s, c, l, a) -> {
            if (s instanceof Player target) {
                target.teleport(loc);
                target.sendMessage("Teleported to " + name + "!");
            }
            return true;
        });

        sender.sendMessage("Teleport point '" + name + "' created.");
        return true;
    }
}
