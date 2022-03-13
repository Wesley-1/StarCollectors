package worldedit.wrapper.v6;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface V6Wrapper {

    static void create(JavaPlugin plugin) {
        new WorldEditV6(plugin);
    }


    void setBlocks(World world, Block block, CuboidRegion region);

    void loadSchematic(Location pasteLoc, String fileName);

    boolean checkRegion(Optional<IWrappedRegion> region, Location location);

    final class WorldEditV6 implements V6Wrapper {
        private final JavaPlugin plugin;

        public WorldEditV6(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void setBlocks(World world, Block block, CuboidRegion region) {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitUtil.getLocalWorld(world), 1000000000);
            try {
                editSession.setBlocks(region, new BaseBlock(block.getType().getId()));
            } catch (MaxChangedBlocksException ignored) {
            }
        }

        @Override
        public void loadSchematic(Location pasteLoc, String fileName) {
            File schematicDir = new File(plugin.getDataFolder(), "/schematics/" + fileName);

            try {

                EditSession editSession = new EditSession(new BukkitWorld(pasteLoc.getWorld()), 999999999);
                editSession.enableQueue();

                SchematicFormat schematic = SchematicFormat.getFormat(schematicDir);
                CuboidClipboard clipboard = schematic.load(schematicDir);

                clipboard.paste(editSession, BukkitUtil.toVector(pasteLoc), true);
                editSession.flushQueue();

            } catch (DataException | IOException | MaxChangedBlocksException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean checkRegion(Optional<IWrappedRegion> region, Location location) {
            return region.map(iWrappedRegion -> iWrappedRegion.contains(location)).orElse(false);
        }
    }
}
