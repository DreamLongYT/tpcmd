package dev.tpcmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class TpDelCommand implements CommandExecutor {

    private final TpCmdPlugin plugin;

    public TpDelCommand(TpCmdPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /tpdel <name>");
            return true;
        }

        String name = args[0];
        File file = new File(plugin.getDataFolder(), "tpcmd/" + name + ".txt");

        if (!file.exists()) {
            sender.sendMessage("Teleport point does not exist.");
            return true;
        }

        if (file.delete()) {
            sender.sendMessage("Teleport point '" + name + "' deleted.");
        } else {
            sender.sendMessage("Failed to delete teleport point.");
        }

        return true;
    }
}
