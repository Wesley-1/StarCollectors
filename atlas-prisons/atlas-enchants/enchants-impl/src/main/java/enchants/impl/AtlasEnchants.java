package enchants.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AtlasEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Test(), this);
    }

    @Override
    public void onDisable() {

    }

}
