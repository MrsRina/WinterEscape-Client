package me.rina.hyperpop.impl.gui.impl.module.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;

/**
 * @author SrRina
 * @since 19/10/2021 at 10:18am
 **/
public class EntryWidget extends Widget {
    private final ModuleWidget mother;
    private final Entry value;

    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;

    private ImperadorEntryBox imperadorEntryBox;
    private boolean isFocusedByCPU;

    public EntryWidget(GUI gui, ModuleWidget mother, Entry value) {
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

    public Entry getValue() {
        return value;
    }

    public ImperadorEntryBox getImperadorEntryBox() {
    	return imperadorEntryBox;
    }

    public void setFocusedByCPU(boolean state) {
    	this.isFocusedByCPU = state;
    }

    public boolean isFocusedByCPU() {
    	return isFocusedByCPU;
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
    	this.imperadorEntryBox.onKeyboard(charCode, keyCode);
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
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }

        this.imperadorEntryBox.onMouseReleased();
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flag.isMouseOver() && (button == 0 || button == 2 || button == 3)) {
        	this.imperadorEntryBox.onMouseClicked(button);
        	this.imperadorEntryBox.doSetIndexAB(this.master.getMouse());

            this.flag.setMouseClickedLeft(button == 0);
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

        this.imperadorEntryBox.setMouseOver(false);
    }

    @Override
    public void onUpdate() {
        int diff = 1;

        if (this.imperadorEntryBox.isFocused()) {
        	this.master.setUpdate();
        	this.setFocusedByCPU(true);

        	this.imperadorEntryBox.string = {0, 0, 0, 255};
        } else {
        	if (this.isFocusedByCPU()) {
        		this.master.unsetUpdate();
        		this.isFocusedByCPU(false);
        	}

        	this.imperadorEntryBox.string = {255, 255, 255, 255};
        }

        if (this.value.lastSet() != null) {
        	this.imperadorEntryBox.setText(this.value.lastSet());
        	this.value.unset();
        } else {
        	this.value.motherfuck(this.imperadorEntryBox.getText());
        }

        this.rect.setWidth(this.getMother().getRect().getWidth() - (diff * 2));
        this.imperadorEntryBox.getRect().set(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.rect.getHeight());
        this.flag.setEnabled(this.value.isShow());
    }

    @Override
    public void onCustomUpdate() {
    	final boolean mouseIsOver = this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getFlag().getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse());

        this.flag.setMouseOver(mouseIsOver);
    	this.imperadorEntryBox.setMouseOver(mouseIsOver);
    }

    @Override
    public void onRender() {
    	// Smooth ticks.
    	this.imperadorEntryBox.doMouseScroll(this.master.getMouse());

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect);

        // Entry box post render.
        this.imperadorEntryBox.onRender();
    }

    @Override
    public void onCustomRender() {

    }
}
