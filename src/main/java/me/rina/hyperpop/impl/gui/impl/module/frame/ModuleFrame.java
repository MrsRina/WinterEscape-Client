package me.rina.hyperpop.impl.gui.impl.module.frame;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.widget.ModuleWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:45
 **/
public class ModuleFrame extends ImperadorFrame {
    private final int moduleType;

    private int titleHeight;
    private int distance = 1;

    public ModuleFrame(GUI gui, int moduleType) {
        super(gui, ModuleType.toString(moduleType));

        this.flag.setDraggable(true);
        this.flag.setResizable(false);

        this.moduleType = moduleType;
        this.titleHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());

        this.rect.setWidth(100);
    }

    public void init() {
        for (Module modules : Client.INSTANCE.moduleManager.getModuleList()) {
            if (modules.getType() != this.moduleType) {
                return;
            }

            ModuleWidget widget = new ModuleWidget(this.master, this, modules);
            widget.init();

            this.add(widget);
        }
    }

    public int getModuleType() {
        return moduleType;
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
        if (this.flag.isMouseOverDraggable() && button == 0) {
            this.setDrag();
            this.flag.setMouseClickedLeft(true);
        }

        super.onMouseClicked(button);
    }

    @Override
    public void onUpdate() {
        int size = this.getTitleHeight() + this.getDistance();

        this.rectDrag.set(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), size);

        for (IGUI elements : this.getElementList()) {
            elements.onUpdate();

            elements.getRect().setX(this.rect.getX());
            elements.getRect().setY(this.rect.getY() + size);

            size += elements.getRect().getHeight() + this.getDistance();
        }

        this.rect.setHeight(size);
    }

    @Override
    public void onCustomUpdate() {
        this.updateDrag();
        this.updateResize();

        this.updateMouseOver();

        for (IGUI elements : this.getElementList()) {
            elements.onCustomUpdate();
        }
    }

    @Override
    public void onRender() {
        // Background.
        Processor.prepare(Theme.INSTANCE.background);
        Processor.solid(this.rect);

        // Title.
        Processor.prepare(Theme.INSTANCE.string);
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.shadow$True$False(Theme.INSTANCE.background));

        for (IGUI elements : this.getElementList()) {
            elements.onRender();
        }
    }
}