package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.value.type.Slider;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;

/**
 * @author SrRina
 * @since 04/11/2021 at 23:24
 **/
public class SliderWidget extends Widget {
    private final ModuleWidget mother;
    private final Slider value;

    protected int interpolatedWidth;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;

    public SliderWidget(GUI gui, ModuleWidget mother, Slider value) {
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

    public Slider getValue() {
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
    	// Selected draw.
    	float current = this.value.getValue().floatValue();

    	float minimum = this.value.getMinimum().floatValue();
    	float maximum = this.value.getMaximum().floatValue();

    	float mouse = Math.min(this.rect.getWidth(), Math.max(0, this.master.getMouse().getX() - this.rect.x));

        if (this.flag.isMouseClickedLeft()) {
        	if (mouse == 0) {
        		this.value.setValue(this.value.getMinimum());
        	} else {
          		if (this.value.getValue() instanceof Integer) {
                    int roundedValue = (int) TurokMath.round(((mouse / this.rect.getWidth()) * (maximum - minimum) + minimum));

                    this.value.setValue(roundedValue);
                } else if (this.value.getValue() instanceof Double) {
                    double roundedValue = TurokMath.round(((mouse / this.rect.getWidth()) * (maximum - minimum) + minimum));

                    this.value.setValue(roundedValue);
                } else if (this.value.getValue() instanceof Float) {
                    float roundedValue = (float) TurokMath.round(((mouse / this.rect.getWidth()) * (maximum - minimum) + minimum));

                    this.value.setValue(roundedValue);
                }
        	}
        }

        // Focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.rect);

        // Width;
    	this.interpolatedWidth = (int) Processor.interpolation(this.interpolatedWidth, (this.rect.getWidth()) * (current - minimum) / (maximum - minimum), this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.selected);
        Processor.solid(this.rect.x, this.rect.y, this.interpolatedWidth, this.rect.height);

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect.x, this.rect.y, this.interpolatedWidth, this.rect.height);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect.x, this.rect.y, this.interpolatedWidth, this.rect.height);

        // The tag.
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag() + ": " + this.value.getValue().toString(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);
    }

    @Override
    public void onCustomRender() {

    }
}