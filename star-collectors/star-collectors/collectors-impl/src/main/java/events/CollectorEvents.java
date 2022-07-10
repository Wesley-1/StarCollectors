package events;

import collectors.enums.CollectorType;
import collectors.managers.CollectorManager;
import collectors.models.CollectorInventory;
import collectors.models.Instance;
import collectors.records.Collector;
import collectors.services.DataService;
import me.lucko.helper.Events;
import menus.CollectorMenu;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Map;

public class CollectorEvents {

    public static void registerEvents() {
        Events.subscribe(BlockPlaceEvent.class).handler(event -> {
            Collector collector = new Collector(event.getPlayer().getUniqueId(), CollectorType.LIMITED_MULTI_ITEM, new CollectorInventory(
                    12, Arrays.asList(
                    new ItemStack(Material.ROTTEN_FLESH),
                    new ItemStack(Material.DIAMOND),
                    new ItemStack(Material.IRON_INGOT),
                    new ItemStack(Material.GOLD_BLOCK),
                    new ItemStack(Material.GOLD_INGOT),
                    new ItemStack(Material.BONE),
                    new ItemStack(Material.BONE_BLOCK),
                    new ItemStack(Material.STONE),
                    new ItemStack(Material.COBBLESTONE),
                    new ItemStack(Material.OAK_WOOD),
                    new ItemStack(Material.OAK_PLANKS),
                    new ItemStack(Material.SPRUCE_PLANKS),
                    new ItemStack(Material.SPRUCE_WOOD))));

            Instance instance = collector.createInstance(event.getBlock().getLocation()).setChunk(event.getBlock().getChunk());
            CollectorManager manager = instance.build();
            manager.handleCreate(event);
        });

        Events.subscribe(PlayerInteractEvent.class).handler(c -> {
            if (DataService.loadedCollectors == null) return;
            if (DataService.loadedCollectors.isEmpty()) return;
            if (!DataService.loadedCollectors.containsKey(c.getClickedBlock().getLocation())) return;
            Collector collector = DataService.loadedCollectors.get(c.getClickedBlock().getLocation());
            Instance instance = collector.createInstance(c.getClickedBlock().getLocation());
            CollectorManager manager = instance.build();
            manager.handleOpen(c, new CollectorMenu(c.getPlayer(), instance));
        });
    }
}
