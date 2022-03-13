package enchants.impl.enchants.speed;

import enchants.api.enchant.AtlasEnchant;
import enchants.api.enchant.AtlasEnchantCost;
import org.bukkit.configuration.ConfigurationSection;

public class SpeedEnchant implements AtlasEnchant<SpeedData> {

    @Override
    public String name() {
        return "Speed";
    }

    @Override
    public SpeedData createEnchantData(ConfigurationSection section) {
        return new SpeedData("Hi", AtlasEnchantCost.createCost(1, section).getCost(), 1);
    }
}
