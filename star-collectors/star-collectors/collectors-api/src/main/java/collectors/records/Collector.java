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

import java.util.UUID;

public record Collector(UUID name, CollectorType collectorType, CollectorInventory inventory) {

    /**
     * @param location The location the collector is being registered at.
     * @param upgrades The upgrades that the collector will have. {@link collectors.interfaces.CollectorUpgrade}
     * @return This returns a new {@link CollectorUpgrade}
     */
    public Instance createInstance(Location location, CollectorUpgrade... upgrades) {
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
                .transform(is -> CollectorItemData.get(CollectorsAPI.get()).write(is))
                .build();
    }
}

