import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.serialize.Position;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * @param name When making a new collector you need a UUID to save it under.
 *             <p>
 *             This record allows us to easily create a new collector.
 */
public record Collector(UUID name) {

    /**
     * @param location The location the collector is being registered at.
     * @param upgrades The upgrades that the collector will have. {@link CollectorManager.CollectorUpgrade}
     * @return This returns a new {@link CollectorManager.CollectorUpgrade}
     */
    public Instance createInstance(Location location, CollectorManager.CollectorUpgrade... upgrades) {
        return new Instance(this).setLocation(location).registerUpgrades(upgrades);
    }

    /**
     *
     * @return returns {@link ItemStack} class.
     *
     */
    public ItemStack createItem() {
        return ItemStackBuilder
                .of(Material.BEACON)
                .amount(1)
                .name("Collector")
                .lore("Test", "Test")
                .breakable(false)
                .hideAttributes()
                .transform(is -> CollectorItemData.get(CollectorsAPI.get()).write(is))
                .build();
    }

    /**
     * This is the instance class, when making a collector you must make an instance of it.
     */
    private class Instance {
        private Player owner;
        private JavaPlugin main;
        private Location bukkitLocation;
        private CollectorDataService service;
        private List<CollectorManager.CollectorUpgrade> upgrades;
        private final Collector collector;
        private Hologram collectorHologram;

        public Instance(Collector collector) {
            this.collector = collector;
            this.main = CollectorsAPI.get();
            this.service = new CollectorDataService(main.getDataFolder().getPath());
        }

        /**
         * @param owner Checks if the owner is equal to the player.
         * @return returns a boolean true/false based on ownership.
         */
        public boolean isOwner(Player owner) {
            return this.owner.equals(owner);
        }

        /**
         * @param o The object which we will be turning into bytes.
         * @return returns the byte array for the object.
         */
        public byte[] toBytes(Object o) {
            try {
                ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(byteOS);
                oos.writeObject(o);
                oos.flush();
                return byteOS.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void createMenu() {

        }

        /**
         * @param bukkitLocation The location that the collector will be at.
         * @return returns the {@link Collector.Instance} class.
         * <p>
         * Builder pattern.
         */
        public Instance setLocation(Location bukkitLocation) {
            this.bukkitLocation = bukkitLocation;
            return this;
        }

        /**
         * Creates the {@link Hologram} for the collector.
         */
        public void createHologram() {
            this.collectorHologram =
                    Hologram.create(
                            Position.of(bukkitLocation.add(0, 1.05, 0)),
                            List.of("test"));

        }

        /**
         * Removes the {@link Hologram} for the collector.
         */
        public void removeHologram() {
            this.collectorHologram.despawn();
            this.collectorHologram = null;
        }

        /**
         * @param upgrades The array of upgrades that the collector will have.
         * @return returns the {@link Collector.Instance} class.
         * <p>
         * Builder pattern.
         */
        public Instance registerUpgrades(CollectorManager.CollectorUpgrade... upgrades) {
            this.upgrades.addAll(Arrays.asList(upgrades));
            return this;
        }

        /**
         * @param owner The owner of the collector.
         * @return returns the {@link Collector.Instance} class.
         * <p>
         * Builder pattern.
         */
        public Instance setOwner(Player owner) {
            this.owner = owner;
            return this;
        }

        /**
         * @return returns the {@link Collector.CollectorManager} class.
         * <p>
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
        public void handleRemove(PlayerInteractEvent event) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
            if (!event.getClickedBlock().getLocation().equals(instance.bukkitLocation)) return;
            if (!instance.service.contains(instance.toBytes(instance.bukkitLocation))) return;
            if (!instance.isOwner(event.getPlayer())) return;

            instance.service.delete(instance.toBytes(instance.bukkitLocation));

            instance.removeHologram();

            event.getPlayer().sendMessage("Object Removed!");
        }

        /**
         * Creates the {@link Collector} and registers it.
         */
        public void handleCreate(BlockPlaceEvent event) {
            if (CollectorItemData.get(instance.main).read(event.getItemInHand()) == null) return;
            if (instance.service.contains(instance.toBytes(event.getBlock().getLocation()))) return;

            instance.service.put(
                    instance.toBytes(event.getBlock().getLocation()),
                    instance.toBytes(instance.collector));

            instance.createHologram();

            event.getPlayer().sendMessage("Object placed!");
        }


        /**
         * Opens the {@link Collector} menu.
         */
        public void handleOpen(PlayerInteractEvent event) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
            if (!instance.service.contains(instance.toBytes(event.getClickedBlock().getLocation()))) return;
            if (!instance.isOwner(event.getPlayer())) return;

            event.getPlayer().sendMessage("Helps!");
        }

        /**
         * This is the {@link CollectorUpgrade} interface.
         * <p>
         * This is how we will register new upgrades.
         */
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
            double getPrice();

            /**
             * @param event This is what will happen in the menu when the upgrade is purchased.
             */
            void handlePurchase(InventoryClickEvent event);
        }
    }
}