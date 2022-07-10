package collectors;

import collectors.exceptions.CollectorCreationException;
import collectors.records.Collector;
import collectors.services.DataService;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class CollectorsAPI {

    private static JavaPlugin plugin;

    /**
     *
     * @return returns a hashmap that stores collectors, and locations.
     *
     */
    public static ConcurrentHashMap<Location, Collector> getService() {
        return DataService.loadedCollectors;
    }

    /**
     *
     * @return returns the main class.
     *
     */
    public static JavaPlugin get() {
        if (plugin == null) {
            throw new CollectorCreationException("CollectorsAPI not binded.");
        }
        return plugin;
    }

    /**
     *
     * @param clazz The main class that will be binded to the collections class.
     *
     * @return returns the {@link CollectorsAPI} class.
     */
    public CollectorsAPI bind(Class<? extends JavaPlugin> clazz) {
        plugin = JavaPlugin.getProvidingPlugin(clazz);

        File dataFolder = new File(plugin.getDataFolder().getPath(), "DB");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        DataService.load();

        return this;
    }
}
