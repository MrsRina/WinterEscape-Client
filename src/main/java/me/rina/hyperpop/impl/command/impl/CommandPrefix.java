package me.rina.hyperpop.impl.command.impl;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.command.Command;

/**
 * @author SrRina
 * @since 07/09/2021 at 13:37
 **/
public class CommandPrefix extends Command {
    public CommandPrefix() {
        super(new String[] {"prefix"}, "Sets prefix of chat commands on client.");
    }

    @Override
    public String syntax() {
        return "prefix <char>";
    }

    @Override
    public void onCommand(String[] args) {
        String prefix = null;

        if (args.length > 1) {
            prefix = args[1];
        }

        if (args.length > 2 || prefix == null) {
            this.splash();

            return;
        }

        Client.INSTANCE.commandManager.setPrefix(prefix);
        Client.log("Prefix changed to " + prefix);
    }
}
