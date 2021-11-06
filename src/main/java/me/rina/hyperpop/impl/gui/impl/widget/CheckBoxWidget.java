package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
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

    private final Texture textureCheckBox = Texturing.load("/assets/ui/checkboxtrue.png");

    public CheckBoxWidget(GUI gui, ModuleWidget mother, CheckBox value) {
        super(gui, value.getTag());

        this.value = value;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.value.getTag()));
    }

    public void init() {
        this.textureCheckBox.load();
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
            this.getMother().getMother().reloadPositionConfiguration();
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
        int offspace = 2;

        this.textureCheckBox.setX(this.rect.getX() + this.rect.getWidth() - this.textureCheckBox.getWidth() - offspace);
        this.textureCheckBox.setY(this.rect.getY() + this.rect.getHeight() - this.textureCheckBox.getHeight() - offspace);

        this.textureCheckBox.setWidth(this.rect.getWidth() / 6);
        this.textureCheckBox.setHeight(this.rect.getHeight() / 2);

        this.rect.setX(this.getMother().getRect().getX() + this.getOffsetX());
        this.rect.setY(this.getMother().getRect().getY() + this.getOffsetY());

        int diff = 0;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth());

        this.flag.setEnabled(this.value.isShow());
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.textureCheckBox);

        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.value.getValue() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());
        this.textureCheckBox.setColor(255, 255, 255, this.interpolatedSelectedAlpha);

        Texturing.render(this.textureCheckBox);

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
    }

    @Override
    public void onCustomRender() {

    }
}