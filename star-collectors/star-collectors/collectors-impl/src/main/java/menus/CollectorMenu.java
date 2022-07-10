package menus;

import collectors.models.Instance;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.sql.external.hikari.util.UtilityElf;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectorMenu extends Gui {
    private final Instance instance;

    public CollectorMenu(Player player, Instance instance) {
        super(player, 3, Text.colorize("&c&l" + player.getName().toUpperCase() + "'S COLLECTOR"));
        this.instance = instance;
    }

    private static final MenuScheme COLLECTOR_DISPLAY = new MenuScheme()
            .mask("111111110")
            .mask("101101101")
            .mask("111101111");

    private static final MenuScheme COLLECTOR_UPGRADES = new MenuScheme()
            .mask("000000000")
            .mask("000000000")
            .mask("000010000");

    private static final MenuScheme COLLECTOR_INVENTORY = new MenuScheme()
            .mask("000000000")
            .mask("011111110")
            .mask("000010000");

    private static final List<Integer> pages = new ArrayList<>();

    @Override
    public void redraw() {
        if (isFirstDraw()) {

            COLLECTOR_DISPLAY.newPopulator(this).placeIfSpace(ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE).buildItem().build());

            this.setItem(8,
                    ItemStackBuilder.of(Material.BOOK)
                            .name(Text.colorize("&cInformation"))
                            .lore(Text.colorize("&aHi"))
                            .buildItem()
                            .build());

            this.setItem(10, ItemStackBuilder.of(Material.ANVIL).buildItem().bind(ClickType.LEFT, event -> {
                COLLECTOR_INVENTORY.newPopulator(this).placeIfSpace(ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE).buildItem().build());
                AtomicInteger slot = new AtomicInteger(10);

                for (ItemStack item : instance.getCollector().inventory().getItemsToCollect()) {
                    this.setItem(slot.get(), instance.getCollector().inventory().getInventoryItem(item));
                    if (slot.get() == 16) return;
                    slot.getAndIncrement();
                }

                this.setItem(8, ItemStackBuilder.of(Material.ARROW).buildItem().bind(ClickType.LEFT, clickEv -> {
                    if (pages.size() == 0) return;
                    pages.remove(0);
                    slot.set(10);
                }).build());

                this.setItem(17, ItemStackBuilder.of(Material.ARROW).buildItem().bind(ClickType.LEFT, clickEv -> {
                    if (pages.size() != 0) return;
                    pages.add(0);
                    slot.set(10);
                }).build());

            }).build());

        }
    }
}

