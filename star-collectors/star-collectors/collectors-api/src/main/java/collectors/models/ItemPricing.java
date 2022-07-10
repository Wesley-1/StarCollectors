package collectors.models;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ItemPricing {
    @Getter
    private double amount;
    @Getter private Material material;
    public static ItemPricing instance;

    public static ItemPricing get(ConfigurationSection section) {
        if (ItemPricing.instance == null) {
            ItemPricing.instance = new ItemPricing(section);
        }
        return ItemPricing.instance;
    }

    public ItemPricing(ConfigurationSection section) {
        for (String string : section.getKeys(false)) {
            this.material = Material.getMaterial(string);
            this.amount = section.getDouble(string + ".price");
            /**
             * EXAMPLE:
             *
             * ACACIA_BOAT
             *     - 100.0
             */
        }
    }
}
