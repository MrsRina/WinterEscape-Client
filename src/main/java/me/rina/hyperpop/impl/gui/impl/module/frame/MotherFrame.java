package me.rina.hyperpop.impl.gui.impl.module.frame;

import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:45
 **/
public class MotherFrame extends ImperadorFrame {
    private final TurokRect rectDrag = new TurokRect(0, 0, 0, 0);

    public MotherFrame(GUI gui) {
        super(gui, "mother:frame");

        this.flag.setDraggable(true);
        this.flag.setResizable(true);
    }

    @Override
    public void onMouseReleased(int button) {
        this.unset();

        super.onMouseReleased(button);
    }

    @Override
    public void onMouseClicked(int button) {
        this.setDrag(this.rectDrag);
        this.setResize();

        super.onMouseClicked(button);
    }

    @Override
    public void onUpdate() {
        this.updateDrag();
        this.updateResize();
    }

    @Override
    public void onRender() {

    }
}