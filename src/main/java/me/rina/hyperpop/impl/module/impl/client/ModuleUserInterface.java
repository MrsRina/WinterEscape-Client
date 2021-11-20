package me.rina.hyperpop.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import scala.xml.parsing.FactoryAdapter;

import java.awt.*;

/**
 * @author SrRina
 * @since 08/09/2021 at 14:59
 **/
public class ModuleUserInterface extends Module {
    public static ModuleUserInterface INSTANCE;

    /* By default I want this font. */
    public static Entry settingFont = new Entry("Font", "The font used.", "Tahoma");

    protected boolean lastFontOk;

    public ModuleUserInterface() {
        super("GUI", "User interface for client.", ModuleType.CLIENT);

        INSTANCE = this;
    }

    @Override
    public void onSetting() {
        if (!settingFont.isFocused()) {
            if (!this.lastFontOk) {
                if (!settingFont.getValue().equals(GUI.FONT_NORMAL.getFontName())) {
                    Font font = new Font(settingFont.getValue(), 0, 16);
                    settingFont.setValue(font.getFontName());

                    Client.INSTANCE.userInterfaceGUI.refreshConfiguration();
                    GUI.FONT_NORMAL.setFont(font);
                }

                this.lastFontOk = true;
            }
        } else {
            this.lastFontOk = false;
        }
    }

    @Override
    public void onShutdown() {
        this.unsetListener();
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
            GUI.HUD_EDITOR = false;
        }

        if (mc.currentScreen == Client.INSTANCE.userInterfaceGUI && mc.world != null) {
            mc.displayGuiScreen(null);
        }
    }
}
