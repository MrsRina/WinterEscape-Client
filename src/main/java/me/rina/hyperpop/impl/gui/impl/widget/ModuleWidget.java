package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.overlay.OverlayElement;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.api.value.type.*;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.backend.Textures;
import me.rina.hyperpop.impl.gui.impl.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 17/10/2021 at 12:52
 **/
public class ModuleWidget extends Widget {
    private final ModuleFrame mother;
    private final Module module;

    protected int interpolatedSelectedAlpha;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;
    protected int interpolatedArrowTick;

    private final List<Widget> loadedWidgetList = new ArrayList<>();
    private int widgetListHeight;

    private final Texture textureArrow = new Texture(null, 0, 0);

    public ModuleWidget(GUI gui, ModuleFrame mother, Module module) {
        super(gui, module.getTag());

        if (module instanceof OverlayElement) {
            final OverlayElement overlayElement = (OverlayElement) module;

            this.master.addOverlayElement(overlayElement);
            this.rect.setTag(overlayElement.getRect().getTag());
        }

        this.module = module;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.module.getTag()));

        Textures.set(this.textureArrow, Texturing.get(Textures.UI_ARROW_DOWN));
    }

    public void init() {
        this.textureArrow.load();
        this.clearList$Reload();
    }

    public void clearList$Reload() {
        this.loadedWidgetList.clear();

        for (Value values : module.getValueList()) {
            if (values instanceof CheckBox) {
                CheckBoxWidget widget = new CheckBoxWidget(this.master, this, (CheckBox) values);
                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (values instanceof Entry) {
                EntryWidget widget = new EntryWidget(this.master, this, (Entry) values);
                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (values instanceof Slider) {
                SliderWidget widget = new SliderWidget(this.master, this, (Slider) values);
                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (values instanceof BindBox) {
                BindBoxWidget widget = new BindBoxWidget(this.master, this, (BindBox) values);
                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (values instanceof Combobox) {
                ComboboxWidget widget = new ComboboxWidget(this.master, this, (Combobox) values);
                widget.init();

                this.loadedWidgetList.add(widget);
            }
        }

        this.reloadPositionConfiguration();
    }

    public void reloadPositionConfiguration() {
        int size = (int) this.rect.getHeight() + this.master.getDistance();
        int j = 0;

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets.getFlag().isEnabled()) {
                widgets.setOffsetY((float) size);

                size += (int) widgets.getRect().getHeight() + this.master.getDistance();
                j++;
            }
        }

        this.setWidgetListHeight((int) size);
    }

    public ModuleFrame getMother() {
        return mother;
    }

    public Module getModule() {
        return module;
    }

    public void setWidgetListHeight(int widgetListHeight) {
        this.widgetListHeight = widgetListHeight;
    }

    public int getWidgetListHeight() {
        return widgetListHeight;
    }

    @Override
    public void onOpen() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.clear();

            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onClose();
        }
    }

    @Override
    public void onClose() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.clear();

            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onClose();
        }
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {
        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onCustomKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flag.isResizing()) {
            this.flag.setResizing(false);
        }

        if (this.flag.isDragging()) {
            this.flag.setDragging(false);
        }

        if (this.flag.isMouseClickedLeft()) {
            if (this.flag.isMouseOver()) {
                this.module.reloadListener();
            }

            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            if (this.flag.isMouseOver()) {
                this.flag.setEnabled(!this.flag.isEnabled());
                this.getMother().reloadPositionConfiguration();

                Textures.set(this.textureArrow, this.flag.isEnabled() ? Texturing.get(Textures.UI_ARROW_UP) : Texturing.get(Textures.UI_ARROW_DOWN));
                this.interpolatedArrowTick = 0;
            }

            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }

        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flag.isMouseOver() && (button == 0 || button == 1)) {
            this.flag.setMouseClickedLeft(button == 0);
            this.flag.setMouseClickedRight(button == 1);
        }

        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onMouseClicked(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void clear() {
        this.flag.setMouseOver(false);
        this.flag.setResizable(false);
        this.flag.setDraggable(false);

        for (Widget widgets : this.loadedWidgetList) {
            widgets.clear();
        }
    }

    @Override
    public void onUpdate() {
        this.rect.setX(this.getMother().getRect().getX() + this.getOffsetX());
        this.rect.setY(this.getMother().getRect().getY() + this.mother.getOffsetY() + this.getOffsetY());

        float off_space = 2;
        float size = (this.rect.getHeight() - (off_space * 2));

        this.textureArrow.setX(this.rect.getX() + this.rect.getWidth() - this.textureArrow.getWidth() - 1f);
        this.textureArrow.setY(this.rect.getY() + off_space);

        this.textureArrow.setWidth(size);
        this.textureArrow.setHeight(size);

        this.textureArrow.setTextureWidth((int) this.textureArrow.getWidth());
        this.textureArrow.setTextureHeight((int) this.textureArrow.getHeight());

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - (diff * 2));

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onUpdate();
        }
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getProtectedScrollRect().collideWithMouse(this.master.getMouse()));

        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onCustomUpdate();
        }
    }

    @Override
    public void onRender() {
        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.module.isEnabled() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getSelected(this.interpolatedSelectedAlpha));
        Processor.solid(this.rect.x, this.rect.y - this.master.getDistance(), this.rect.width, this.rect.height + (this.master.getDistance() * 2));

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect.x, this.rect.y - this.master.getDistance(), this.rect.width, this.rect.height + (this.master.getDistance() * 2));

        // Texture arrow.
        this.interpolatedArrowTick = Processor.interpolation(this.interpolatedArrowTick, 255, this.master.getDisplay().getPartialTicks() * 0.1f);

        Texturing.render(this.textureArrow);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay().getPartialTicks());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect.x, this.rect.y - this.master.getDistance(), this.rect.width, this.rect.height + (this.master.getDistance() * 2));

        // The tag.
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);

        // Render.
        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onRender();
            Processor.setScissor(this.mother.getProtectedScrollRect(), this.master.getDisplay());
        }
    }

    @Override
    public void onCustomRender() {

    }
}
