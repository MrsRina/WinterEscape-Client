package me.rina.hyperpop.impl.gui.api.engine.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 21/10/2021 at 18:25
 **/
public class Texturing {
    public static final List<Texture> TEXTURE_LIST = new ArrayList<>();

    public static Texture load(String path) {
        Texture texture;

        TEXTURE_LIST.add(texture = new Texture(path, 0, 0));

        return texture;
    }

    public static void frame(float x, float y, float w, float h, int ow, int oh, boolean repeat) {
        float i = 1;
        float k = 1;

        if (repeat) {
            i = 1f + (w - ow) / ow;
            k = 1f + (h - oh) / oh;
        }

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(i, 0); GL11.glVertex2f(x + w, y);

        GL11.glTexCoord2f(i, k); GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(0, k); GL11.glVertex2f(x, y + h);

        GL11.glEnd();
    }

    public static void render(Texture texture, boolean repeat) {
        GL11.glPushMatrix();

        renderPrimitive(texture, repeat);

        GL11.glPushMatrix();
    }

    public static void render(Texture texture) {
        render(texture, false);
    }

    public static void renderPrimitive(Texture texture, boolean repeat) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f);

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture.getResourceLocation());
        frame(texture.x, texture.y, texture.width, texture.height, texture.getTextureWidth(), texture.getTextureHeight(), repeat);
    }

    public static void renderPrimitive(Texture texture) {
        renderPrimitive(texture, false);
    }
}