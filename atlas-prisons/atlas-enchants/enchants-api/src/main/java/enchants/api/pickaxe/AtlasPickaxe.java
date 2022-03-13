package enchants.api.pickaxe;

import me.elapsed.universal.objects.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;

public interface AtlasPickaxe {

    UUID getPickaxeIdentifier();
    ItemBuilder getPickaxeItem();
    HashMap<UUID, AtlasPickaxe> getStoredPicks();


}
