package me.rina.zyra.impl.gui.impl.widget;

import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import me.rina.zyra.api.value.type.ColorPicker;
import me.rina.zyra.impl.gui.GUI;
import me.rina.zyra.impl.gui.api.base.widget.Widget;
import me.rina.zyra.impl.gui.api.engine.Processor;
import me.rina.zyra.impl.gui.api.engine.caller.Statement;
import me.rina.zyra.impl.gui.api.imperador.widget.ImperadorEntryBox;
import me.rina.zyra.impl.gui.api.theme.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 27/11/2021 at 14:57
 *
 * The part of code color picker is from one friend called Doctor-Swag.
 * Credit to him code.
 **/
public class ColorPickerWidget extends Widget {
    private final ModuleWidget mother;
    private final ColorPicker value;

    protected int normalHeight;
    protected int fullHeight;

    private final TurokRect alphaRect = new TurokRect("alpha", 0, 0);
    private final TurokRect hueRect = new TurokRect("hue", 0, 0);
    private final TurokRect SBRect = new TurokRect("saturation&brightness", 0, 0);

    protected boolean isFocusedByCPU;
    protected boolean isStarted;

    protected ImperadorEntryBox imperadorEntryBoxRed;
    protected ImperadorEntryBox imperadorEntryBoxGreen;
    protected ImperadorEntryBox imperadorEntryBoxBlue;
    protected ImperadorEntryBox imperadorEntryBoxAlpha;

    protected Color color;
    protected float colorWidth;
    protected float colorHeight;
    protected boolean selecting;
    protected boolean hueSelecting;
    protected boolean alphaSelecting;
    protected float hue;
    protected float alpha;

    public ColorPickerWidget(GUI master, ModuleWidget mother, ColorPicker value) {
        super(master, value.getTag());

        this.mother = mother;
        this.value = value;

        this.flag.setLocked(false);
        this.flag.setEnabled(true);

        this.normalHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.value.getTag());
        this.rect.setHeight(this.normalHeight);

