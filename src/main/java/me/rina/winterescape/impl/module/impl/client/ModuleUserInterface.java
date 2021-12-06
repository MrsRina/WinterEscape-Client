package me.rina.winterescape.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.winterescape.Client;
import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.api.value.type.ColorPicker;
import me.rina.winterescape.api.value.type.Entry;
import me.rina.winterescape.impl.event.ClientTickEvent;
import me.rina.winterescape.impl.gui.GUI;
import me.rina.winterescape.impl.gui.api.theme.Theme;
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

    /* Color for themes. */
    public static ColorPicker settingBackground = new ColorPicker("Background", "Background color.", new Color(45, 45, 45, 255));
    public static ColorPicker settingFocused = new ColorPicker("Focused", "Focused color.", new Color(20, 20, 20, 255));
    public static ColorPicker settingSelected = new ColorPicker("Selected", "Selected color.", new Color(97, 97, 232, 200));
    public static ColorPicker settingHighlight = new ColorPicker("Highlight", "Highlight color.", new Color(255, 255, 255, 50));
    public static ColorPicker settingPressed = new ColorPicker("Pressed", "Pressed color.", new Color(255, 255, 0, 50));
    public static ColorPicker settingString = new ColorPicker("String", "String color.", new Color(255, 255, 255, 255));

    protected boolean lastFontOk;

    public ModuleUserInterface() {
        super("GUI", "User interface for client.", ModuleType.CLIENT);

        INSTANCE = this;

        this.setKey(Keyboard.KEY_P);
    }

    @Override
    public void onSetting() {
        Theme.INSTANCE.updateAllColor();

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
