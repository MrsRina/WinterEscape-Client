package me.rina.hyperpop.impl.gui;

import event.bus.EventListener;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.hyperpop.impl.module.management.ModuleManager;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.util.TurokDisplay;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:30
 **/
public class GUI extends GuiScreen {
    public static TurokFont FONT_BIG_NORMAL = new TurokFont(new Font("Verdana", 0, 18), true, true);
    public static TurokFont FONT_NORMAL = new TurokFont(new Font("Verdana", 0, 16), true, true);

    private final List<Frame> loadedFrameList = new ArrayList<>();

    private final TurokMouse mouse;
    private final TurokDisplay display;

    public GUI() {
        this.mouse = new TurokMouse();
        this.display = new TurokDisplay(mc);
    }

    public TurokMouse getMouse() {
        return mouse;
    }

    public TurokDisplay getDisplay() {
        return display;
    }

    public void init() {
        int offsetSpace = 10;

        for (Module modules : ModuleManager.INSTANCE.getModuleList()) {
            ModuleFrame frame = new ModuleFrame(this, modules);

            frame.getRect().setX(offsetSpace);
            frame.getRect().setY(10);

            offsetSpace += frame.getRect().getWidth();

            this.loadedFrameList.add(frame);
        }
    }

    @EventListener
    public void onUpdateEvent(ClientTickEvent event) {
        for (Frame frames : this.loadedFrameList) {
            frames.onUpdate();
            frames.onCustomUpdate();
        }
    }

    @Override
    public void initGui() {
        for (Frame frames : this.loadedFrameList) {
            frames.onOpen();
        }
    }

    @Override
    public void onGuiClosed() {
        for (Frame frames : this.loadedFrameList) {
            frames.onClose();
        }
    }

    @Override
    public void keyTyped(char charCode, int keyCode) {
        for (Frame frames : this.loadedFrameList) {
            frames.onKeyboard(charCode, keyCode);
            frames.onCustomKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseReleased(button);
            frames.onCustomMouseReleased(button);
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);
            frames.onCustomMouseClicked(button);
        }
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        this.display.setPartialTicks(partialTicks);
        this.mouse.setPos(mx, my);

        for (Frame frames : this.loadedFrameList) {
            frames.onRender();
        }
    }
}