package me.rina.winterescape.impl.gui.impl.widget;

import me.rina.winterescape.api.value.type.CheckBox;
import me.rina.winterescape.impl.gui.GUI;
import me.rina.winterescape.impl.gui.api.base.widget.Widget;
import me.rina.winterescape.impl.gui.api.engine.Processor;
import me.rina.winterescape.impl.gui.api.engine.caller.Statement;
import me.rina.winterescape.impl.gui.api.engine.texture.Texture;
import me.rina.winterescape.impl.gui.api.engine.texture.Texturing;
import me.rina.winterescape.impl.gui.api.theme.Theme;
import me.rina.winterescape.impl.gui.impl.backend.Textures;
import me.rina.turok.render.font.management.TurokFontManager;
import org.lwjgl.opengl.GL11;

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

    private final Texture textureCheckBox = new Texture(null, 0, 0);

    public CheckBoxWidget(GUI gui, ModuleWidget mother, CheckBox value) {
        super(gui, value.getTag());

        this.value = value;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.value.getTag()));

        Textures.set(textureCheckBox, Texturing.get(Textures.UI_CHECKBOX));
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

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag()));

        this.rect.setX(this.mother.getMother().getRect().getX() + this.master.getDistance() * 2);
        this.rect.setY(this.getMother().getMother().getRect().getY() + this.getMother().getMother().getOffsetY() + this.getMother().getOffsetY() + this.getOffsetY());

        this.textureCheckBox.setX(this.rect.getX() + this.rect.getWidth() - this.textureCheckBox.getWidth() - 1f);
        this.textureCheckBox.setY(this.rect.getY() + off_space);

        this.textureCheckBox.setWidth(size);
        this.textureCheckBox.setHeight(size);

        this.textureCheckBox.setTextureWidth((int) size);
        this.textureCheckBox.setTextureHeight((int) size);

        int diff = 1;

        this.setOffsetX(diff);
        this.rect.setWidth(this.getMother().getRect().getWidth() - this.getOffsetX() * 2);

        this.flag.setEnabled(this.value.isShow() && this.mother.getFlag().isSelected());
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

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect);

        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.value.getValue() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());
        this.textureCheckBox.setColor(255, 255, 255, this.interpolatedSelectedAlpha);

        // Focused background.
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.textureCheckBox);

        Texturing.render(this.textureCheckBox);

        // The tag.
        Processor.setScissor((int) this.mother.getMother().getRect().getX() + this.master.getDistance() * 2, (int) this.mother.getMother().getProtectedScrollRect().getY(), this.rect.width - this.textureCheckBox.getWidth() - 1f, this.mother.getMother().getProtectedScrollRect().getHeight(), this.master.getDisplay());
        Processor.string(GUI.FONT_NORMAL, this.rect.getTag(), this.rect.getX() + 2, this.rect.getY() + 3, Theme.INSTANCE.background);
        Processor.unsetScissor();
    }

    @Override
    public void onCustomRender() {

    }
}