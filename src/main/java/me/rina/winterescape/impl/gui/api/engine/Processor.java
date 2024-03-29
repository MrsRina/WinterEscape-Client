package me.rina.winterescape.impl.gui.api.engine;

import me.rina.winterescape.impl.gui.api.engine.caller.Statement;
import me.rina.winterescape.impl.gui.api.theme.Theme;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 09/09/2021 at 17:40
 **/
public class Processor {
    public static Minecraft theMinecraft = Minecraft.getMinecraft();
    public static int TEXTURE_COLOR = new Color(255, 255, 255).getRGB();

    /* Start of post fx render. */
    public static void prepare(Color color) {
        prepare(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void prepare(int red, int green, int blue, int alpha) {
        Statement.matrix();
        Statement.blend();

        Statement.color(red, green, blue, alpha);
    }

    public static void solid(TurokRect rect) {
        solid(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static void solid(float x, float y, float w, float h) {
        Statement.unset(GL11.GL_TEXTURE_2D);
        Statement.prepare(GL11.GL_QUADS);

        Statement.vertex2d(x, y);
        Statement.vertex2d(x, y + h);

        Statement.vertex2d(x + w, y + h);
        Statement.vertex2d(x + w, y);

        Statement.draw();

        Statement.set(GL11.GL_TEXTURE_2D);
        Statement.refresh();
    }

    public static void outline(TurokRect rect) {
        outline(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static void outline(float x, float y, float w, float h) {
        Statement.unset(GL11.GL_TEXTURE_2D);

        Statement.set(GL11.GL_LINE_SMOOTH);
        Statement.line(1f);

        Statement.prepare(GL11.GL_LINES);

        Statement.vertex2d(x, y);
        Statement.vertex2d(x, y + h);

        Statement.vertex2d(x, y + h);
        Statement.vertex2d(x + w, y + h);

        Statement.vertex2d(x + w, y + h);
        Statement.vertex2d(x + w, y);

        Statement.vertex2d(x + w, y);
        Statement.vertex2d(x, y);

        Statement.draw();
        Statement.refresh();
    }

    public static void string(TurokFont font, String string, float x, float y, Color background) {
        string(font, string, x, y, 255);
    }

    public static void string(TurokFont font, String string, float x, float y, int alpha) {
        boolean shadow = true; // Theme.INSTANCE.shadow$True$False(background);
        Color color = Theme.INSTANCE.getString(alpha);

        Statement.matrix();

        Statement.set(GL11.GL_TEXTURE_2D);
        Statement.blend();

        GlStateManager.enableAlpha();
        Statement.color(color);

        if (shadow) {
            if (font.isRenderingCustomFont()) {
                font.drawStringWithShadow(string, x, y, color.getRGB());
            } else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, (int) x, (int) y, color.getRGB());
            }
        } else {
            if (font.isRenderingCustomFont()) {
                font.drawString(string, x, y, color.getRGB());
            } else {
                Minecraft.getMinecraft().fontRenderer.drawString(string, (int) x, (int) y, color.getRGB());
            }
        }

        Statement.refresh();
    }
    /* End of post fx render functions. */

    /* Start of process functions. */
    public static void setScissor(TurokRect rect, TurokDisplay display) {
        setScissor(rect.x, rect.y, rect.width, rect.height, display);
    }

    public static void setScissor(float x, float y, float w, float h, TurokDisplay display) {
        int calculatedW = (int) (x + w);
        int calculatedH = (int) (y + h);

        Statement.scissor((int) (x * display.getScaleFactor()), (int) (display.getHeight() - (calculatedH * display.getScaleFactor())), (int) ((calculatedW - x) * display.getScaleFactor()), (int) ((calculatedH - y) * display.getScaleFactor()));
    }

    public static void unsetScissor() {
        Statement.unset(GL11.GL_SCISSOR_TEST);
    }
    /* End of process functions. */

    /* Start of interpolation functions. */
    public static float interpolation(float a, float b, TurokDisplay display) {
        return interpolation(a, b, display.getPartialTicks());
    }

    public static double interpolation(double a, double b, TurokDisplay display) {
        return a + (b - a) * display.getPartialTicks();
    }

    public static int interpolation(int a, int b, TurokDisplay display) {
        return (int) (a + (b - a) * display.getPartialTicks());
    }

    public static float interpolation(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static double interpolation(double a, double b, float t) {
        return a + (b - a) * t;
    }

    public static int interpolation(int a, int b, float t) {
        return (int) (a + (b - a) * t);
    }
    /* End of interpolation functions. */

    /* Start of math functions. */
    public static double clamp(double value, double minimum, double maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static float clamp(float value, float minimum, float maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static int clamp(int value, int minimum, int maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static long clamp(long value, long minimum, long maximum) {
        return value <= minimum ? minimum : (value >= maximum ? maximum : value);
    }

    public static int clamp(int alphaColor) {
        return alphaColor <= 0 ? 0 : (alphaColor >= 255 ? 255 : alphaColor);
    }

    public static int trunc(double value) {
        return (int) value;
    }

    public static int trunc(float value) {
        return (int) value;
    }
    /* End of math functions. */
}