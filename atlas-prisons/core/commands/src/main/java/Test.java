import command.AtlasCommand;
import command.AtlasSubCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {

        AtlasCommand atlasCommand = AtlasCommand.builder()
                .setCommandName("test")
                .setDescription("Test command")
                .setPermission("testPermission")
                .setCommandAlias(Arrays.asList("test1", "test2"))
                .setExecutor(sender -> {
                    if (sender instanceof Player player) {

                        player.sendMessage("Testing command!");
                    }
                });

        AtlasSubCommand.builder()
                .setCommandName("testSub")
                .setPermission("permissionTest")
                .setDescription("descriptionTest")
                .setExecutor((sender, args) -> {
                    if (sender instanceof Player player) {
                        player.sendMessage("hi");
                        switch (args.length) {
                            case 2 -> {
                                String string = args[1];
                                player.sendMessage("Arguments are working! 2 " + string);
                            }
                            case 3 -> {
                                String string = args[1];
                                String string2 = args[2];
                                player.sendMessage("Arguments are working! 3 " + string + string2);
                            }
                        }
                    }
                }).build(atlasCommand);

        atlasCommand.build();
    }

    @Override
    public void onDisable() {

    }

}
