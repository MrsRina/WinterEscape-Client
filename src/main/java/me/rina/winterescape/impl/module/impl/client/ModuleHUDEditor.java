package me.rina.winterescape.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.winterescape.Client;
import me.rina.winterescape.api.module.Module;
import me.rina.winterescape.api.value.type.ColorPicker;
import me.rina.winterescape.api.value.type.Slider;
import me.rina.winterescape.api.value.type.Entry;
import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.impl.event.ClientTickEvent;
import me.rina.winterescape.impl.gui.GUI;

import java.awt.*;

/**
 * @author Rina
 * @since 21/10/2021 at 00:38am
 **/
public class ModuleHUDEditor extends Module {
	public static ModuleHUDEditor INSTANCE;
	public static Color COLOR_HUD = new Color(255, 255, 255, 255);

	/* Color font. */
	public static ColorPicker settingColor = new ColorPicker("Orange", "Yellow", Color.BLACK);

	/* By default I want Arial. */
	public static Entry settingFont = new Entry("Font", "The font for overlay.", "Arial");

	public ModuleHUDEditor() {
		super("Overlay", "HUD editor.", ModuleType.CLIENT);

		INSTANCE = this;
	}

	@Override
	public void onSetting() {
		COLOR_HUD = settingColor.getColor();

		GUI.HUD_EDITOR = this.isEnabled();
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
	public void onDisable() {
		Client.INSTANCE.userInterfaceGUI.onGuiClosed();

		if (!settingFont.getValue().equalsIgnoreCase(Client.OVERLAY_FONT.getFontName())) {
			Font font = new Font(settingFont.getValue(), 0, 16);

			if (font.getFontName().equalsIgnoreCase("Default")) {
				font = new Font("Tahoma", 0,16);
			}

			Client.OVERLAY_FONT.setFont(font);
		}
	}

	@Override
	public void onEnable() {
	}

	public int clamp(int v) {
		return v <= 0 ? 0 : v >= 255 ? 255 : 0;
	}
}