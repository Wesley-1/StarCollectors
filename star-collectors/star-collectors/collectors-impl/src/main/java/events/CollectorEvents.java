package events;

import collectors.enums.CollectorType;
import collectors.items.CollectorItemData;
import collectors.managers.CollectorManager;
import collectors.models.CollectorInventory;
import collectors.models.Instance;
import collectors.records.Collector;
import collectors.services.DataService;
import collectors.utility.CollectorTransformer;
import me.lucko.helper.Events;
import menus.CollectorMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CollectorEvents {

    public static void registerCollectorPlace() {
        Events.subscribe(BlockPlaceEvent.class).handler(event -> {
            Player player = event.getPlayer();
            Location location = event.getBlock().getLocation();
            ItemStack itemInHand = event.getItemInHand();

            if (CollectorItemData.readData(itemInHand) == null) return;

            Collector collector = new Collector(
                    player.getUniqueId(),
                    CollectorTransformer.getType(itemInHand),
                    CollectorTransformer.getInventory(itemInHand));

            Instance instance = collector.createInstance(location);
            CollectorManager manager = instance.build();

            manager.handleCreate(event);
        });
    }

    public static void registerCollectorInteract() {
        Events.subscribe(PlayerInteractEvent.class).handler(event -> {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            if (block == null || block.getType() == Material.AIR) return;
            if (!DataService.loadedCollectors.containsKey(event.getClickedBlock().getLocation())) return;

            Collector collector = DataService.loadedCollectors.get(event.getClickedBlock().getLocation());
            Instance instance = collector.createInstance(event.getClickedBlock().getLocation());
            CollectorManager manager = instance.build();

            manager.handleOpen(event, new CollectorMenu(player, instance));

        });

    }
}
