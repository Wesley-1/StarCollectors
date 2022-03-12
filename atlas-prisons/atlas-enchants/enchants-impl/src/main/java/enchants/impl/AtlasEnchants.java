package enchants.impl;

import enchants.impl.service.EnchantDataService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AtlasEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        doLoad();
        Bukkit.getPluginManager().registerEvents(new Test(), this);
    }

    @Override
    public void onDisable() {
        doSave();
    }

    public void doLoad() {
        EnchantDataService.load();
    }

    public void doSave() {
        EnchantDataService.save();
    }

}
