package me.rina.hyperpop.impl.gui.api.engine;

import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import sun.font.FontManager;

import java.awt.*;

/**
 * @author SrRina
 * @since 09/09/2021 at 17:40
 **/
public class Processor {
    public static Minecraft theMinecraft = Minecraft.getMinecraft();

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
        Statement.prepare(GL11.GL_QUADS);

        Statement.vertex2d(x, y);
        Statement.vertex2d(x, y + h);

        Statement.vertex2d(x + w, y + h);
        Statement.vertex2d(x + w, y);

        Statement.draw();
        Statement.refresh();
    }

    public static void outline(TurokRect rect) {
        outline(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static void outline(float x, float y, float w, float h) {
        Statement.set(GL11.GL_LINE_SMOOTH);
        Statement.prepare(GL11.GL_LINE);

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

    public static void string(TurokFont font, String string, float x, float y, boolean shadow) {
        GlStateManager.enableAlpha();

        if (shadow) {
            if (font.isRenderingCustomFont()) {
                font.drawStringWithShadow(string, x, y, Color.WHITE.getRGB());
            } else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, (int) x, (int) y, Color.WHITE.getRGB());
            }
        } else {
            if (font.isRenderingCustomFont()) {
                font.drawString(string, x, y, Color.WHITE.getRGB());
            } else {
                Minecraft.getMinecraft().fontRenderer.drawString(string, (int) x, (int) y, Color.WHITE.getRGB());
            }
        }

        Statement.refresh();
    }
    /* End of post fx render functions. */

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
}