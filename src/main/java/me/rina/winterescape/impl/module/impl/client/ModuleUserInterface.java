package me.rina.winterescape.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.winterescape.Client;
import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.api.value.type.Entry;
import me.rina.winterescape.impl.event.ClientTickEvent;
import me.rina.winterescape.impl.gui.GUI;
import org.lwjgl.input.Keyboard;

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

        this.setKey(Keyboard.KEY_P);
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
