package mines.impl.mine.regions;

import lombok.Getter;
import mines.api.mines.AtlasMine;
import mines.impl.AtlasMines;
import mines.impl.mine.data.MineData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.codemc.worldguardwrapper.WorldGuardWrapper;

public class AtlasRegions {

    @Getter private Location boundingCornerOne, boundingCornerTwo, mineCornerOne, mineCornerTwo;
    private World world;
    private AtlasMines instance;

    public AtlasRegions(AtlasMine mine) {
        this.instance = AtlasMines.getPlugin(AtlasMines.class);
        this.world = Bukkit.getWorld("MineWorld");
        this.boundingCornerOne = getConfiguredLocation("BoundingCornerOne", mine.getMineNumber() * 1000);
        this.boundingCornerTwo = getConfiguredLocation("BoundingCornerTwo", mine.getMineNumber() * 1000);
        this.mineCornerOne = getConfiguredLocation("MineCornerOne", mine.getMineNumber() * 1000);
        this.mineCornerTwo = getConfiguredLocation("MineCornerTwo", mine.getMineNumber() * 1000);

        WorldGuardWrapper.getInstance().addCuboidRegion(String.valueOf(mine.getMineNumber() + "-BoundingRegion"), boundingCornerOne, boundingCornerTwo);
        WorldGuardWrapper.getInstance().addCuboidRegion(String.valueOf(mine.getMineNumber() + "-MineRegion"), mineCornerOne, mineCornerTwo);
    }

    public Location getConfiguredLocation(String locType, double addToX) {
        var x = instance.getConfig().getDouble("MineLocations.base." + locType + ".x");
        var y = instance.getConfig().getDouble("MineLocations.base." + locType + ".y");
        var z = instance.getConfig().getDouble("MineLocations.base." + locType + ".z");
        return new Location(world, x + addToX, y, z);
    }

}
