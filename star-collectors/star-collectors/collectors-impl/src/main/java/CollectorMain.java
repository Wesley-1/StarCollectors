import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.scheduler.Scheduler;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CollectorMain {

    public void onPlace(BlockPlaceEvent event) {
        Collector collector = new Collector(UUID.randomUUID(), Collector.CollectorType.LIMITED_MULTI_ITEM,
                new Collector.CollectorManager.CollectorInventory(10000, 1, Arrays.asList(ItemStackBuilder.of(Material.ROTTEN_FLESH).build())));
        Collector.Instance instance = collector.createInstance(event.getBlock().getLocation(), new TestUpgrade());
        instance.setChunk(event.getBlock().getChunk());
        instance.build().handleCreate(event);
    }

    public final class TestUpgrade implements Collector.CollectorManager.CollectorUpgrade {

        @Override
        public String getName() {
            return "Testing";
        }

        @Override
        public int getMaxLevels() {
            return 5;
        }

        @Override
        public double getPrice(int level) {
            return switch(level) {
                case 1 -> 20.0;
                case 2 -> 30.0;
                case 3 -> 40.0;
                case 4 -> 50.0;
                case 5 -> 60.0;
                default -> throw new IllegalStateException("Unexpected value: " + level);
            };
        }

        @Override
        public void handlePurchase(InventoryClickEvent event, Collector.Instance instance) {
            upgrade(instance);
            event.getWhoClicked().sendMessage("Hi");
        }

        @Override
        public void run(Collector.Instance instance) {
            Schedulers.async().runRepeating(() -> {
                List<Entity> items = Arrays.stream(instance.getChunk()
                                .getEntities()).filter(e -> e instanceof Item).toList();


                items.forEach(Entity::remove);

            }, 10, 10);
        }
    }
}
