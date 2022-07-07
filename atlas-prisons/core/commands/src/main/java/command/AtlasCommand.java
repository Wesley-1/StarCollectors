package command;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AtlasCommand {

    private String commandName;
    private String permission;
    private List<String> commandAlias;
    private List<AtlasCommand> subCommands;
    private Command command;
    private SimpleCommandMap commandMap;

    public static AtlasCommand instance;

    static {
        instance = new AtlasCommand();
    }

    public AtlasCommand() {
        this.commandName = "test";
        this.permission = "test";
        this.subCommands = new ArrayList<>();
        this.commandMap = new SimpleCommandMap(Bukkit.getServer());
        this.commandAlias = new ArrayList<>();
    }

    public static AtlasCommand builder() {
        return instance;
    }

    public AtlasCommand setCommandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public AtlasCommand setCommandAlias(List<String> commandAlias) {
        this.commandAlias = commandAlias;
        return this;
    }

    public AtlasCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public AtlasCommand createSubCommand(List<AtlasCommand> subCommands) {
        this.subCommands = subCommands;
        return this;
    }

    public void build(JavaPlugin plugin, Consumer<CommandSender> commandConsumer) {
        this.command = new Command(this.commandName, "", "/", this.commandAlias) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (args[0].equalsIgnoreCase())
                if (sender.hasPermission(permission)) {
                    commandConsumer.accept(sender);
                }
                return false;
            }
        };
        System.out.println(
                "working:" + this.command.getName()
        );
        commandMap.register("AtlasCommand", this.command);
        plugin.getCommand(this.commandName).register(commandMap);
        commandMap.getCommands().forEach(command1 -> System.out.println(command1.getName()));
    }
}
