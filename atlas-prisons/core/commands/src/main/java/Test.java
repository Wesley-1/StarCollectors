import command.AtlasCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {

        AtlasCommand.builder().setCommandName("test").setPermission("testPerm").setCommandAlias(Arrays.asList("test1", "test2")).build(this, (sender) -> {
            Player player = (Player) sender;
            player.sendMessage("hi");
        });

    }

    @Override
    public void onDisable() {

    }

}
