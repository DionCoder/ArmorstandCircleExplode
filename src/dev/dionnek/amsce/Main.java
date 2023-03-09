package dev.dionnek.amsce;

import dev.dionnek.amsce.cmd.AmsceCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("amsce").setExecutor(new AmsceCommand(this));
        System.out.println("ArmorstandCircleExplode Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("ArmorstandCircleExplode Disabled!");
    }
}
