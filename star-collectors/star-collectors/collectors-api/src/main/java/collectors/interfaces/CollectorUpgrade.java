package collectors.interfaces;

import collectors.models.Instance;
import org.bukkit.event.inventory.InventoryClickEvent;
import collectors.records.Collector;

import java.io.Serializable;

public interface CollectorUpgrade {

    /**
     * @return returns the name of the {@link CollectorUpgrade}
     */
    String getName();

    /**
     * @return returns the max levels of the {@link CollectorUpgrade}
     */
    int getMaxLevels();

    /**
     * @return returns the price of the {@link CollectorUpgrade}
     */
    double getPrice(int level);

    /**
     * @param instance {@link Instance} instance class.
     * @return returns the current level of the upgrade.
     */
    default int getCurrentLevel(Instance instance) {

        if (instance.getUpgrades().contains(this)) {
            return instance.getUpgradeLevels().get(this);
        }

        return 0;
    }

    /**
     * @param instance {@link Instance}
     *                 <p>
     *                 instance class.
     *                 <p>
     *                 Gets the current level of the upgrade and adds {X} levels to it.
     */
    default void upgrade(Instance instance) {
        if (instance.getUpgrades().contains(this)) {

            if (instance.getUpgradeLevels().get(this) == null) return;
            if (instance.getUpgradeLevels().get(this) <= getMaxLevels()) return;

            instance.getUpgradeLevels().replace(this, instance.getUpgradeLevels().get(this) + 1);
        }
    }

    /**
     * @param event This is what will happen in the menu when the upgrade is purchased.
     */
    void handlePurchase(InventoryClickEvent event, Instance instance);

    /**
     * @param instance {@link Instance} instance class.
     */
    void run(Instance instance);
}