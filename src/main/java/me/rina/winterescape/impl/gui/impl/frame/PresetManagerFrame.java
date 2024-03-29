package me.rina.winterescape.impl.gui.impl.frame;

import me.rina.winterescape.impl.gui.GUI;
import me.rina.winterescape.impl.gui.api.imperador.frame.ImperadorFrame;

/**
 * @author SrRina
 * @since 26/11/2021 at 16:38
 **/
public class PresetManagerFrame extends ImperadorFrame {
    public PresetManagerFrame(GUI master) {
        super(master, "Preset Manager");

        this.flag.setResizable(true);
        this.flag.setDraggable(true);
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onMouseReleased(int button) {

    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {

    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onCustomUpdate() {

    }

    @Override
    public void onRender() {

    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void clear() {
        this.flag.setMouseOverDraggable(false);
        this.flag.setMouseOverResizable(false);
        this.flag.setMouseOver(false);
    }
}
