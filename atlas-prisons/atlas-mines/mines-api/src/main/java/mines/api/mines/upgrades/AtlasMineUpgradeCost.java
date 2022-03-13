package mines.api.mines.upgrades;

import org.bukkit.configuration.ConfigurationSection;

public interface AtlasMineUpgradeCost {

    static AtlasMineUpgradeCost createCost(int upgradeLevel, ConfigurationSection section) {
        try {
            final double costMulti = section.getDouble("costMulti");
            final double price = section.getDouble("cost");
            final double totalCostOfNewLevel = price + (price * costMulti) * upgradeLevel;
            final String commandToTake = section.getString("command");

            return new AtlasMineUpgradeCost() {
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
