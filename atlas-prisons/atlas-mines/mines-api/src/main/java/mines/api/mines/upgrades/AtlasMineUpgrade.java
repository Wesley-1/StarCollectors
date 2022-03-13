package mines.api.mines.upgrades;

import org.bukkit.configuration.ConfigurationSection;

public interface AtlasMineUpgrade<T extends AtlasMineUpgradeData> {

    String getname();

    T createUpgradeData(ConfigurationSection section);

}
