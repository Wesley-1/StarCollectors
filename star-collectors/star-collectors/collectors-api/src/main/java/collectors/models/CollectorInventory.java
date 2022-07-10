package collectors.models;

import collectors.exceptions.CollectorCreationException;
import collectors.records.Collector;
import lombok.Getter;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.List;

/**
 * This is a class, that is used when building the {@link Instance}
 *
 * How we will be handling all storing of the collectors inventory.
 */
public class CollectorInventory {
    private final int size;
    @Getter private final List<ItemStack> itemsToCollect;
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
     * @param differentItemsAllowed These are for the different items in the collector.
     * @param items                 These are the items that will be going into the collector.
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
     * @param itemStack The itemstack we are trying to obtain.
     * @return returns the itemstack of the {@link Collector} item.
     */
    public Item getInventoryItem(ItemStack itemStack) {
        return ItemStackBuilder.of(itemStack.getType())
                .lore(storedItems.get(itemStack).toString())
                .name("This is a drop item.")
                .buildItem().build();
    }

    public boolean isFull() {
        return (this.storedItems.size() + 1) >= this.size && this.size != 0;
    }

    /**
     * @param items The items you would like to add.
     *              <p>
     *              Handles the adding of items to the {@link Collector} storage.
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
     * @param section The configuration section for the material prices.
     * @return returns the total amount in the collector.
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
}
