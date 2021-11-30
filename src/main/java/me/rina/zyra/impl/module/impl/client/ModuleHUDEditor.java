package me.rina.zyra.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.zyra.Client;
import me.rina.zyra.api.module.Module;
import me.rina.zyra.api.value.type.ColorPicker;
import me.rina.zyra.api.value.type.Slider;
import me.rina.zyra.api.value.type.Entry;
import me.rina.zyra.api.module.type.ModuleType;
import me.rina.zyra.impl.event.ClientTickEvent;
import me.rina.zyra.impl.gui.GUI;

import java.awt.*;

/**
 * @author Rina
 * @since 21/10/2021 at 00:38am
 **/
public class ModuleHUDEditor extends Module {
	public static ModuleHUDEditor INSTANCE;

	public static Slider settingRed = new Slider("Red", "Red color value of all HUD color mode.", 255, 0, 255);
	public static Slider settingGreen = new Slider("Green", "Green value of all HUD color mode.", 255, 0, 255);
	public static Slider settingBlue = new Slider("Blue", "Blue value of all HUD color mode.", 0, 0, 255);

	public static ColorPicker settingColor = new ColorPicker("Orange", "Yellow", Color.BLACK);

	public static Entry settingFont = new Entry("Font", "The font for overlay.", "Arial");
	public static Color COLOR_HUD = new Color(255, 255, 255, 255); 

	public ModuleHUDEditor() {
		super("Overlay", "HUD editor.", ModuleType.CLIENT);

		INSTANCE = this;
	}

	@Override
	public void onSetting() {
		if (COLOR_HUD.getRed() != settingRed.getValue().intValue() || COLOR_HUD.getGreen() != settingGreen.getValue().intValue() || COLOR_HUD.getBlue() != settingBlue.getValue().intValue()) {
			COLOR_HUD = new Color(this.clamp(settingRed.getValue().intValue()), this.clamp(settingGreen.getValue().intValue()), this.clamp(settingBlue.getValue().intValue()), 255);
		}

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