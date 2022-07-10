package collectors.utility;

import collectors.enums.CollectorType;
import collectors.items.CollectorItemData;
import collectors.models.CollectorInventory;
import collectors.records.Collector;
import collectors.services.DataService;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectorTransformer {

    public static CollectorInventory getInventory(ItemStack itemStack) {
        return (CollectorInventory)
                SerializationUtils.deserialize(
                SerializationUtils.serialize(CollectorItemData.INVENTORY_KEY.getKey()));
    }

    public static List<Object> getUpgrades(ItemStack itemStack) {
        return Collections.singletonList(SerializationUtils.deserialize(
                SerializationUtils.serialize(CollectorItemData.UPGRADES_KEY.getKey())));
    }

    public static CollectorType getType(ItemStack itemStack) {
        return (CollectorType)
                SerializationUtils.deserialize(
                SerializationUtils.serialize(CollectorItemData.TYPE_KEY.getKey()));
    }

    public static Collector fromLocation(Location location) {
        return DataService.loadedCollectors.get(location);
    }
}
