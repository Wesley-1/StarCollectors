package collectors.managers;

import collectors.exceptions.CollectorCreationException;
import collectors.items.CollectorItemData;
import me.lucko.helper.menu.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import collectors.records.Collector;
import collectors.models.Instance;

import java.sql.SQLOutput;

public class CollectorManager {
    private final Instance instance;

    public CollectorManager(Instance instance) {
        this.instance = instance;
    }

    /**
     * Removes the {@link Collector} and un-registers it.
     */
    public void handleRemove(InventoryClickEvent event) {
        if (instance.getChunk() == null) throw new CollectorCreationException("The instance chunk is null.");
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

        if (CollectorItemData.readData(event.getItemInHand()) == null)
            throw new CollectorCreationException("The CollectorData is null.");

        if (CollectorItemData.readInventory(event.getItemInHand()) == null)
            throw new CollectorCreationException("The InventoryData is null.");

        if (CollectorItemData.readUpgrades(event.getItemInHand()) == null)
            throw new CollectorCreationException("The UpgradesData is null.");

        if (CollectorItemData.readType(event.getItemInHand()) == null)
            throw new CollectorCreationException("The TypeData is null.");

        System.out.println("Item data");

        if (instance.getService().containsKey(event.getBlock().getLocation()))
            throw new CollectorCreationException("Service contains loc already.");

        System.out.println("Service already set.");

        instance.getService().put(instance.getBukkitLocation(), instance.getCollector());

        instance.createHologram();
        instance.setChunk(event.getBlock().getChunk());

        event.getPlayer().sendMessage("Object placed!");
    }


    /**
     * Opens the {@link Collector} menu.
     */
    public void handleOpen(PlayerInteractEvent event, Gui gui) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) throw new CollectorCreationException("");
        if (!instance.getService().containsKey(event.getClickedBlock().getLocation())) throw new CollectorCreationException("");
        if (!instance.isOwner(event.getPlayer())) return;

        gui.open();

        event.getPlayer().sendMessage("Works!");
    }
}