        this.color = this.value.getColor();
    }

    public void init() {
        this.imperadorEntryBoxRed = new ImperadorEntryBox(this.master, GUI.FONT_NORMAL, "255");
        this.imperadorEntryBoxGreen = new ImperadorEntryBox(this.master, GUI.FONT_NORMAL, "255");
        this.imperadorEntryBoxBlue = new ImperadorEntryBox(this.master, GUI.FONT_NORMAL, "255");
        this.imperadorEntryBoxAlpha = new ImperadorEntryBox(this.master, GUI.FONT_NORMAL, "255");
    }

    public ModuleWidget getMother() {
        return mother;
    }

    public ColorPicker getValue() {
        return value;
    }

    public void updateEntryBox(ImperadorEntryBox imperadorEntryBox) {
        imperadorEntryBox.setFont(GUI.FONT_NORMAL);

        imperadorEntryBox.setPartialTicks(this.master.getDisplay().getPartialTicks());
        imperadorEntryBox.getScissor().set(imperadorEntryBox.getRect().getX(), this.mother.getMother().getProtectedScrollRect().getY(), imperadorEntryBox.getRect().getWidth(), this.mother.getMother().getProtectedScrollRect().getHeight());

        imperadorEntryBox.setOffsetY(2f);
        imperadorEntryBox.doMouseScroll(this.master.getMouse());

        if (imperadorEntryBox.isFocused()) {
            imperadorEntryBox.string = new int[]{0, 0, 0, 255};
            imperadorEntryBox.background = new int[]{255, 255, 255, 255};
            imperadorEntryBox.setIsShadow(false);
        } else {
            imperadorEntryBox.string = new int[]{255, 255, 255, 255};
            imperadorEntryBox.background = new int[]{Theme.INSTANCE.focused.getRed(), Theme.INSTANCE.focused.getGreen(), Theme.INSTANCE.focused.getBlue(), Theme.INSTANCE.focused.getAlpha()};
            imperadorEntryBox.setIsShadow(true);
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
        this.imperadorEntryBoxRed.setFocused(false);
        this.imperadorEntryBoxGreen.setFocused(false);
        this.imperadorEntryBoxBlue.setFocused(false);
        this.imperadorEntryBoxAlpha.setFocused(false);
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        this.imperadorEntryBoxRed.onKeyboard(charCode, keyCode);
        this.imperadorEntryBoxGreen.onKeyboard(charCode, keyCode);
        this.imperadorEntryBoxBlue.onKeyboard(charCode, keyCode);
        this.imperadorEntryBoxAlpha.onKeyboard(charCode, keyCode);
    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onMouseReleased(int button) {
        if (!this.getFlag().isEnabled() || !this.mother.getFlag().isSelected()) {
            return;
        }

        if (this.flag.isMouseClickedLeft()) {
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            if (this.flag.isMouseOver()) {
                this.flag.setLocked(!this.flag.isLocked());
                this.rect.setHeight(this.flag.isLocked() ? this.fullHeight : this.normalHeight);
                this.mother.getMother().reloadPositionConfiguration();
            }

            this.flag.setMouseClickedRight(false);
        }

        this.imperadorEntryBoxRed.onMouseReleased(button);
        this.imperadorEntryBoxGreen.onMouseReleased(button);
        this.imperadorEntryBoxBlue.onMouseReleased(button);
        this.imperadorEntryBoxAlpha.onMouseReleased(button);

        selecting = false;
        alphaSelecting = false;
        hueSelecting = false;
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (!this.getFlag().isEnabled() || !this.mother.getFlag().isSelected()) {
            return;
        }

        if (this.flag.isMouseOver()) {
            this.flag.setMouseClickedLeft(button == 0);
            this.flag.setMouseClickedRight(button == 1 && this.flag.isMouseOverDraggable());

            this.imperadorEntryBoxRed.doSetIndexAB(this.master.getMouse());
            this.imperadorEntryBoxGreen.doSetIndexAB(this.master.getMouse());
            this.imperadorEntryBoxBlue.doSetIndexAB(this.master.getMouse());
            this.imperadorEntryBoxAlpha.doSetIndexAB(this.master.getMouse());

            if (this.SBRect.collideWithMouse(this.master.getMouse())) {
                selecting = true;
            }

            if (this.hueRect.collideWithMouse(this.master.getMouse())) {
                hueSelecting = true;
            }

            if (this.alphaRect.collideWithMouse(this.master.getMouse())) {
                alphaSelecting = true;
            }
        }

        this.imperadorEntryBoxRed.onMouseClicked(button);
        this.imperadorEntryBoxGreen.onMouseClicked(button);
        this.imperadorEntryBoxBlue.onMouseClicked(button);
        this.imperadorEntryBoxAlpha.onMouseClicked(button);
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

        float divisionBy1 = this.rect.getWidth() / 1.9f;
        float divisionBy2 = this.rect.getWidth() / 2f;
        float divisionBy7 = this.rect.getWidth() / 7;

        this.SBRect.set(this.rect.getX(), this.rect.getY() + this.normalHeight + 1, divisionBy1, divisionBy2);
        this.hueRect.set(this.SBRect.getX() + this.SBRect.getWidth() + 1, this.rect.getY() + this.normalHeight + 1, divisionBy7, divisionBy2);
        this.alphaRect.set(this.hueRect.getX() + this.hueRect.getWidth() + 1, this.rect.getY() + this.normalHeight + 1, divisionBy7, divisionBy2);

        this.normalHeight = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.rect.getTag());
        this.fullHeight = this.normalHeight + 1 + (int) divisionBy2 + 1;

        this.flag.setEnabled(this.value.isShow() && this.mother.getFlag().isSelected());

        // Update rect of entry boxes.
        this.imperadorEntryBoxRed.getRect().set(this.alphaRect.getX() + this.alphaRect.getWidth() + 1, this.alphaRect.getY(), divisionBy7, divisionBy1 / 4.5f);
        this.imperadorEntryBoxGreen.getRect().set(this.alphaRect.getX() + this.alphaRect.getWidth() + 1, this.alphaRect.getY() + this.imperadorEntryBoxRed.getRect().getHeight() + 1, divisionBy7, divisionBy1 / 4.5f);
        this.imperadorEntryBoxBlue.getRect().set(this.alphaRect.getX() + this.alphaRect.getWidth() + 1, this.imperadorEntryBoxGreen.getRect().getY() + this.imperadorEntryBoxGreen.getRect().getHeight() + 1, divisionBy7, divisionBy1 / 4.5f);
        this.imperadorEntryBoxAlpha.getRect().set(this.alphaRect.getX() + this.alphaRect.getWidth() + 1, this.imperadorEntryBoxBlue.getRect().getY() + this.imperadorEntryBoxBlue.getRect().getHeight() + 1, divisionBy7, divisionBy1 / 4.5f);

        // Update main setters, scissor & render font of entry boxes.
        this.updateEntryBox(this.imperadorEntryBoxRed);
        this.updateEntryBox(this.imperadorEntryBoxGreen);

        this.updateEntryBox(this.imperadorEntryBoxBlue);
        this.updateEntryBox(this.imperadorEntryBoxAlpha);

        // Update CPU focus.
        if (this.imperadorEntryBoxRed.isFocused() || this.imperadorEntryBoxGreen.isFocused() || this.imperadorEntryBoxBlue.isFocused() || this.imperadorEntryBoxAlpha.isFocused()) {
            this.isFocusedByCPU = true;
            this.master.setUpdate();

            int r = this.color.getRed();
            int g = this.color.getGreen();
            int b = this.color.getBlue();
            int a = this.color.getAlpha();

            try {
                r = Integer.parseInt(this.imperadorEntryBoxRed.getText());
            } catch (NumberFormatException exc) {
            }

            try {
                g = Integer.parseInt(this.imperadorEntryBoxGreen.getText());
            } catch (NumberFormatException exc) {
            }

            try {
                b = Integer.parseInt(this.imperadorEntryBoxBlue.getText());
            } catch (NumberFormatException exc) {
            }

            try {
                a = Integer.parseInt(this.imperadorEntryBoxAlpha.getText());
            } catch (NumberFormatException exc) {
            }

            this.color = new Color(Processor.clamp(r), Processor.clamp(g), Processor.clamp(b), Processor.clamp(a));
            this.setCoordinatesByColor(this.color);
        } else {
            if (this.isFocusedByCPU) {
                this.master.unsetUpdate();
                this.isFocusedByCPU = false;
            }

            this.imperadorEntryBoxRed.setText("" + this.color.getRed());
            this.imperadorEntryBoxGreen.setText("" + this.color.getGreen());
            this.imperadorEntryBoxBlue.setText("" + this.color.getBlue());
            this.imperadorEntryBoxAlpha.setText("" + this.color.getAlpha());
        }

        if (this.isStarted) {
            this.color = new Color(this.value.getRed(), this.value.getGreen(), this.value.getBlue(), this.value.getAlpha());
        } else {
            this.color = new Color(this.value.getRed(), this.value.getGreen(), this.value.getBlue(), this.value.getAlpha());
            this.setCoordinatesByColor(this.color);

            this.isStarted = true;
        }

        if (this.flag.isLocked()) {
            if (selecting) {
                colorWidth = (this.master.getMouse().getX() - (this.SBRect.getX())) / (this.SBRect.getWidth());
                colorHeight = (this.master.getMouse().getY() - (this.SBRect.getY())) / (this.SBRect.getHeight());
            }

            if (hueSelecting) {
                hue = (this.master.getMouse().getY() - (this.hueRect.getY())) / (this.hueRect.getHeight());

                Color theNewestColor = getColorFromCoordinates();

                getValue().setRed(theNewestColor.getRed());
                getValue().setGreen(theNewestColor.getGreen());
                getValue().setBlue(theNewestColor.getBlue());
                getValue().setAlpha(theNewestColor.getAlpha());
            }

            if (alphaSelecting) {
                alpha = (this.master.getMouse().getY() - (this.alphaRect.getY())) / (this.alphaRect.getHeight());

                Color theNewestColor = getColorFromCoordinates();

                getValue().setRed(theNewestColor.getRed());
                getValue().setGreen(theNewestColor.getGreen());
                getValue().setBlue(theNewestColor.getBlue());
                getValue().setAlpha(theNewestColor.getAlpha());
            }

            if (colorWidth > 1.0f) colorWidth = 1.0f;
            if (colorHeight > 1.0f) colorHeight = 1.0f;
            if (colorWidth < 0) colorWidth = 0;
            if (colorHeight < 0) colorHeight = 0;
            if (hue > 1.0f) hue = 1.0f;
            if (hue < 0.0f) hue = 0.0f;
            if (alpha > 1.0f) alpha = 1.0f;
            if (alpha < 0.0f) alpha = 0.0f;

            if (this.isStarted) {
                Color theNewestColor = getColorFromCoordinates();

                getValue().setRed(theNewestColor.getRed());
                getValue().setGreen(theNewestColor.getGreen());
                getValue().setBlue(theNewestColor.getBlue());
                getValue().setAlpha(theNewestColor.getAlpha());
            }
        }
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(this.master.getMouse()) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));
        this.flag.setMouseOverDraggable((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && (this.master.getMouse().getX() > this.rect.getX() && this.master.getMouse().getX() < this.rect.getX() + this.rect.getWidth() && this.master.getMouse().getY() > this.rect.getY() && this.master.getMouse().getY() < this.rect.getY() + this.normalHeight) && this.mother.getMother().getProtectedScrollRect().collideWithMouse(this.master.getMouse()));

        this.imperadorEntryBoxRed.doMouseOver(this.master.getMouse());
        this.imperadorEntryBoxGreen.doMouseOver(this.master.getMouse());
        this.imperadorEntryBoxBlue.doMouseOver(this.master.getMouse());
        this.imperadorEntryBoxAlpha.doMouseOver(this.master.getMouse());
    }

    @Override
    public void onRender() {
        // Preview.
        Processor.prepare(this.value.getColor());
        Processor.solid(this.rect.getX() + this.rect.getWidth() - (this.normalHeight), this.rect.getY() + 2, (this.normalHeight - 2), (this.normalHeight - 2));

        // Tag.
        Statement.set(GL11.GL_SCISSOR_TEST);
        Processor.setScissor(this.rect.getX() + 2f, this.rect.getY(), TurokFontManager.getStringWidth(GUI.FONT_NORMAL, this.value.getTag()) + 2f, this.rect.getHeight(), this.master.getDisplay());
        Processor.string(GUI.FONT_NORMAL, this.value.getTag(), this.rect.getX() + 2, this.rect.getY() + 3f, 255);
        Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());

        if (this.flag.isLocked()) {
            // Render entry boxes.
            this.imperadorEntryBoxRed.onRender();

            Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());
            Processor.unsetScissor();

            this.imperadorEntryBoxGreen.onRender();

            Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());
            Processor.unsetScissor();

            this.imperadorEntryBoxBlue.onRender();

            Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());
            Processor.unsetScissor();

            this.imperadorEntryBoxAlpha.onRender();

            Processor.setScissor(this.mother.getMother().getProtectedScrollRect(), this.master.getDisplay());
            Processor.unsetScissor();

            // Draw stuff.
            this.drawHueSliderBetter(this.hueRect.getX(), this.hueRect.getY(), this.hueRect.getWidth(), this.hueRect.getHeight(), this.hue);
            this.drawAlphaSlider(this.alphaRect.getX(), this.alphaRect.getY(), this.alphaRect.getWidth(), this.alphaRect.getHeight(), this.alpha);
            this.drawSquareColorPicker(this.SBRect.getX(), this.SBRect.getY(), this.SBRect.getWidth(), this.SBRect.getHeight(), this.SBRect.getX() + this.colorWidth * (this.SBRect.getWidth()), this.SBRect.getY() + (this.colorHeight * this.SBRect.getHeight()), 1.0f, Color.getHSBColor(hue, 1.0f, 1.0f));
        }
    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void clear() {
        this.imperadorEntryBoxRed.setMouseOver(false);
        this.imperadorEntryBoxGreen.setMouseOver(false);
        this.imperadorEntryBoxBlue.setMouseOver(false);
        this.imperadorEntryBoxAlpha.setMouseOver(false);
    }

    public void setCoordinatesByColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        this.alpha = color.getAlpha() / 255f;
        this.hue = hsb[0];
        this.colorWidth = hsb[1];
        this.colorHeight = 1f - hsb[2];
    }


    public Color getColorFromCoordinates() {
        float saturation = colorWidth;
        float brightness = 1.0f - colorHeight;

        this.value.setSaturation(saturation);
        this.value.setBrightness(brightness);

        Color color1 = Color.getHSBColor(hue, saturation, brightness);
        return new Color(MathHelper.clamp(color1.getRed(), 0, 255), MathHelper.clamp(color1.getGreen(), 0, 255), MathHelper.clamp(color1.getBlue(), 0, 255), (int) MathHelper.clamp(alpha * 255, 0, 255));
    }

    public void drawSquareColorPicker(float x, float y, float width, float height, float circleX, float circleY, float radius, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0.0d).color(255, 255, 255, 255).endVertex();
        builder.pos(x, y + height, 0.0d).color(255, 255, 255, 255).endVertex();
        builder.pos(x + width, y + height, 0.0d).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
        builder.pos(x + width, y, 0.0d).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
        tessellator.draw();

        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0.0d).color(0, 0, 0, 0).endVertex();
        builder.pos(x, y + height, 0.0d).color(0, 0, 0, 255).endVertex();
        builder.pos(x + width, y + height, 0.0d).color(0, 0, 0, 255).endVertex();
        builder.pos(x + width, y, 0.0d).color(0, 0, 0, 0).endVertex();
        tessellator.draw();

        drawCircle(circleX, circleY, radius, Color.BLACK);

        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public void drawCompleteImage(float posX, float posY, float width, float height, ResourceLocation location) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(width, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void drawAlphaSlider(float x, float y, float width, float height, float progress) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        // GlStateManager.enableTexture2D();
        // GlStateManager.disableBlend();
        // ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("onepop/", ALPHA_SLIDER);
        // Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        // GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        // this.drawCompleteImage(x, y, width, height, location);
        // GlStateManager.enableBlend();
        // GlStateManager.disableTexture2D();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0.0d).color(255, 255, 255, 0).endVertex();
        builder.pos(x, y + height, 0.0d).color(0, 0, 0, 255).endVertex();
        builder.pos(x + width, y + height, 0.0d).color(0, 0, 0, 255).endVertex();
        builder.pos(x + width, y, 0.0d).color(255, 255, 255, 0).endVertex();
        tessellator.draw();

        this.drawRectangle(x, y + (height * progress), width, 1.0f, 1.0f, Color.GRAY);

        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public void drawHueSliderBetter(float x, float y, float width, float height, float progress) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        Color color1 = new Color(255, 0, 0, 255); // 0.0
        Color color2 = new Color(255, 255, 0, 255); // 0.1666
        Color color3 = new Color(0, 255, 0, 255); // 0.3333
        Color color4 = new Color(0, 255, 255, 255); // 0.5
        Color color5 = new Color(0, 0, 255, 255); // 0.6666
        Color color6 = new Color(255, 0, 255, 255); // 0.8333
        float offset = height / 6;

        GL11.glColor4f(color1.getRed() / 255f, color1.getGreen() / 255f, color1.getBlue() / 255f, color1.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y);
        GL11.glColor4f(color2.getRed() / 255f, color2.getGreen() / 255f, color2.getBlue() / 255f, color2.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + offset);
        GL11.glColor4f(color3.getRed() / 255f, color3.getGreen() / 255f, color3.getBlue() / 255f, color3.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + offset * 2);
        GL11.glColor4f(color4.getRed() / 255f, color4.getGreen() / 255f, color4.getBlue() / 255f, color4.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + offset * 3);
        GL11.glColor4f(color5.getRed() / 255f, color5.getGreen() / 255f, color5.getBlue() / 255f, color5.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + offset * 4);
        GL11.glColor4f(color6.getRed() / 255f, color6.getGreen() / 255f, color6.getBlue() / 255f, color6.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + offset * 5);
        GL11.glColor4f(color1.getRed() / 255f, color1.getGreen() / 255f, color1.getBlue() / 255f, color1.getAlpha() / 255f);
        GL11.glVertex2f(x + width / 2, y + height);
        GL11.glEnd();

        drawRectangle(x, y + (height * progress), width, 2, 1.0f, Color.BLACK);

        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void drawRectangle(float x, float y, float width, float height, float lineWidth, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GlStateManager.glLineWidth(lineWidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos(x, y, 0.0d).endVertex();
        builder.pos(x, y + height, 0.0d).endVertex();
        builder.pos(x + width, y + height, 0.0d).endVertex();
        builder.pos(x + width, y, 0.0d).endVertex();
        tessellator.draw();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public void drawCircle(float x, float y, float radius, Color color) {
        double ps;
        double cs;
        double i;
        double[] outer;
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
        GL11.glBegin(9);
        for (i = 0.0; i < 36.0; i += 1.0) {
            cs = i * 10.0 * 3.141592653589793 / 180.0;
            ps = (i * 10.0 - 1.0) * 3.141592653589793 / 180.0;
            outer = new double[]{Math.cos(cs) * (double) radius, (-Math.sin(cs)) * (double) radius, Math.cos(ps) * (double) radius, (-Math.sin(ps)) * (double) radius};
            GL11.glVertex2d((double) x + outer[0], (double) y + outer[1]);
        }
        GL11.glEnd();
        GL11.glEnable(2848);
        GL11.glBegin(3);
        for (i = 0.0; i < 37.0; i += 1.0) {
            cs = i * 10.0 * 3.141592653589793 / 180.0;
            ps = (i * 10.0 - 1.0) * 3.141592653589793 / 180.0;
            outer = new double[]{Math.cos(cs) * (double) radius, (-Math.sin(cs)) * (double) radius, Math.cos(ps) * (double) radius, (-Math.sin(ps)) * (double) radius};
            GL11.glVertex2d((double) x + outer[0], (double) y + outer[1]);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.popMatrix();
    }
}
