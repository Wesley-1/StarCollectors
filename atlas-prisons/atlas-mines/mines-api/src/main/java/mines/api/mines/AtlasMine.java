package mines.api.mines;

import com.google.common.collect.ImmutableMap;
import mines.api.mines.upgrades.AtlasMineUpgrade;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public interface AtlasMine {

    UUID getMineOwner();
    int getMineNumber();
    HashMap<AtlasMineUpgrade, Integer> getMineUpgrades();


}
