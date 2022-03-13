package mines.impl.mine.data;

import me.elapsed.universal.AtlasComponent;
import me.elapsed.universal.database.json.serializer.Persist;
import mines.api.mines.AtlasMine;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MineData implements AtlasComponent {
    private static final transient MineData INSTANCE = new MineData();

    public static ConcurrentHashMap<UUID, AtlasMine> loadedMines = new ConcurrentHashMap<>();

    private MineData() {}

    public static void load() { Persist.getInstance().loadOrSaveDefault(INSTANCE, MineData.class, "MinesDatabase"); }
    public static void save() {
        Persist.getInstance().save(INSTANCE, "MinesDatabase");
    }
}
