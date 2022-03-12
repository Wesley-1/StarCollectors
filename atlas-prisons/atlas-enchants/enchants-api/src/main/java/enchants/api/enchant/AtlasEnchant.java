package enchants.api.enchant;

import org.bukkit.configuration.ConfigurationSection;

public interface AtlasEnchant<T extends AtlasEnchantData> {

    /**
     * @return Returns the enchants name
     */
    String name();

    /**
     *
     * @param section Config section
     * @return This returns the dataclass related to the enchant/
     *
     */
    //
    T createEnchantData(ConfigurationSection section);
}
