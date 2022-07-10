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
import java.util.List;
import java.util.function.Predicate;

public class CollectorCommand {

    public static void registerCommmands() {
        Commands.create()
                .assertPermission("collector.give").assertPlayer().assertUsage("<player> <amount>").handler(c -> {

            String arg = c.arg(0).parseOrFail(String.class);
            if (arg.equalsIgnoreCase("give")) {
                Player target = c.arg(1).parseOrFail(Player.class);
                Collector collector = new Collector(target.getUniqueId(), CollectorType.LIMITED_MULTI_ITEM, new CollectorInventory(
                        1, List.of(new ItemStack(Material.STONE))));

                target.getInventory().addItem(collector.createItem());
            }
        }).register("collector");
    }
}
