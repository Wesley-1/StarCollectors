package commmands;

import collectors.enums.CollectorType;
import collectors.models.CollectorInventory;
import collectors.records.Collector;
import me.lucko.helper.Commands;
import me.lucko.helper.command.argument.ArgumentParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.Predicate;

public class CollectorCommand {

    public static void registerCommmands() {
        Commands.create()
                .assertPermission("collector.give").assertPlayer().assertUsage("<player> <amount>").handler(c -> {

            String arg = c.arg(0).parseOrFail(String::describeConstable);
            if (arg.equalsIgnoreCase("give")) {
                Player target = c.arg(1).parseOrFail(Player.class);
                Collector collector = new Collector(target.getUniqueId(), CollectorType.LIMITED_MULTI_ITEM, new CollectorInventory(
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

                target.getInventory().addItem(collector.createItem());
            }
        }).register("collector");
    }
}
