import lombok.Getter;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.text3.Text;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @param name When making a new collector you need a UUID to save it under.
 *             <p>
 *             This record allows us to easily create a new collector.
 */
public record Collector(UUID name, CollectorType collectorType, CollectorManager.CollectorInventory inventory) {

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
                .name("Collector: " + collectorType.name)
                .lore("Test", "Test")
                .breakable(false)
                .hideAttributes()
                .transform(is -> CollectorItemData.get(CollectorsAPI.get()).write(is))
                .build();
    }

    enum CollectorType {
        LIMITED_MULTI_ITEM("Limited_Multi"),
        LIMITED_SINGLE_ITEM("Limited_Single"),
        INFINITE_MULTI_ITEM("Inf_Multi"),
        INFINITE_SINGLE_ITEM("Inf_Single");

        private final String name;
        CollectorType(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * This is the instance class, when making a collector you must make an instance of it.
     */
    public class Instance {
        @Getter private Player owner;
        private JavaPlugin main;
        @Getter private Location bukkitLocation;
        private CollectorDataService service;
        private List<CollectorManager.CollectorUpgrade> upgrades;
        @Getter private final Collector collector;
        @Getter private Hologram collectorHologram;
        @Getter private Chunk chunk;
        private final LinkedHashMap<CollectorManager.CollectorUpgrade, Integer> upgradeLevels;

        public Instance(Collector collector) {
            this.collector = collector;
            this.owner = Bukkit.getPlayer(collector.name);
            this.main = CollectorsAPI.get();
            this.upgradeLevels = new LinkedHashMap<>();
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
         *
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

        /**
         *
         * @param chunk This allows us to set the chunk the collector is in.
         *
         * @return returns the {@link Collector.Instance} class.
         *
         * Builder Pattern.
         */
        public Instance setChunk(Chunk chunk) {
            this.chunk = chunk;
            return this;
        }

        /**
         * Resets the chunk the object was in.
         *
         * @return returns the {@link Collector.Instance} c;ass/
         *
         * Builder Pattern.
         */
        public Instance removeChunk() {
            this.chunk = null;
            return this;
        }

        /**
         * @param bukkitLocation The location that the collector will be at.
         * @return returns the {@link Collector.Instance} class.
         *
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
         *
         * @param event {@link EntityDeathEvent} class.
         *
         */
        public void handleEntityDeath(EntityDeathEvent event) {
            if (collector == null) return;
            if (collector.inventory.isFull()) return;
            collector.inventory.handleAddingItems(event.getDrops());
            event.getDrops().clear();
        }

        /**
         * @param upgrades The array of upgrades that the collector will have.
         * @return returns the {@link Collector.Instance} class.
         * <p>
         * Builder pattern.
         */
        public Instance registerUpgrades(CollectorManager.CollectorUpgrade... upgrades) {
            this.upgrades.addAll(Arrays.asList(upgrades));
            this.upgrades.forEach($ -> {
                this.upgradeLevels.put($, 1);
            });

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
    class CollectorManager {
        private final Instance instance;

        public CollectorManager(Instance instance) {
            this.instance = instance;
        }

        /**
         * Removes the {@link Collector} and un-registers it.
         */
        public void handleRemove(InventoryClickEvent event) {
            if (instance.chunk == null) return;
            if (!instance.service.contains(instance.toBytes(instance.bukkitLocation))) return;
            if (!(event.getWhoClicked() instanceof Player player)) return;
            if (!instance.isOwner(player)) return;

            instance.service.delete(instance.toBytes(instance.bukkitLocation));

            instance.removeHologram();
            instance.removeChunk();

            player.sendMessage("Object Removed!");
        }

        /**
         * Creates the {@link Collector} and registers it.
         */
        public void handleCreate(BlockPlaceEvent event) {
            if (instance.chunk != null) return;
            if (CollectorItemData.get(instance.main).read(event.getItemInHand()) == null) return;
            if (instance.service.contains(instance.toBytes(event.getBlock().getLocation()))) return;

            instance.service.put(
                    instance.toBytes(event.getBlock().getLocation()),
                    instance.toBytes(instance.collector));

            instance.createHologram();
            instance.setChunk(event.getBlock().getChunk());

            event.getPlayer().sendMessage("Object placed!");
        }


        /**
         * Opens the {@link Collector} menu.
         */
        public void handleOpen(PlayerInteractEvent event, Gui gui) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
            if (!instance.service.contains(instance.toBytes(event.getClickedBlock().getLocation()))) return;
            if (!instance.isOwner(event.getPlayer())) return;

            gui.open();

            event.getPlayer().sendMessage("Works!");
        }

        /**
         * This is a class, that is used when building the {@link Collector.Instance}
         *
         * How we will be handling all storing of the collectors inventory.
         */
        public static class CollectorInventory {
            private final int size;
            private final List<ItemStack> itemsToCollect;
            private final int differentItemsAllowed;
            private final HashMap<ItemStack, Integer> storedItems;
            private double totalMoneyAmount;

            public CollectorInventory(int size, int differentItemsAllowed, List<ItemStack> items) {
                if ((items.size() + 1) > differentItemsAllowed)
                    throw new CollectorCreationException("Error creating collector: Item Types");
                this.totalMoneyAmount = 0;
                this.differentItemsAllowed = differentItemsAllowed;
                this.itemsToCollect = items;
                storedItems = new HashMap<>();
                this.size = size;
            }

            /**
             *
             * @param differentItemsAllowed These are for the different items in the collector.
             * @param items These are the items that will be going into the collector.
             *
             */
            public CollectorInventory(int differentItemsAllowed, List<ItemStack> items) {
                if ((items.size() + 1) > differentItemsAllowed)
                    throw new CollectorCreationException("Error creating collector: Item Types");
                this.totalMoneyAmount = 0;
                this.differentItemsAllowed = differentItemsAllowed;
                this.itemsToCollect = items;
                storedItems = new HashMap<>();
                this.size = 0;
            }

            /**
             *
             * @param itemStack The itemstack we are trying to obtain.
             *
             * @return returns the itemstack of the {@link Collector} item.
             *
             */
            public ItemStack getInventoryItem(ItemStack itemStack) {
                return ItemStackBuilder.of(itemStack.getType())
                        .lore(storedItems.get(itemStack).toString())
                        .name("This is a drop item.")
                        .build();
            }

            public boolean isFull() {
                return (this.storedItems.size() + 1) >= this.size && this.size != 0;
            }
            /**
             *
             * @param items The items you would like to add.
             *
             * Handles the adding of items to the {@link Collector} storage.
             *
             */
            public void handleAddingItems(List<ItemStack> items) {
                items.forEach($ -> {
                    if (!itemsToCollect.contains($)) return;
                    if ((storedItems.size() + 1) >= this.size && this.size != 0) return;

                    if (!storedItems.containsKey($))
                        storedItems.put($, 1);
                    else storedItems.replace($, storedItems.get($) + 1);

                });

            }

            /**
             *
             * @param section The configuration section for the material prices.
             *
             * @return returns the total amount in the collector.
             *
             */
            public double getTotalMoneyAmount(ConfigurationSection section) {
                this.totalMoneyAmount = 0;
                storedItems.forEach(((itemStack, integer) -> {
                    if (itemStack.getType().equals(ItemPricing.get(section).getMaterial())) {
                        this.totalMoneyAmount += (integer * ItemPricing.get(section).getAmount());
                    }
                }));
                return this.totalMoneyAmount;
            }

            /**
             * Clears the collectors items.
             */
            public void handleRemovingItems() {
                storedItems.forEach((itemStack, integer) -> storedItems.remove(itemStack));
            }

            public static class ItemPricing {
                @Getter private double amount;
                @Getter private Material material;
                public static ItemPricing instance;
                public static ItemPricing get(ConfigurationSection section) {
                    if (ItemPricing.instance == null) {
                        ItemPricing.instance = new ItemPricing(section);
                    }
                    return ItemPricing.instance;
                }

                public ItemPricing(ConfigurationSection section) {
                    for (String string : section.getKeys(false)) {
                        this.material = Material.getMaterial(string);
                        this.amount = section.getDouble(string + ".price");
                        /**
                         * EXAMPLE:
                         *
                         * ACACIA_BOAT
                         *     - 100.0
                         */
                    }
                }
            }
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
            double getPrice(int level);

            /**
             *
             * @param instance {@link Instance} instance class.
             *
             * @return returns the current level of the upgrade.
             */
            default int getCurrentLevel(Instance instance) {

                if (instance.upgrades.contains(this)) {
                    return instance.upgradeLevels.get(this);
                }

                return 0;
            }

            /**
             *
             * @param instance {@link Instance} instance class.
             *
             * Gets the current level of the upgrade and adds {X} levels to it.
             */
            default void upgrade(Instance instance) {
                if (instance.upgrades.contains(this)) {

                    if (instance.upgradeLevels.get(this) == null) return;
                    if (instance.upgradeLevels.get(this) <= getMaxLevels()) return;

                    instance.upgradeLevels.replace(this, instance.upgradeLevels.get(this) + 1);
                }
            }

            /**
             * @param event This is what will happen in the menu when the upgrade is purchased.
             */
            void handlePurchase(InventoryClickEvent event, Instance instance);

            /**
             *
             * @param instance {@link Instance} instance class.
             *
             */
            void run(Instance instance);
        }
    }
}