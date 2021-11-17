package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.value.type.BindBox;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.backend.Textures;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 15/11/2021 at 12:02
 **/
public class BindBoxWidget extends Widget {
    private final ModuleWidget mother;
    private final BindBox value;

    protected int interpolatedSelectedAlpha;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;
    protected int interpolatedStringAlpha;

    protected int keySizeWidth = -1;

    private final TurokRect rectKey = new TurokRect("zz", 0, 0);
    private boolean isFocusedByCPU;

    public BindBoxWidget(GUI gui, ModuleWidget mother, BindBox value) {
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

    public BindBox getValue() {
        return value;
    }

    public void setFocusedByCPU(boolean focusedByCPU) {
        isFocusedByCPU = focusedByCPU;
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
        if (this.flag.isLocked()) {
            switch (keyCode) {
                case Keyboard.KEY_ESCAPE: {
                    this.flag.setLocked(false);

                    this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL,  this.value.getKey() == -1 ? "NONE" : Keyboard.getKeyName(this.value.getKey()).toUpperCase());

                    break;
                }

                case Keyboard.KEY_DELETE: {
                    this.value.setKey(-1);
                    this.flag.setLocked(false);

                    this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL, "NONE");

                    break;
                }

                default: {
                    this.value.setKey(keyCode);
                    this.flag.setLocked(false);

                    this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL, Keyboard.getKeyName(keyCode).toUpperCase());

                    break;
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flag.isLocked()) {
            this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL,  this.value.getKey() == -1 ? "NONE" : Keyboard.getKeyName(this.value.getKey()).toUpperCase());
            this.flag.setLocked(false);
        }

        if (this.flag.isResizing()) {
            this.flag.setResizing(false);
        }

        if (this.flag.isDragging()) {
            this.flag.setDragging(false);
        }

        if (this.flag.isMouseClickedLeft()) {
            this.flag.setLocked(this.flag.isMouseOver());

            if (this.flag.isLocked()) {
                this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL, "<key>");
            }

            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            if (this.flag.isMouseOver()) {
                this.value.setValue(!this.value.getValue());
                this.getMother().getMother().reloadPositionConfiguration();
            }

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
        if (this.keySizeWidth == -1) {
            this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL,  this.value.getKey() == -1 ? "NONE" : Keyboard.getKeyName(this.value.getKey()).toUpperCase());
        }

        float off_space = 2;

        this.rectKey.setX(this.rect.getX() + this.rect.getWidth() - this.rectKey.getWidth() - off_space);
        this.rectKey.setY(this.rect.getY() + off_space);

        this.rectKey.setWidth(this.keySizeWidth + 2f);
        this.rectKey.setHeight(this.rect.getHeight() - 3);

        this.rect.setX(this.getMother().getRect().getX() + this.getOffsetX());
        this.rect.setY(this.getMother().getRect().getY() + this.getOffsetY());

        int diff = 0;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth());

        this.flag.setEnabled(this.value.isShow());

        if (this.flag.isLocked()) {
            this.setFocusedByCPU(true);
            this.master.setUpdate();
        } else {
            if (this.isFocusedByCPU()) {
                this.master.unsetUpdate();
                this.setFocusedByCPU(false);
            }
        }
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.value.getValue() ? (Theme.INSTANCE.selected.getAlpha()) : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getSelected(this.interpolatedSelectedAlpha));
        Processor.solid(this.rect);

        // Post focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.rectKey);

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

        // Key.
        this.interpolatedStringAlpha = Processor.interpolation(this.interpolatedStringAlpha, this.flag.isLocked() ? (this.master.getSlowerCooldownUsingAnWidgetTimer().isPassedMS(500) ? 255 : 0) : Theme.INSTANCE.string.getAlpha(), this.master.getDisplay());

        if (this.interpolatedStringAlpha >= 20) {
            Processor.string(GUI.FONT_NORMAL, this.flag.isLocked() ? "<key>" : (this.value.getKey() == -1 ? "NONE" : Keyboard.getKeyName(this.value.getKey()).toUpperCase()), this.rectKey.getX() + 1f, this.rectKey.getY() + 1, this.interpolatedStringAlpha);
        }
    }

    @Override
    public void onCustomRender() {

    }
}
