package collectors.records;

import collectors.items.CollectorItemData;
import collectors.CollectorsAPI;
import collectors.enums.CollectorType;
import collectors.interfaces.CollectorUpgrade;
import collectors.models.Instance;
import me.lucko.helper.item.ItemStackBuilder;
import collectors.models.CollectorInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public record Collector(UUID name, CollectorType collectorType, CollectorInventory inventory, CollectorUpgrade... upgrades) {

    /**
     * @param location The location the collector is being registered at.
     * @return This returns a new {@link Instance}
     */
    public Instance createInstance(Location location) {
        return new Instance(this).setLocation(location).registerUpgrades(upgrades);
    }

    /**
     * @return returns {@link ItemStack} class.
     */
    public ItemStack createItem() {
        return ItemStackBuilder
                .of(Material.BEACON)
                .amount(1)
                .name("Collector: " + collectorType.name())
                .lore("Test", "Test")
                .breakable(false)
                .hideAttributes()
                .transform(is -> CollectorItemData.get(CollectorsAPI.get(), collectorType, List.of(upgrades), inventory).write(is))
                .build();
    }
}

