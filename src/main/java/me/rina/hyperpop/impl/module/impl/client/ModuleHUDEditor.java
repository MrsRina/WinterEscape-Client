package me.rina.hyperpop.impl.module.impl.client;

import event.bus.EventListener;
import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.value.type.Slider;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.impl.gui.GUI;

import java.awt.Color;

/**
 * @author Rina
 * 21/10/2021 at 00:38am
 **/
public class ModuleHUDEditor extends Module {
	public static ModuleHUDEditor INSTANCE;

	public static Slider settingRed = new Slider("Red", "Red color value of all HUD color mode.", 255, 0, 255);
	public static Slider settingGreen = new Slider("Green", "Green value of all HUD color mode.", 255, 0, 255);
	public static Slider settingBlue = new Slider("Blue", "Blue value of all HUD color mode.", 0, 0, 255);

	public static Entry settingEntry = new Entry("Hello", "Entry box.", "Holy Client");
	public static Color COLOR_HUD = new Color(255, 255, 255, 255); 

	public ModuleHUDEditor() {
		super("OverlayEditor", "HUD editor.", ModuleType.CLIENT);

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
	}

	@Override
	public void onEnable() {
	}

	public int clamp(int v) {
		return v <= 0 ? 0 : v >= 255 ? 255 : 0;
	}
}