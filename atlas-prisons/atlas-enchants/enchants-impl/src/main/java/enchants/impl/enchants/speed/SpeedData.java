package enchants.impl.enchants.speed;

import enchants.api.enchant.AtlasEnchantCost;
import enchants.api.enchant.AtlasEnchantData;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

public class SpeedData implements AtlasEnchantData {
    private final String information;
    private final double cost;
    private final double chance;

    public SpeedData(String information, double cost, double chance) {
        this.information = information;
        this.cost = cost;
        this.chance = chance;
    }

    @Override
    public String getInformation() {
        return information;
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public AtlasEnchantCost getCost(int level, ConfigurationSection section) {
        return AtlasEnchantCost.createCost(level, section);
    }

    @Override
    public void activate() {

    }
}
