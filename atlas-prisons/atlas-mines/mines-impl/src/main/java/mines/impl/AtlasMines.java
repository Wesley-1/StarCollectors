package mines.impl;

import mines.impl.mine.data.MineData;
import org.bukkit.plugin.java.JavaPlugin;

public class AtlasMines extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        MineData.load();
    }

    @Override
    public void onDisable() {
        MineData.save();
    }
}
