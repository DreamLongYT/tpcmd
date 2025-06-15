package dev.tpcmd;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TpCmdPlugin extends JavaPlugin implements CommandExecutor {

    private final Map<String, Location> teleportPoints = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("TpCmdPlugin enabled!");

        // Register commands
        getCommand("tpcreate").setExecutor(new TpCreateCommand(this));
        getCommand("tpdel").setExecutor(new TpDelCommand(this));
        getCommand("tpcmd").setExecutor(this); // Central handler

        // Load saved teleport points
        loadTeleportPoints();
    }

    @Override
    public void onDisable() {
        getLogger().info("TpCmdPlugin disabled.");
    }

    public void loadTeleportPoints() {
        File folder = new File(getDataFolder(), "tpcmd");
        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return;

        for (File file : files) {
            try {
                String name = file.getName().replace(".txt", "");
                String[] data = Files.readString(file.toPath()).split(",");
                Location loc = new Location(
                        getServer().getWorld(data[0]),
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]),
                        Float.parseFloat(data[4]),
                        Float.parseFloat(data[5])
                );
                teleportPoints.put(name.toLowerCase(), loc);
            } catch (IOException | NullPointerException | NumberFormatException e) {
                getLogger().warning("Failed to load teleport point: " + file.getName());
            }
        }
    }

    public Location getTeleportLocation(String name) {
        return teleportPoints.get(name.toLowerCase());
    }

    public void registerTeleportPoint(String name, Location loc) {
        teleportPoints.put(name.toLowerCase(), loc);
    }

    public void unregisterTeleportPoint(String name) {
        teleportPoints.remove(name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can teleport.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /tpcmd <name>");
            return true;
        }

        Location loc = getTeleportLocation(args[0]);
        if (loc == null) {
            player.sendMessage("Teleport point not found.");
            return true;
        }

        player.teleport(loc);
        player.sendMessage("Teleported to " + args[0] + ".");
        return true;
    }
}
