package collectors.managers;

import collectors.items.CollectorItemData;
import me.lucko.helper.menu.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import collectors.records.Collector;
import collectors.models.Instance;

public class CollectorManager {
    private final Instance instance;

    public CollectorManager(Instance instance) {
        this.instance = instance;
    }

    /**
     * Removes the {@link Collector} and un-registers it.
     */
    public void handleRemove(InventoryClickEvent event) {
        if (instance.getChunk() == null) return;
        if (!instance.getService().containsKey(instance.getBukkitLocation())) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!instance.isOwner(player)) return;

        instance.getService().remove(instance.getBukkitLocation());

        instance.removeHologram();
        instance.removeChunk();

        player.sendMessage("Object Removed!");
    }

    /**
     * Creates the {@link Collector} and registers it.
     */
    public void handleCreate(BlockPlaceEvent event) {
        if (instance.getChunk() != null) return;
        if (CollectorItemData.get(instance.getMain()).read(event.getItemInHand()) == null) return;
        if (instance.getService().containsKey(event.getBlock().getLocation())) return;

        instance.getService().put(instance.getBukkitLocation(), instance.getCollector());

        instance.createHologram();
        instance.setChunk(event.getBlock().getChunk());

        event.getPlayer().sendMessage("Object placed!");
    }


    /**
     * Opens the {@link Collector} menu.
     */
    public void handleOpen(PlayerInteractEvent event, Gui gui) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
        if (!instance.getService().containsKey(event.getClickedBlock().getLocation())) return;
        if (!instance.isOwner(event.getPlayer())) return;

        gui.open();

        event.getPlayer().sendMessage("Works!");
    }
}
