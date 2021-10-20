package me.rina.hyperpop.impl.gui.impl.module.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;

import java.util.ArrayList;
import java.util.HashMap;
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

    private final List<Widget> loadedWidgetList = new ArrayList<>();
    private int widgetListHeight;

    public ModuleWidget(GUI gui, ModuleFrame mother, Module module) {
        super(gui, module.getTag());

        this.module = module;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.module.getTag()));
    }

    public void init() {
        this.clearList$Reload();
    }

    public void clearList$Reload() {
        this.loadedWidgetList.clear();

        for (Map.Entry<String, Value> entry : this.module.getRegister().entrySet()) {
            Value value = entry.getValue();

            if (value instanceof CheckBox) {
                CheckBoxWidget widget = new CheckBoxWidget(this.master, this, (CheckBox) value);

                this.loadedWidgetList.add(widget);
            } else if (value instanceof Entry) {
                EntryWidget widget = new EntryWidget(this.master, this, (Entry) value);

                this.loadedWidgetList.add(widget);
            }
        }
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
        if (this.flag.isResizing()) {
            this.flag.setResizing(false);
        }

        if (this.flag.isDragging()) {
            this.flag.setDragging(false);
        }

        if (this.flag.isMouseClickedLeft()) {
            this.module.reloadListener();
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            this.flag.setEnabled(!this.flag.isEnabled());
            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flag.isMouseOver() && (button == 0 || button == 2)) {
            this.flag.setMouseClickedLeft(button == 0);
            this.flag.setMouseClickedRight(button == 2);
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
    }

    @Override
    public void onUpdate() {
        int diff = 1;

        this.rect.setWidth(this.getMother().getRect().getWidth() - (diff * 2));

        int size = 1;

        for (Widget widgets : this.loadedWidgetList) {
            if (!this.flag.isEnabled()) {
                continue;
            }

            if (!widgets.getFlag().isEnabled()) {
                continue;
            }

            widgets.getRect().setX(this.rect.getX());
            widgets.getRect().setY(this.rect.getY() + size);

            size += widgets.getRect().getHeight() + this.master.getDistance();

            widgets.onUpdate();
        }

        this.widgetListHeight = size;
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.module.isEnabled() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getSelected(this.interpolatedSelectedAlpha));
        Processor.solid(this.rect);

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect);

        // The tag.
        Processor.prepare(Theme.INSTANCE.string);
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.shadow$True$False(Theme.INSTANCE.background));

        // Render.
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();
        }
    }

    @Override
    public void onCustomRender() {

    }
}
