package me.rina.hyperpop.impl.gui.impl.container;

import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.container.Container;
import me.rina.hyperpop.impl.gui.impl.frame.PresetManagerFrame;

/**
 * @author SrRina
 * @since 26/11/2021 at 18:00
 **/
public class PresetContainer extends Container {
    private final PresetManagerFrame mother;

    public PresetContainer(GUI master, PresetManagerFrame mother) {
        super(master, "PresetContainer");

        this.mother = mother;
    }

    public PresetManagerFrame getMother() {
        return mother;
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

    }
}
