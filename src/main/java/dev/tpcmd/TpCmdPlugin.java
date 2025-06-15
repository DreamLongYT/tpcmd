package dev.tpcmd;

import org.bukkit.plugin.java.JavaPlugin;

public class TpCmdPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("tpcreate").setExecutor(new TpCreateCommand(this));
        getCommand("tpdel").setExecutor(new TpDelCommand(this));
        getLogger().info("TpCmdPlugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("TpCmdPlugin disabled");
    }
}
