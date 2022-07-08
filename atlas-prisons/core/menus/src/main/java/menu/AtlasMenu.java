package menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public record AtlasMenu(String menuName, MenuLayout layout, Inventory inventory) {

    private static HashMap<String, Consumer<InventoryClickEvent>> clickEvents = new HashMap<>();

    private void setItem(ItemStack item, int slot) {
        inventory.setItem(slot, item);
    }

    private void setItem(ItemStack item, int slot, Consumer<InventoryClickEvent> clickEvent) {
        inventory.setItem(slot, item);
        clickEvents.put(menuName, clickEvent);
    }

    private void removeItem(int slot) {
        inventory.clear(slot);
    }

    private void clearInventory() {
        inventory.clear();
    }

    private void fill(ItemStack itemStack) {
        for (int i = 0; i <= layout.getLayout().length + 1; i++) {
            for (ItemStack content : inventory.getContents()) {
                if (content.getType().equals(Material.AIR)) {
                    inventory.setItem(i, itemStack);
                }
            }
        }

    }
}
