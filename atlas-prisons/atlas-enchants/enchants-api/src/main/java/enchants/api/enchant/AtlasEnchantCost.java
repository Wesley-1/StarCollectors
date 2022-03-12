package enchants.api.enchant;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface AtlasEnchantCost {

    static AtlasEnchantCost createCost(int enchantmentLevel, ConfigurationSection section) {
        try {
            final double costMulti = section.getDouble("costMulti");
            final double price = section.getDouble("cost");
            final double totalCostOfNewLevel = price + (price * costMulti) * enchantmentLevel;
            final String commandToTake = section.getString("command");

            return new AtlasEnchantCost() {
                @Override
                public double getCostMultiplier() {
                    return costMulti;
                }
                @Override
                public double getCost() {
                    return totalCostOfNewLevel;
                }
                @Override
                public String getCommandToTake() {
                    return commandToTake;
                }
            };

        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to setup the cost of this enchant.");
        }
    }

    double getCostMultiplier();
    double getCost();
    String getCommandToTake();

}
