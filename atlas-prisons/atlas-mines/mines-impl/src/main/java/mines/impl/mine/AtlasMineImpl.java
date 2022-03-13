package mines.impl.mine;

import lombok.Getter;
import mines.api.mines.AtlasMine;
import mines.api.mines.upgrades.AtlasMineUpgrade;
import mines.impl.mine.data.MineData;
import mines.impl.mine.regions.AtlasRegions;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class AtlasMineImpl implements AtlasMine {
    private final UUID mineOwner;
    private final int mineNumber;
    private final HashMap<AtlasMineUpgrade, Integer> mineUpgrades;
    private final AtlasRegions region;

    public AtlasMineImpl(UUID mineOwner) {
        this.mineUpgrades = new HashMap<>();
        this.mineOwner = mineOwner;
        this.mineNumber = MineData.loadedMines.size() + 1;
        this.region = new AtlasRegions(this);
        MineData.loadedMines.put(mineOwner, this);
    }
}
