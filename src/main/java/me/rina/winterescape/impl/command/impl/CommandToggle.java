package me.rina.winterescape.impl.command.impl;

import me.rina.winterescape.Client;
import me.rina.winterescape.api.command.Command;
import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.value.type.Combobox;
import me.rina.winterescape.impl.module.management.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;

/**
 * @author SrRina
 * @since 07/09/2021 at 16:21
 **/
public class CommandToggle extends Command {
    public CommandToggle() {
        super(new String[] {"toggle", "t"}, "Toggle an module.");
    }

    @Override
    public String syntax() {
        return "toggle/t <tag>";
    }

    @Override
    public void onCommand(String[] args) {
        String tag = null;

        if (args.length > 1) {
            tag = args[1];
        }

        if (args.length > 2 || tag == null) {
            splash();

            return;
        }

        final Module module = ModuleManager.get(tag);

        if (module == null) {
            Client.log(ChatFormatting.RED + "Unknown module, there is not no module with this tag.");

            return;
        }

        final Combobox combobox = (Combobox) module.getValue("Alert");
        final String save = combobox.getValue();

        combobox.setValue("Disabled");

        module.reload(!module.isEnabled());
        Client.log("Module " + module.getTag() + " " + (module.isEnabled() ? "toggled" : "disabled") + "!");

        combobox.setValue(save);
    }
}
