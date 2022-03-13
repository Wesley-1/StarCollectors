package enchants.api.enchant;

import org.bukkit.configuration.ConfigurationSection;

public interface AtlasEnchantData {

    String getInformation();
    double getChance();
    AtlasEnchantCost getCost(int level, ConfigurationSection section);
    void activate();

}
