package me.rina.hyperpop.impl.gui.impl.module.frame;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:45
 **/
public class ModuleFrame extends ImperadorFrame {
    private final TurokRect rectDrag = new TurokRect(0, 0, 0, 0);
    private final Module theModule;

    private int titleHeight;
    private int distance = 1;

    public ModuleFrame(GUI gui, Module module) {
        super(gui, module.getTag());

        this.flag.setDraggable(false);
        this.flag.setResizable(true);

        this.theModule = module;
        this.titleHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());

        this.rect.setWidth(100);
    }

    public Module getTheModule() {
        return theModule;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
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

        int size = this.getTitleHeight() + this.getDistance();

        this.rect.setHeight(size);
    }

    @Override
    public void onRender() {
        Processor.prepare(Theme.INSTANCE.background);
        Processor.solid(this.rect);
    }
}