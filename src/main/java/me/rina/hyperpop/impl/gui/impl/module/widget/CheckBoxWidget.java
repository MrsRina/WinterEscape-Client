package me.rina.hyperpop.impl.gui.impl.module.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;

/**
 * @author SrRina
 * @since 18/10/2021 at 19:08
 **/
public class CheckBoxWidget extends Widget {
    private final ModuleWidget mother;
    private final CheckBox value;

    protected int interpolatedSelectedAlpha;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;

    public CheckBoxWidget(GUI gui, ModuleWidget mother, CheckBox value) {
        super(gui, value.getTag());

        this.value = value;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.value.getTag()));
    }

    public void init() {

    }

    public ModuleWidget getMother() {
        return mother;
    }

    public CheckBox getValue() {
        return value;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {
        this.clear();
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onMouseReleased(int button) {
        boolean release = false;

        if (this.flag.isResizing()) {
            this.flag.setResizing(false);
        }

        if (this.flag.isDragging()) {
            this.flag.setDragging(false);
        }

        if (this.flag.isMouseClickedLeft()) {
            release = this.flag.isMouseOver();

            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            release = this.flag.isMouseOver();

            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }

        if (release) {
            this.value.setValue(!this.value.getValue());
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
        this.flag.setEnabled(this.value.isShow());
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.value.getValue() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());

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
        Processor.prepareString(Theme.INSTANCE.string);
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.shadow$True$False(Theme.INSTANCE.background));
    }

    @Override
    public void onCustomRender() {

    }
}
