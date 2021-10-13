package me.rina.hyperpop.impl.command.management;

import me.rina.hyperpop.api.command.Command;
import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.api.value.ValueGeneric;
import me.rina.hyperpop.impl.command.impl.CommandPrefix;
import me.rina.hyperpop.impl.command.impl.CommandSocial;
import me.rina.hyperpop.impl.command.impl.CommandToggle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 06/09/2021 at 21:43
 **/
public class CommandManager extends Feature {
    public static CommandManager INSTANCE;

    private final List<Command> commandList = new ArrayList<>();
    private final ValueGeneric<String> prefixValue = new ValueGeneric<>("Prefix", "Sets prefix shit.", "-");

    public CommandManager() {
        super("Command", "Add commands & process them.");

        INSTANCE = this;
    }

    public void preInitAll() {
        this.add(new CommandPrefix());
        this.add(new CommandToggle());
        this.add(new CommandSocial());
    }

    public void setPrefix(String prefix) {
        this.prefixValue.setValue(prefix);
    }

    public String getPrefix() {
        return prefixValue.getValue();
    }

    public void add(Command command) {
        this.commandList.add(command);
    }

    public Command getCommand(String string) {
        Command command = null;

        for (Command commands : this.getCommandList()) {
            if (commands.contains(string)) {
                command = commands;

                break;
            }
        }

        return command;
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}
