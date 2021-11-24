package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.api.imperador.widget.ImperadorEntryBox;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.turok.render.font.management.TurokFontManager;
import org.lwjgl.opengl.GL11;

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
        this.imperadorEntryBox = new ImperadorEntryBox(this.master, GUI.FONT_NORMAL, this.value.getValue());
        this.imperadorEntryBox.setRendering(true);
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
        this.flag.setMouseClickedLeft(false);
        this.flag.setMouseClickedRight(false);
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        this.imperadorEntryBox.onKeyboard(charCode, keyCode);
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
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }

        this.imperadorEntryBox.onMouseReleased(button);
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flag.isMouseOver() && (button == 0 || button == 2 || button == 3)) {
            this.imperadorEntryBox.doSetIndexAB(this.master.getMouse());
            this.imperadorEntryBox.onMouseClicked(button);

            this.flag.setMouseClickedLeft(button == 0);
        }

        if (!this.flag.isMouseOver()) {
            this.imperadorEntryBox.setFocused(false);
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
        this.rect.setX(this.mother.getMother().getRect().getX() + this.master.getDistance() * 2);
        this.rect.setY(this.getMother().getMother().getRect().getY() + this.getMother().getMother().getOffsetY() + this.getMother().getOffsetY() + this.getOffsetY());

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - this.getOffsetX() * 2);

        if (this.imperadorEntryBox.isFocused()) {
        	this.master.setUpdate();
        	this.setFocusedByCPU(true);

        	this.imperadorEntryBox.string = new int[] {0, 0, 0, 255};
            this.imperadorEntryBox.background = new int[] {255, 255, 255, 255};
            this.imperadorEntryBox.setIsShadow(false);
        } else {
        	if (this.isFocusedByCPU()) {
        		this.master.unsetUpdate();
        		this.setFocusedByCPU(false);
        	}

        	this.imperadorEntryBox.string = new int[] {255, 255, 255, 255};
            this.imperadorEntryBox.background = new int[] {Theme.INSTANCE.focused.getRed(), Theme.INSTANCE.focused.getGreen(), Theme.INSTANCE.focused.getBlue(), Theme.INSTANCE.focused.getAlpha()};
            this.imperadorEntryBox.setIsShadow(true);
        }

        if (this.value.lastSet() != null) {
        	this.imperadorEntryBox.setText(this.value.lastSet());
        	this.value.unset();
        } else {
        	this.value.motherfuck(this.imperadorEntryBox.getText());
        }

        this.value.setFocused(this.imperadorEntryBox.isFocused());
        this.imperadorEntryBox.getRect().set(this.rect.getX(), this.rect.getY() + (this.master.getDistance()), this.rect.getWidth(), this.rect.getHeight() - (this.master.getDistance() * 2));
        this.flag.setEnabled(this.value.isShow() && this.mother.getFlag().isSelected());
        this.imperadorEntryBox.setFont(GUI.FONT_NORMAL);
        this.imperadorEntryBox.setPartialTicks(this.master.getDisplay().getPartialTicks());
        this.imperadorEntryBox.getScissor().set(this.mother.getMother().getRect().getX() + this.master.getDistance() * 2, this.mother.getMother().getProtectedScrollRect().getY(), this.rect.getWidth(), this.mother.getMother().getProtectedScrollRect().getHeight());
        this.imperadorEntryBox.setOffsetY(2f);
    }

    @Override
    public void onCustomUpdate() {
    	final boolean mouseIsOver = (!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse());

        this.flag.setMouseOver(mouseIsOver);
    	this.imperadorEntryBox.setMouseOver(mouseIsOver);
    }

    @Override
    public void onRender() {
        // Scissor.
        Statement.set(GL11.GL_SCISSOR_TEST);
        Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());

    	// Smooth ticks.
    	this.imperadorEntryBox.doMouseScroll(this.master.getMouse());

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect.x, this.rect.y + (this.master.getDistance()), this.rect.getWidth(), this.rect.height - (this.master.getDistance() * 2));

        // Entry box post render.
        this.imperadorEntryBox.onRender();

        // Fix scissor after.
        Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() && !this.imperadorEntryBox.isFocused() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.imperadorEntryBox.getRect());

        Processor.unsetScissor();
    }

    @Override
    public void onCustomRender() {

    }
}