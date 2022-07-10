package collectors.services;

import collectors.data.serializer.Persist;
import collectors.records.Collector;
import org.bukkit.Location;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataService
{
    private static final transient DataService INSTANCE = new DataService();

    public static ConcurrentHashMap<Location, Collector> loadedCollectors = new ConcurrentHashMap<>();

    public DataService() {}

    public static void load() { Persist.get().loadOrSaveDefault(INSTANCE, DataService.class, "CollectorData"); }
    public static void save() { Persist.get().save(INSTANCE, "CollectorData"); }

}
