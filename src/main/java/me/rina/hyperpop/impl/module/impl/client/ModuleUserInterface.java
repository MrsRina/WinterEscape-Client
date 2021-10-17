package me.rina.hyperpop.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.impl.gui.GUI;

/**
 * @author SrRina
 * @since 08/09/2021 at 14:59
 **/
public class ModuleUserInterface extends Module {
    public ModuleUserInterface() {
        super("UserInterface", "User interface for client.", ModuleType.CLIENT);
    }

    @EventListener
    public void onClientTickEvent(ClientTickEvent event) {
        if (mc.world == null) {
            return;
        }

        if (mc.currentScreen != Client.INSTANCE.userInterfaceGUI) {
            mc.displayGuiScreen(Client.INSTANCE.userInterfaceGUI);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
