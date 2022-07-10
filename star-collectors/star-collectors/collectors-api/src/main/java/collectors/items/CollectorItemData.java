package collectors.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CollectorItemData extends PersistentItemData {
    private final NamespacedKey DATA_KEY;
    private static CollectorItemData instance;

    public CollectorItemData(JavaPlugin plugin) {
        this.DATA_KEY = new NamespacedKey(plugin, "Collector");
    }

    public static CollectorItemData get(JavaPlugin plugin) {
        if (CollectorItemData.instance == null) {
            CollectorItemData.instance = new CollectorItemData(plugin);
        }
        return CollectorItemData.instance;
    }

    public void write(ItemStack itemStack) {
        super.write(itemStack, DATA_KEY);
    }

    public CollectorItemData read(ItemStack itemStack) {
        return PersistentItemData.read(itemStack, DATA_KEY, CollectorItemData.class);
    }
}
