import collectors.CollectorsAPI;
import collectors.managers.CollectorManager;
import collectors.services.DataService;
import commmands.CollectorCommand;
import events.CollectorEvents;
import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CollectorMain extends ExtendedJavaPlugin {

    @Override
    protected void enable() {
        new CollectorsAPI().bind(CollectorMain.class);
        CollectorEvents.registerEvents();
        CollectorCommand.registerCommmands();
    }


    @Override
    protected void disable() {
        DataService.save();
    }
}