package me.rina.hyperpop.impl.gui.impl.frame;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.overlay.OverlayElement;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.imperador.frame.ImperadorFrame;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.backend.Textures;
import me.rina.hyperpop.impl.gui.impl.widget.ModuleWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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

    private final Texture textureGeneric = new Texture(null, 0, 0);

    public ModuleFrame(GUI gui, int moduleType) {
        super(gui, ModuleType.toString(moduleType));

        this.flag.setDraggable(true);
        this.flag.setResizable(false);

        this.moduleType = moduleType;
        this.titleHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());

        this.rect.setWidth(100);

        Texture texture = Texturing.get("/assets/generic/" + ModuleType.toString(moduleType).toLowerCase() + ".png");

        if (texture != null) {
            Textures.set(this.textureGeneric, Texturing.get("/assets/generic/" + ModuleType.toString(moduleType).toLowerCase() + ".png"));
        }
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
        int size = this.getTitleHeight() + this.master.getDistance() * 2;
        int last = this.getElementList().size();
        int i = 0;

        for (IGUI elements : this.getElementList()) {
            if (elements instanceof ModuleWidget) {
                ModuleWidget widget = (ModuleWidget) elements;

                widget.setOffsetY(size);
                widget.reloadPositionConfiguration();

                i++;

                int added = 0;

                if (i == last) {
                    added = 1;
                }

                if (widget.getFlag().isEnabled()) {
                    size += widget.getWidgetListHeight() + added;
                } else {
                    size += widget.getRect().getHeight() + added;
                }
            }
        }

        this.sizeamount = size;
    }

    public void updateScroll() {
        if (this.rect.getHeight() != GUI.HEIGHT_LIMIT) {
            return;
        }

        float theDiff = this.rect.getHeight() - this.sizeamount;

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
        this.setOffsetY(Processor.interpolation(this.getOffsetY(), this.scrollamount, this.master.getDisplay()));
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
        this.titleHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());

        this.rectDrag.set(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.getTitleHeight());
        this.scrollRect.set(this.rect.getX(), this.rect.getY() + this.getTitleHeight() + this.master.getDistance() * 2, this.rect.getWidth(), this.rect.getHeight() - this.getTitleHeight());

        float off_space = 2;
        float size = (this.getTitleHeight() - 1);

        this.textureGeneric.setX(this.rect.getX() + this.rect.getWidth() - this.textureGeneric.getWidth() - off_space);
        this.textureGeneric.setY(this.rect.getY() + 1);

        this.textureGeneric.setWidth(size);
        this.textureGeneric.setHeight(size);

        this.textureGeneric.setTextureWidth((int) size);
        this.textureGeneric.setTextureHeight((int) size);

        this.rect.setHeight(Processor.interpolation(this.rect.getHeight(), Processor.clamp(this.sizeamount, 0, GUI.HEIGHT_LIMIT), this.master.getDisplay()));
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

        // Focused.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.rect.x, this.rect.y, this.rect.width, this.getTitleHeight() + 1f);

        // Background.
        Processor.prepare(Theme.INSTANCE.background);
        Processor.solid(this.rect.x, this.rect.y + this.getTitleHeight() + this.master.getDistance(), this.rect.width, this.rect.height - this.getTitleHeight() - this.master.getDistance());

        // Outline stuff.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.outline(this.rect.x + 0.1f, this.rect.y + this.getTitleHeight() + this.master.getDistance(), this.rect.width - 0.5f, this.rect.height - this.getTitleHeight() - this.master.getDistance() - 0.2f);

        // Render the icon.
        Texturing.render(this.textureGeneric);

        // Title.
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);

        for (IGUI elements : this.getElementList()) {
            Statement.set(GL11.GL_SCISSOR_TEST);
            Processor.setScissor(this.scrollRect, this.master.getDisplay());

            elements.onRender();

            Processor.setScissor(this.scrollRect, this.master.getDisplay());
            Processor.unsetScissor();
        }
    }
}