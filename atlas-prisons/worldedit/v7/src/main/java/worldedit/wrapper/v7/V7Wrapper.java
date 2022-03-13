package worldedit.wrapper.v7;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public interface V7Wrapper {

    static void create(JavaPlugin plugin) {
        new WorldEditV7(plugin);
    }

    void setBlocks(World world, Block block, CuboidRegion region);
    void loadSchematic(Location pasteLoc, String fileName);
    boolean checkRegion(Optional<IWrappedRegion> region, Location location);

    final class WorldEditV7 implements V7Wrapper {
        private final JavaPlugin plugin;

        public WorldEditV7(JavaPlugin plugin) {
            this.plugin = plugin;
        }


        @Override
        public void setBlocks(World world, Block block, CuboidRegion region) {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);

            try {
                editSession.setBlocks(region, (Pattern) block.getState());
            } catch (MaxChangedBlocksException e) {
                // As of the blocks are unlimited this should not be called
            }
        }

        @Override
        public void loadSchematic(Location pasteLoc, String fileName) {
            File file = new File(plugin.getDataFolder(), "/schematics/" + fileName);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(pasteLoc.getWorld()), -1)) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(pasteLoc.getX(), pasteLoc.getY(), pasteLoc.getZ()))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean checkRegion(Optional<IWrappedRegion> region, Location location) {
            return region.map(iWrappedRegion -> iWrappedRegion.contains(location)).orElse(false);
        }
    }
}
