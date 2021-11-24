package me.rina.hyperpop.impl.gui.impl.widget;

import me.rina.hyperpop.api.value.type.BindBox;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.api.engine.texture.Texturing;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.backend.Textures;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

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
        this.flag.setMouseClickedLeft(false);
        this.flag.setMouseClickedRight(false);
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

                    break;
                }

                case Keyboard.KEY_DELETE: {
                    this.value.setKey(-1);
                    this.flag.setLocked(false);

                    break;
                }

                default: {
                    this.value.setKey(keyCode);
                    this.flag.setLocked(false);

                    break;
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flag.isLocked()) {
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
        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag()));

        float off_space = 2;

        this.rect.setX(this.mother.getMother().getRect().getX() + this.master.getDistance() * 2);
        this.rect.setY(this.getMother().getMother().getRect().getY() + this.getMother().getMother().getOffsetY() + this.getMother().getOffsetY() + this.getOffsetY());

        this.rectKey.setX(this.rect.getX());
        this.rectKey.setY(this.rect.getY() + 1);

        this.rectKey.setWidth(this.rect.getWidth());
        this.rectKey.setHeight(this.rect.getHeight() - (2));

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - this.getOffsetX() * 2);

        this.flag.setEnabled(this.value.isShow() && this.mother.getFlag().isSelected());

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
        // Scissor.
        Statement.set(GL11.GL_SCISSOR_TEST);
        Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());

        // Post focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.rectKey);

        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.value.getValue() ? (Theme.INSTANCE.selected.getAlpha()) : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getSelected(this.interpolatedSelectedAlpha));
        Processor.solid(this.rectKey);

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rectKey);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rectKey);

        // The tag.
        Processor.setScissor((int) this.mother.getMother().getRect().getX() + this.master.getDistance() * 2, (int) this.mother.getMother().getProtectedScrollRect().getY(), this.rect.width - this.keySizeWidth - 1f, this.mother.getMother().getProtectedScrollRect().getHeight(), this.master.getDisplay());
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);
        Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());

        // Key.
        this.interpolatedStringAlpha = 255;

        String animation = "";

        if (this.flag.isLocked()) {
            if (this.master.getSlowerCooldownUsingAnWidgetTimer().isPassedMS(250)) {
                animation = ".";
            }

            if (this.master.getSlowerCooldownUsingAnWidgetTimer().isPassedMS(500)) {
                animation = "..";
            }

            if (this.master.getSlowerCooldownUsingAnWidgetTimer().isPassedMS(750)) {
                animation = "...";
            }

            this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL, animation);
        }

        final String tag = this.flag.isLocked() ? animation : (this.value.getKey() == -1 ? "NONE" : (Keyboard.getKeyName(this.value.getKey()).toUpperCase()));

        this.keySizeWidth = TurokFontManager.getStringWidth(GUI.FONT_NORMAL, tag);

        Processor.string(GUI.FONT_NORMAL, tag, this.rectKey.getX() + this.rectKey.getWidth() - (this.keySizeWidth) - 3f, this.rect.getY() + 3f, this.interpolatedStringAlpha);
        Processor.unsetScissor();
    }

    @Override
    public void onCustomRender() {

    }
}
