package me.rina.hyperpop.impl.module.impl.client;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.gui.impl.module.ModuleGUI;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author SrRina
 * @since 08/09/2021 at 14:59
 **/
public class ModuleUserInterface extends Module {
    public ModuleUserInterface() {
        super("UserInterface", "User interface for client.", ModuleType.CLIENT);
    }

    @Override
    public void onEnable() {
        if (!(mc.currentScreen instanceof ModuleGUI)) {
            mc.displayGuiScreen(Client.INSTANCE.guiModule);
        }
    }

    @Override
    public void onDisable() {}
}