package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Combobox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texture;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.backend.Textures;
import me.rina.turok.render.font.management.TurokFontManager;

/**
 * @author SrRina
 * @since 16/11/2021 at 14:37
 **/
public class ComboboxWidget extends Widget {
    private final ModuleWidget mother;
    private final Combobox value;

    protected int interpolatedSelectedAlpha;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;

    public ComboboxWidget(GUI gui, ModuleWidget mother, Combobox value) {
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

    public Combobox getValue() {
        return value;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {
        this.clear();
        this.flag.setMouseClickedLeft(false);
        this.flag.setMouseClickedRight(false);
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
            if (this.flag.isMouseOver()) {
                this.value.gonext();
                this.getMother().getMother().reloadPositionConfiguration();
            }

            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            this.master.getPopupMenuFrame().callPopup(this.rect.getTag(), this.master.getMouse().getX(), this.master.getMouse().getY(), 75, this.value.getValue(), this.value.getList());
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
        if (this.flag.isMouseOver()) {
            this.flag.setMouseClickedLeft(button == 0);
            this.flag.setMouseClickedRight(button == 1);
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
        float off_space = 2;
        float size = (this.rect.getHeight() - (off_space * 2));

        this.rect.setX(this.getMother().getRect().getX() + this.getOffsetX());
        this.rect.setY(this.getMother().getRect().getY() + this.getOffsetY());

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - this.getOffsetX() * 2);

        this.flag.setEnabled(this.value.isShow());

        if (this.master.getPopupMenuFrame().getFlag().isEnabled() && this.master.getPopupMenuFrame().isReleasedCallback() && this.master.getPopupMenuFrame().getRect().getTag().equalsIgnoreCase(this.rect.getTag())) {
            this.value.setValue(this.master.getPopupMenuFrame().getCallback());
            this.master.getPopupMenuFrame().onClose();
        }
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.rect.x, this.rect.y + (this.master.getDistance()), this.rect.getWidth(), this.rect.height - (this.master.getDistance() * 2));

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect.x, this.rect.y + (this.master.getDistance()), this.rect.getWidth(), this.rect.height - (this.master.getDistance() * 2));

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay().getPartialTicks() * 0.1f);

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect.x - 1, this.rect.y - (this.master.getDistance()), this.rect.getWidth() + 2, this.rect.height + (this.master.getDistance() * 2));

        // The tag.
        Processor.setScissor((int) this.rect.getX(), (int) this.mother.getMother().getProtectedScrollRect().getY(), this.rect.width, this.mother.getMother().getProtectedScrollRect().getHeight(), this.master.getDisplay());
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag() + ": " + this.value.getValue(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);
    }

    @Override
    public void onCustomRender() {

    }
}
