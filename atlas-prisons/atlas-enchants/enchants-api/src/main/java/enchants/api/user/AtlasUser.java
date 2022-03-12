package enchants.api.user;

import enchants.api.enchant.AtlasEnchant;

import java.util.HashMap;
import java.util.UUID;

public interface AtlasUser<X, Y> {

    UUID getUUID();
    HashMap<X, Y> getStoredData();

}
