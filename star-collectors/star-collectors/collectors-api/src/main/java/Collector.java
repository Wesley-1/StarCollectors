import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.function.Consumer;

/**
 *
 * @param name When making a new collector you need a UUID to save it under.
 *
 * This record allows us to easily create a new collector.
 */
public record Collector(UUID name) {

    /**
     *
     * @param location The location the collector is being registered at.
     * @param upgrades The upgrades that the collector will have. {@link CollectorManager.CollectorUpgrade}
     * @return This returns a new {@link CollectorManager.CollectorUpgrade}
     */
    public Instance createInstance(Location location, CollectorManager.CollectorUpgrade... upgrades) {
        return new Instance().setLocation(location).registerUpgrades(upgrades);
    }

    /**
     * This is the instance class, when making a collector you must make an instance of it.
     */
    private class Instance {
        private Location bukkitLocation;
        private List<CollectorManager.CollectorUpgrade> upgrades;

        public Instance() {
            // TODO: Register database
        }
        /**
         *
         * @param bukkitLocation The location that the collector will be at.
         *
         * @return returns the {@link Collector.Instance} class.
         *
         * Builder pattern.
         */
       public Instance setLocation(Location bukkitLocation) {
            this.bukkitLocation = bukkitLocation;
            return this;
       }

        /**
         *
         * @param upgrades The array of upgrades that the collector will have.
         *
         * @return returns the {@link Collector.Instance} class.
         *
         * Builder pattern.
         */
       public Instance registerUpgrades(CollectorManager.CollectorUpgrade... upgrades) {
           this.upgrades.addAll(Arrays.asList(upgrades));
            return this;
       }

        /**
         *
         * @return returns the {@link Collector.CollectorManager} class.
         *
         * Builds the {@link Collector.CollectorManager} class.
         */
        public CollectorManager build() {
            try {
                return new CollectorManager(this);
            } catch (CollectorCreationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * This is a class which will allow us to handle all things related to a collector.
     */
    private class CollectorManager {
        private final Instance instance;

        public CollectorManager(Instance instance) {
            this.instance = instance;
        }

        /**
         * Removes the {@link Collector} and un-registers it.
         */
        public void handleRemove() {

        }

        /**
         * Creates the {@link Collector} and registers it.
         */
        public void handleCreate() {

        }

        /**
         * Opens the {@link Collector} menu.
         */
        public void handleOpen() {

        }

        /**
         * This is the {@link CollectorUpgrade} interface.
         *
         * This is how we will register new upgrades.
         */
        public interface CollectorUpgrade {

            /**
             *
             * @return returns the name of the {@link CollectorUpgrade}
             *
             */
            String getName();

            /**
             *
             * @return returns the max levels of the {@link CollectorUpgrade}
             *
             */
            int getMaxLevels();

            /**
             *
             * @return returns the price of the {@link CollectorUpgrade}
             *
             */
            double getPrice();

            /**
             *
             * @param event This is what will happen in the menu when the upgrade is purchased.
             *
             */
            void handlePurchase(InventoryClickEvent event);
        }
    }
}
