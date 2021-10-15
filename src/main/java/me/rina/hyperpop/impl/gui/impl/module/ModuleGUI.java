package me.rina.hyperpop.impl.gui.impl.module;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.hyperpop.impl.module.management.ModuleManager;
import me.rina.turok.render.font.TurokFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:30
 **/
public class ModuleGUI extends GUI {
    public static TurokFont FONT_BIG_NORMAL = new TurokFont(new Font("Verdana", 0, 18), true, true);
    public static TurokFont FONT_NORMAL = new TurokFont(new Font("Verdana", 0, 16), true, true);

    private final List<Frame> loadedFrameList = new ArrayList<>();

    public ModuleGUI() {
        super();
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

    @Override
    public void onOpen() {
        for (Frame frames : this.loadedFrameList) {
            frames.onOpen();
        }
    }

    @Override
    public void onClose() {
        for (Frame frames : this.loadedFrameList) {
            frames.onClose();
        }
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        for (Frame frames : this.loadedFrameList) {
            frames.onKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {
        for (Frame frames : this.loadedFrameList) {
            frames.onCustomKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onUpdate() {
        for (Frame frames : this.loadedFrameList) {
            frames.onUpdate();
        }
    }

    @Override
    public void onCustomUpdate() {
        for (Frame frames : this.loadedFrameList) {
            frames.onCustomUpdate();
        }
    }

    @Override
    public void onRender() {
        for (Frame frames : this.loadedFrameList) {
            frames.onRender();
        }
    }

    @Override
    public void onCustomRender() {
        for (Frame frames : this.loadedFrameList) {
            frames.onCustomRender();
        }
    }
}