package me.rina.hyperpop.impl.gui.impl.frame;

import me.rina.hyperpop.api.module.overlay.OverlayElement;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 18/11/2021 at 00:12
 **/
public class ElementFrame extends Frame {
    private final OverlayElement element;

    protected int interpolatedHighlightAlpha;
    protected int interpolatedPressedAlpha;

    protected int registry;

    public ElementFrame(GUI master, OverlayElement element) {
        super(master, element.getTag());

        this.element = element;
    }

    public void init() {}

    public OverlayElement getElement() {
        return element;
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
        this.clear();
        this.flag.setMouseClickedLeft(false);
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onMouseReleased(int button) {
        if (!this.flag.isEnabled()) {
            return;
        }

        if (this.flag.isMouseClickedLeft()) {
            this.flag.setMouseClickedLeft(false);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.flag.isEnabled()) {
            return;
        }

        if (this.flag.isMouseOverDraggable()) {
            if (button == 0) {
                this.setOffsetX(this.master.getMouse().getX() - this.element.getRect().getX());
                this.setOffsetY(this.master.getMouse().getY() - this.element.getRect().getY());
            }

            this.flag.setMouseClickedLeft(button == 0);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        this.master.matrixMoveFocusedFrameToLast();
    }

    @Override
    public void onUpdate() {
        this.flag.setEnabled(this.element.isEnabled() && GUI.HUD_EDITOR);

        if (!this.flag.isEnabled()) {
            return;
        }

        if (this.flag.isMouseClickedLeft()) {
            this.element.getRect().setX(this.master.getMouse().getX() - this.getOffsetX());
            this.element.getRect().setY(this.master.getMouse().getY() - this.getOffsetY());
        }

        this.rect.copy(this.element.getRect());
    }

    @Override
    public void onCustomUpdate() {
        if (!this.flag.isEnabled()) {
            return;
        }

        boolean flag = this.element.getRect().collideWithMouse(this.master.getMouse());

        this.flag.setMouseOver(flag);
        this.flag.setMouseOverDraggable(flag);
    }

    @Override
    public void onRender() {
        if (!this.flag.isEnabled()) {
            return;
        }

        // Apply collision for element.
        this.element.collision(this.master.getDisplay(), 1);

        // Focused...
        Processor.prepare(Theme.INSTANCE.focused);
        Processor.solid(this.element.getRect());

        // Pressed.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.element.getRect());

        // Highlight.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.element.getRect());

        // Draw element.
        this.element.onRender(this.master.getDisplay().getPartialTicks());

        // g.
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        GlStateManager.enableBlend();

        GL11.glPopMatrix();

        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void clear() {
        this.flag.setMouseOver(false);
        this.flag.setMouseOverDraggable(false);
    }
}
