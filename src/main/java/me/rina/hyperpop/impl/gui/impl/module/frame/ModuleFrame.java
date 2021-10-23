package me.rina.hyperpop.impl.gui.impl.module.frame;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.widget.ModuleWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Mouse;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:45
 **/
public class ModuleFrame extends ImperadorFrame {
    private final int moduleType;

    private int titleHeight;

    protected float sizeamount;
    protected float scrollamount;

    protected boolean hasWheel;
    protected final TurokRect scrollRect = new TurokRect("rekt", 0, 0);

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
                continue;
            }

            ModuleWidget widget = new ModuleWidget(this.master, this, modules);
            widget.init();

            this.add(widget);
        }
    }

    public void updateScroll() {
        if (this.rect.getHeight() != GUI.HEIGHT_LIMIT) {
            return;
        }

        float theDiff = this.rect.getHeight() - this.sizeamount;

        if (this.master.getMouse().hasWheel()) {
            this.scrollamount -= this.master.getMouse().getScroll();
        }

        int i = -((this.hasWheel ? Mouse.getDWheel() : 0) / 10);

        if (this.scrollRect.collideWithMouse(this.master.getMouse())) {
            this.hasWheel = Mouse.hasWheel();
        } else {
            this.hasWheel = false;
        }

        if (this.hasWheel) {
            this.scrollamount -= i;
        }

        this.scrollamount = TurokMath.clamp(this.scrollamount, theDiff, 0);
    }

    public TurokRect getProtectedScrollRect() {
        return scrollRect;
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

    @Override
    public void onMouseReleased(int button) {
        this.unset();

        super.onMouseReleased(button);
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.master.matrixMoveFocusedFrameToLast();

        super.onCustomMouseReleased(button);
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
    public void onCustomMouseClicked(int button) {
        this.master.matrixMoveFocusedFrameToLast();

        super.onCustomMouseClicked(button);
    }

    @Override
    public void clear() {
        this.flag.setMouseOver(false);
        this.flag.setMouseOverDraggable(false);
        this.flag.setMouseOverResizable(false);
    }

    @Override
    public void onUpdate() {
        int size = this.getTitleHeight() + this.master.getDistance();

        this.rectDrag.set(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), size);
        this.scrollRect.set(this.rect.getX(), this.rect.getY() + this.getTitleHeight(), this.rect.getWidth(), this.rect.getHeight() - this.getTitleHeight());

        this.flag.setEnabled(GUI.HUD_EDITOR == (this.moduleType == ModuleType.HUD));

        for (IGUI elements : this.getElementList()) {
            if (elements instanceof Widget) {
                ((Widget) elements).clear();
            }

            int diff = 1;

            elements.getRect().setX(this.rect.getX() + diff);
            elements.getRect().setY(this.rect.getY() + size + this.scrollamount);

            size += elements.getRect().getHeight() + this.master.getDistance();

            if (elements instanceof ModuleWidget && elements.getFlag().isEnabled()) {
                size += ((ModuleWidget) elements).getWidgetListHeight();
            }

            elements.onUpdate();
        }

        this.sizeamount = size;
        this.rect.setHeight(Math.min(size, GUI.HEIGHT_LIMIT));
    }

    @Override
    public void onCustomUpdate() {
        this.updateMouseOver();
        this.updateResize();

        for (IGUI elements : this.getElementList()) {
            elements.onCustomUpdate();
        }
    }

    @Override
    public void onRender() {
        // We need an smooth scroll and drag, basically;
        this.updateDrag();
        this.updateScroll();

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