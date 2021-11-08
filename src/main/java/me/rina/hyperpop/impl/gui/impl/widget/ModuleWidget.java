package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.api.value.type.Slider;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.hyperpop.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    protected float rotationValue;
    protected float interpolatedRotationValue;

    protected boolean boolOptionValue;
    protected boolean boolOptionValueSecond;

    private final List<Widget> loadedWidgetList = new ArrayList<>();
    private int widgetListHeight;

    private Texture textureArrow = Texturing.load("/assets/ui/arrow.png");

    public ModuleWidget(GUI gui, ModuleFrame mother, Module module) {
        super(gui, module.getTag());

        this.module = module;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.module.getTag()));
    }

    public void init() {
        this.textureArrow.load();
        this.clearList$Reload();
    }

    public void clearList$Reload() {
        this.loadedWidgetList.clear();

        for (Value values : module.getValueList()) {
            Value value = values;

            if (value instanceof CheckBox) {
                CheckBoxWidget widget = new CheckBoxWidget(this.master, this, (CheckBox) value);

                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (value instanceof Entry) {
                EntryWidget widget = new EntryWidget(this.master, this, (Entry) value);

                widget.init();

                this.loadedWidgetList.add(widget);
            } else if (value instanceof Slider) {
                SliderWidget widget = new SliderWidget(this.master, this, (Slider) value);

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

                this.rotationValue = this.flag.isEnabled() ? 90 : 34;
                this.getMother().reloadPositionConfiguration();
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
        this.rect.setY(this.getMother().getRect().getY() + this.getOffsetY());

        int offspace = GUI.SCALE_FACTOR;

        this.textureArrow.setX(this.rect.getX() + this.rect.getWidth() - this.textureArrow.getWidth() - offspace);
        this.textureArrow.setY(this.rect.getY() + this.rect.getHeight() - this.textureArrow.getHeight() - offspace);

        this.textureArrow.setWidth(this.rect.getWidth() / 8 + GUI.SCALE_FACTOR);
        this.textureArrow.setHeight(this.rect.getHeight() / GUI.SCALE_FACTOR);

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - (diff * 2));

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onUpdate();
        }
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()));

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
        Processor.solid(this.rect);

        // Texture arrow.
        if (this.boolOptionValue) {
            this.interpolatedRotationValue = Processor.interpolation(this.interpolatedRotationValue, this.rotationValue, this.master.getDisplay());
        }

        if (this.interpolatedRotationValue >= 360) {
            this.interpolatedRotationValue = 0;

            this.boolOptionValue = false;
        }

        Statement.matrix();
        Statement.rotate(this.interpolatedRotationValue, 0f, 0f, -1f);
        Texturing.renderPrimitive(this.textureArrow);
        Statement.refresh();

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect);

        // The tag.
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);

        // Render.
        for (Widget widgets : this.loadedWidgetList) {
            if (!widgets.getFlag().isEnabled() || !this.flag.isEnabled()) {
                continue;
            }

            widgets.onRender();
        }
    }

    @Override
    public void onCustomRender() {

    }
}
