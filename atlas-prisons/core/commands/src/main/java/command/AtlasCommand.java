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
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AtlasCommand {

    private String commandName;
    private String permission;
    private String description;
    private List<String> commandAlias;
    private List<AtlasSubCommand> subCommands;
    private Command command;
    private SimpleCommandMap commandMap;
    private Consumer<CommandSender> consumerSender;

    public static AtlasCommand instance;

    static {
        instance = new AtlasCommand();
    }

    public AtlasCommand() {
        this.commandName = "test";
        this.permission = "test";
        this.description = "Description";
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

    public AtlasCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public void addSubCommands(AtlasSubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    public AtlasCommand setExecutor(Consumer<CommandSender> consumerSender) {
        this.consumerSender = consumerSender;
        return this;
    }

    public void build(JavaPlugin plugin) {
        this.command = new Command(this.commandName, this.description, "/", this.commandAlias) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                switch (args.length) {
                    case 0 -> {
                        if (sender.hasPermission(permission)) {
                            consumerSender.accept(sender);
                        }
                    }
                    case 1 -> subCommands.forEach(subCommand -> {
                        if (args[0].equalsIgnoreCase(subCommand.getCommandName())) {
                            if (sender.hasPermission(subCommand.getPermission())) {
                                subCommand.getConsumerSender().accept(sender, args);
                            }
                        }
                    });
                }
                return false;
            }
        };
        commandMap.register("AtlasCommand", this.command);
        plugin.getCommand(this.commandName).register(commandMap);
    }
}
