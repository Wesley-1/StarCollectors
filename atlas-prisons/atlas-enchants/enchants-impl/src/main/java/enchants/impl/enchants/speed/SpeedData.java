package enchants.impl.enchants.speed;

import enchants.api.enchant.AtlasEnchantData;
import lombok.Getter;

@Getter
public class SpeedData implements AtlasEnchantData {
    private final SpeedEnchant parent;
    private final String information;
    private final double cost;

    public SpeedData(SpeedEnchant parent, String information, double cost) {
        this.parent = parent;
        this.information = information;
        this.cost = cost;
    }
}
