package me.rina.zyra.impl.gui.impl.widget;

import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import me.rina.zyra.api.value.type.ColorPicker;
import me.rina.zyra.impl.gui.GUI;
import me.rina.zyra.impl.gui.api.base.widget.Widget;
import me.rina.zyra.impl.gui.api.engine.Processor;

import java.awt.*;

/**
 * @author SrRina
 * @since 27/11/2021 at 14:57
 **/
public class ColorPickerWidget extends Widget {
    private final ModuleWidget mother;
    private final ColorPicker value;

    protected int normalHeight;
    protected int fullHeight;

    private final TurokRect alphaRect = new TurokRect("alpha", 0, 0);
    private final TurokRect hueRect = new TurokRect("hue", 0, 0);
    private final TurokRect SBRect = new TurokRect("saturation&brightness", 0, 0);

    public ColorPickerWidget(GUI master, ModuleWidget mother, ColorPicker value) {
        super(master, value.getTag());

        this.mother = mother;
        this.value = value;

        this.flag.setLocked(false);

        this.normalHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.value.getTag());
        this.rect.setHeight(this.normalHeight);
    }

    public void init() {

    }

    public ModuleWidget getMother() {
        return mother;
    }

    public ColorPicker getValue() {
        return value;
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
        if (!this.getFlag().isEnabled() || !this.flag.isSelected()) {
            return;
        }

        if (this.flag.isMouseClickedLeft()) {
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            if (this.flag.isMouseOver()) {
                this.flag.setLocked(!this.flag.isLocked());
            }

            this.flag.setMouseClickedRight(false);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.getFlag().isEnabled() || !this.flag.isSelected()) {
            return;
        }

        if (this.flag.isMouseOver()) {
            this.flag.setMouseClickedLeft(button == 0);
            this.flag.setMouseClickedRight(button == 1);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onUpdate() {
        this.rect.setX(this.mother.getMother().getRect().getX() + this.master.getDistance() * 2);
        this.rect.setY(this.getMother().getMother().getRect().getY() + this.getMother().getMother().getOffsetY() + this.getMother().getOffsetY() + this.getOffsetY());

        this.rect.setHeight(this.flag.isLocked() ? this.fullHeight : this.normalHeight);
        this.rect.setWidth(this.getMother().getRect().getWidth() - this.getOffsetX() * 2);

        float divisionBy3 = this.rect.getWidth() / 3;
        float divisionBy6 = this.rect.getWidth() / 6;

        this.SBRect.set(this.rect.getX(), this.rect.getY() + this.normalHeight + 1, divisionBy3, divisionBy3);
        this.hueRect.set(this.rect.getX() + this.SBRect.getWidth() + 1, this.rect.getY() + this.normalHeight + 1, divisionBy6, divisionBy3);
        this.alphaRect.set(this.rect.getX() + 1 + this.SBRect.getWidth() + 1 + hueRect.getWidth() + 1, this.rect.getY() + this.normalHeight + 1, divisionBy6, divisionBy3);

        this.normalHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());
        this.fullHeight = this.normalHeight + 1;

        this.flag.setEnabled(this.value.isShow() && this.mother.getFlag().isSelected());
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        if (this.flag.isLocked()) {
            Processor.prepare(255, 0, 0, 100);
            Processor.solid(this.SBRect);

            Processor.prepare(0, 255, 0, 100);
            Processor.solid(this.hueRect);

            Processor.prepare(0, 0, 255, 100);
            Processor.solid(this.alphaRect);
        }
    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void clear() {

    }
}
