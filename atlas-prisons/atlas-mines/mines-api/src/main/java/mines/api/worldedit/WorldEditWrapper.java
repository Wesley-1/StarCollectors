package mines.api.worldedit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;
import worldedit.wrapper.v6.V6Wrapper;
import worldedit.wrapper.v7.V7Wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface WorldEditWrapper {

    static String getWorldEditVersion(JavaPlugin plugin) {
        if (plugin.getConfig().getString("VersionOfWorldEdit").contains("6")) {
            return "V6";
        } else {
            return "V7";
        }
    }

    static void create(JavaPlugin plugin) {
        if (getWorldEditVersion(plugin).contains("6")) {
            V6Wrapper.create(plugin);
        } else {
            V7Wrapper.create(plugin);
        }
    }
}
