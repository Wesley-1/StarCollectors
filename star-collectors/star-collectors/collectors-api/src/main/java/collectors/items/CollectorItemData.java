package collectors.items;

import collectors.enums.CollectorType;
import collectors.interfaces.CollectorUpgrade;
import collectors.models.CollectorInventory;
import lombok.Getter;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
public class CollectorItemData extends PersistentItemData {
    public static NamespacedKey DATA_KEY;
    public static NamespacedKey TYPE_KEY;
    public static NamespacedKey UPGRADES_KEY;
    public static NamespacedKey INVENTORY_KEY;
    public static CollectorItemData instance;

    public CollectorItemData(JavaPlugin plugin, CollectorType type, List<CollectorUpgrade> upgrades, CollectorInventory inventory) {
        DATA_KEY = new NamespacedKey(plugin, "Collector");
        TYPE_KEY = new NamespacedKey(plugin, type.);
        UPGRADES_KEY = new NamespacedKey(plugin, Arrays.toString(SerializationUtils.serialize((Serializable) upgrades)));
        INVENTORY_KEY = new NamespacedKey(plugin, Arrays.toString(SerializationUtils.serialize(inventory)));
    }

    public static CollectorItemData get(JavaPlugin plugin, CollectorType type, List<CollectorUpgrade> upgrades, CollectorInventory inventory) {
        if (CollectorItemData.instance == null) {
            CollectorItemData.instance = new CollectorItemData(plugin, type, upgrades, inventory);
        }
        return CollectorItemData.instance;
    }

    public void write(ItemStack itemStack ) {
        super.write(itemStack, DATA_KEY);
        super.write(itemStack, TYPE_KEY);
        super.write(itemStack, UPGRADES_KEY);
        super.write(itemStack, INVENTORY_KEY);
    }

    public static CollectorItemData readData(ItemStack itemStack) {
        return PersistentItemData.read(itemStack, DATA_KEY, CollectorItemData.class);
    }

    public static CollectorItemData readType(ItemStack itemStack) {
        return PersistentItemData.read(itemStack, TYPE_KEY, CollectorItemData.class);
    }

    public static CollectorItemData readInventory(ItemStack itemStack) {
        return PersistentItemData.read(itemStack, INVENTORY_KEY, CollectorItemData.class);
    }

    public static CollectorItemData readUpgrades(ItemStack itemStack) {
        return PersistentItemData.read(itemStack, UPGRADES_KEY, CollectorItemData.class);
    }
}
