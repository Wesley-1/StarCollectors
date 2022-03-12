package enchants.impl.service;

import enchants.impl.user.EnchantUser;
import me.elapsed.universal.AtlasComponent;
import me.elapsed.universal.database.json.serializer.Persist;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantDataService implements AtlasComponent {

    private static final transient EnchantDataService INSTANCE = new EnchantDataService();

    public static ConcurrentHashMap<UUID, EnchantUser> loadedUsers = new ConcurrentHashMap<>();

    private EnchantDataService() { }

    public static void load() {
        Persist.getInstance().loadOrSaveDefault(INSTANCE, EnchantDataService.class, "EnchantUserDB");
    }

    public static void save() {
        Persist.getInstance().save("EnchantUserDB");
    }
}