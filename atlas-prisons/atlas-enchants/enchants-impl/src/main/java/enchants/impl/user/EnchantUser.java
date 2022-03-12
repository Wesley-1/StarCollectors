package enchants.impl.user;

import enchants.api.enchant.AtlasEnchant;
import enchants.api.user.AtlasUser;
import enchants.impl.service.EnchantDataService;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class EnchantUser implements AtlasUser<AtlasEnchant, Integer> {

    private final UUID UUID;
    private final HashMap<AtlasEnchant, Integer> storedData;

    public EnchantUser(UUID UUID) {
        this.UUID = UUID;
        this.storedData = new HashMap<>();
        EnchantDataService.loadedUsers.put(UUID, this);
    }
}
