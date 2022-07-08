package command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AtlasSubCommand {
   @Getter
   private String commandName;
   @Getter
   private BiConsumer<CommandSender, String[]> consumerSender;
   @Getter
   private String permission;
   @Getter
   private String description;
   private static AtlasSubCommand instance;

    static {
        instance = new AtlasSubCommand();
    }

    public static AtlasSubCommand builder() {
        return instance;
    }

    public AtlasSubCommand() {
        this.commandName = "sub";
        this.permission = "subPerm";
        this.description = "subDesc";
    }

    public AtlasSubCommand setCommandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public AtlasSubCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public AtlasSubCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public AtlasSubCommand setExecutor(BiConsumer<CommandSender, String[]> consumerSender) {
        this.consumerSender = consumerSender;
        return this;
    }

    public void build(AtlasCommand command) {
        if (Objects.equals(commandName, "sub")) return;
        if (Objects.equals(permission, "subPerm")) return;
        if (Objects.equals(description, "subDesc")) return;
        command.addSubCommands(this);
    }
}
