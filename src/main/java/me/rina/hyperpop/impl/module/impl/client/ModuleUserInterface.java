package me.rina.hyperpop.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import scala.xml.parsing.FactoryAdapter;

/**
 * @author SrRina
 * @since 08/09/2021 at 14:59
 **/
public class ModuleUserInterface extends Module {
    public static ModuleUserInterface INSTANCE;

    public static CheckBox valueExample = new CheckBox("Button", "A button.", false);

    public ModuleUserInterface() {
        super("UserInterface", "User interface for client.", ModuleType.CLIENT);

        INSTANCE = this;
    }

    @Override
    public void onSetting() {
        valueExample.setShow(true);
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
    public void onEnable() {
        if (GUI.HUD_EDITOR) {
            ModuleHUDEditor.INSTANCE.unsetListener();

            GUI.HUD_EDITOR = false;
        }
    }

    @Override
    public void onDisable() {
        if (GUI.HUD_EDITOR) {
            Client.INSTANCE.userInterfaceGUI.onGuiClosed();

            GUI.HUD_EDITOR = false;
        }
    }
}
