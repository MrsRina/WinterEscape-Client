package me.rina.hyperpop.impl.gui.impl.frame;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.widget.ModuleWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Mouse;

import java.awt.*;

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

    private Texture textureGeneric;

    public ModuleFrame(GUI gui, int moduleType) {
        super(gui, ModuleType.toString(moduleType));

        this.flag.setDraggable(true);
        this.flag.setResizable(false);

        this.moduleType = moduleType;
        this.titleHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());

        this.rect.setWidth(100);
        this.textureGeneric = Texturing.load("/assets/generic/" + ModuleType.toString(moduleType).toLowerCase() + ".png");
    }

    public void init() {
        this.textureGeneric.load();

        for (Module modules : Client.INSTANCE.moduleManager.getModuleList()) {
            if (modules.getType() != this.moduleType) {
                continue;
            }

            ModuleWidget widget = new ModuleWidget(this.master, this, modules);
            widget.init();

            this.add(widget);
        }

        this.reloadPositionConfiguration();
    }

    public void reloadPositionConfiguration() {
        int size = this.getTitleHeight() + this.master.getDistance();

        for (IGUI elements : this.getElementList()) {
            if (elements instanceof ModuleWidget) {
                ModuleWidget widget = (ModuleWidget) elements;

                widget.setOffsetY(size);
                widget.reloadPositionConfiguration();

                if (widget.getFlag().isEnabled()) {
                    size += widget.getWidgetListHeight();
                } else {
                    size += widget.getRect().getHeight() + this.master.getDistance();
                }
            }
        }

        this.rect.setHeight(Processor.clamp(size, 0, GUI.HEIGHT_LIMIT));
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

        this.scrollamount = Processor.clamp(this.scrollamount, theDiff, 0);
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

        for (IGUI elements : this.getElementList()) {
            if (elements instanceof ModuleWidget) {
                ModuleWidget widget = (ModuleWidget) elements;
                
                widget.clear();
            }
        }
    }

    @Override
    public void onUpdate() {
        this.rectDrag.set(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.getTitleHeight());
        this.scrollRect.set(this.rect.getX(), this.rect.getY() + this.getTitleHeight(), this.rect.getWidth(), this.rect.getHeight() - this.getTitleHeight());

        int offspace = 2;

        this.textureGeneric.setX(this.rect.getX() + this.rect.getWidth() - this.textureGeneric.getWidth() - offspace);
        this.textureGeneric.setY(this.rect.getY() + this.rect.getHeight() - this.textureGeneric.getHeight() - offspace);

        this.textureGeneric.setWidth(this.rect.getWidth() / 6);
        this.textureGeneric.setHeight(this.rect.getHeight() / 2);

        this.flag.setEnabled(GUI.HUD_EDITOR == (this.moduleType == ModuleType.HUD));

        for (IGUI elements : this.getElementList()) {
            elements.onUpdate();
        }
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

        // Render the icon.
        Texturing.render(this.textureGeneric);

        // Title.
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);

        for (IGUI elements : this.getElementList()) {
            elements.onRender();
        }
    }
}