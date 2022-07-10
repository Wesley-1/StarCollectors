package collectors.data.serializer;

import collectors.CollectorsAPI;
import collectors.data.typeadapter.ColorTypeAdapter;
import collectors.data.typeadapter.EnumTypeAdapter;
import collectors.data.typeadapter.ItemStackTypeAdapter;
import collectors.data.typeadapter.LocationTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Persist {
    private static Persist instance;
    private final JavaPlugin api;
    private final Gson gson = this.buildGson().create();

    public Persist() {
        instance = this;
        this.api = CollectorsAPI.get();
        File folder = new File(api.getDataFolder() + "/databases");
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object o) {
        return getName(o.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    private GsonBuilder buildGson() {
        return (
                new GsonBuilder())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY)
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .registerTypeAdapter(Location.class, new LocationTypeAdapter())
                .registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter())
                .enableComplexMapKeySerialization().excludeFieldsWithModifiers(128, 64);
    }

    public File getFile(String name) {
        return new File(api.getDataFolder() + "/databases", name + ".json");
    }

    public File getFile(Class<?> clazz) {
        return this.getFile(getName(clazz));
    }

    public File getFile(Object obj) {
        return this.getFile(getName(obj));
    }

    public File getFile(Type type) {
        return this.getFile(getName(type));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz) {
        return this.loadOrSaveDefault(def, clazz, this.getFile(clazz));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, String name) {
        return this.loadOrSaveDefault(def, clazz, this.getFile(name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, File file) {
        if (!file.exists()) {
            Bukkit.getLogger().info("Creating default: " + file);
            this.save(def, file);
            return def;
        } else {
            T loaded = this.load(clazz, file);
            if (loaded == null) {
                Bukkit.getLogger().warning("Using default as I failed to load: " + file);
                File backup = new File(file.getPath() + "_bad");
                if (backup.exists()) {
                    backup.delete();
                }

                Bukkit.getLogger().warning("Backing up copy of bad file to: " + backup);
                file.renameTo(backup);
                return def;
            } else {
                return loaded;
            }
        }
    }

    public boolean save(Object instance) {
        return this.save(instance, this.getFile(instance));
    }

    public boolean save(Object instance, String name) {
        return this.save(instance, this.getFile(name));
    }

    public boolean save(Object instance, File file) {
        return DiscUtil.writeCatch(file, this.gson.toJson(instance), true);
    }

    public <T> T load(Class<T> clazz) {
        return this.load(clazz, this.getFile(clazz));
    }

    public <T> T load(Class<T> clazz, String name) {
        return this.load(clazz, this.getFile(name));
    }

    public <T> T load(Class<T> clazz, File file) {
        String content = DiscUtil.readCatch(file);
        if (content == null) {
            return null;
        } else {
            try {
                return this.gson.fromJson(content, clazz);
            } catch (Exception var5) {
                Bukkit.getLogger().warning(var5.getMessage());
                return null;
            }
        }
    }

    public <T> T load(Type typeOfT, String name) {
        return this.load(typeOfT, this.getFile(name));
    }

    public <T> T load(Type typeOfT, File file) {
        String content = DiscUtil.readCatch(file);
        if (content == null) {
            return null;
        } else {
            try {
                return this.gson.fromJson(content, typeOfT);
            } catch (Exception var5) {
                Bukkit.getLogger().warning(var5.getMessage());
                return null;
            }
        }
    }

    public static Persist getInstance() {
        return instance;
    }
}

