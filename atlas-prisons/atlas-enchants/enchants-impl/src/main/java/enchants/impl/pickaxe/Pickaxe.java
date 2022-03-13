package enchants.impl.pickaxe;

import enchants.api.enchant.AtlasEnchant;
import enchants.api.pickaxe.AtlasPickaxe;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.objects.ItemBuilder;
import org.bukkit.entity.Item;

import java.util.HashMap;
import java.util.UUID;

public class Pickaxe implements AtlasPickaxe {
    private final UUID pickaxeIdentifier;
    private final ItemBuilder pickaxeItem;
    private final NMS nms;
    private final HashMap<UUID, AtlasPickaxe> storedPicks;

    public Pickaxe(ItemBuilder pickaxeItem) {
        this.nms = NMS.getInstance();
        this.pickaxeIdentifier = UUID.randomUUID();
        this.storedPicks = new HashMap<>();
        this.pickaxeItem = pickaxeItem;

        storedPicks.put(pickaxeIdentifier, this);
    }

    @Override
    public UUID getPickaxeIdentifier() {
        return pickaxeIdentifier;
    }

    @Override
    public ItemBuilder getPickaxeItem() {
        {
            nms.setKey(pickaxeItem, "Identifier", pickaxeIdentifier.toString());
        }
        return pickaxeItem;
    }

    @Override
    public HashMap<UUID, AtlasPickaxe> getStoredPicks() {
        return storedPicks;
    }

    public ItemBuilder addEnchantLevels(ItemBuilder pickaxe, AtlasEnchant enchant, int amountOfLevels) {
        {
            if (storedPicks.containsKey(UUID.fromString(nms.getStringValue(pickaxe, "Identifier")))) {
                nms.setKey(pickaxe, enchant.name(), nms.getIntValue(pickaxe, enchant.name()) + amountOfLevels);
            }
        }
        return pickaxe;
    }

    public ItemBuilder subtractEnchantLevel(ItemBuilder pickaxe, AtlasEnchant enchant, int amountOfLevels) {
        {
            if (storedPicks.containsKey(UUID.fromString(nms.getStringValue(pickaxe, "Identifier")))) {
                nms.setKey(pickaxe, enchant.name(), nms.getIntValue(pickaxe, enchant.name()) + amountOfLevels);
            }
        }
        return pickaxe;
    }
}
