package me.rina.winterescape.impl.gui.api.engine.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.*;

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

    public static Texture get(String path) {
        Texture texture = null;

        for (Texture textures : TEXTURE_LIST) {
            if (textures.getPath().equalsIgnoreCase(path)) {
                texture = textures;

                break;
            }
        }

        return texture;
    }

    /**
     * The reason for I do not use this method is because ALL
     * gl context, different from 'GuiScreen.drawModalRectWit
     * hCustomSizedTexture()' using tessellator (VBO).
     **/
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
        if (texture == null || texture.getTag() == null || texture.getBufferedImage() == null || texture.getDynamicTexture() == null || texture.getResourceLocation() == null) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(texture.x, texture.y, 0f);
        renderPrimitive(texture, repeat);

        GL11.glPopMatrix();
    }

    public static void render(Texture texture) {
        render(texture, false);
    }

    public static void renderPrimitive(Texture texture, boolean repeat) {
        if (texture == null || texture.getTag() == null || texture.getBufferedImage() == null || texture.getDynamicTexture() == null || texture.getResourceLocation() == null) {
            return;
        }

        RenderHelper.enableGUIStandardItemLighting();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture.getResourceLocation());

        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        GlStateManager.color(1f, 1f, 1f, 1f);

        float x = 0;
        float y = 0;
        float textureWidth = texture.width;
        float textureHeight = texture.height;

        float u = 0;
        float v = 0;

        float t = 1;
        float s = 1;

        float width = textureWidth;
        float height = textureHeight;

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(width, y, 0F).tex(t, v).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        bufferbuilder.pos(x, y, 0F).tex(u, v).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        bufferbuilder.pos(x, y + height, 0F).tex(u, s).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        bufferbuilder.pos(x, y + height, 0F).tex(u, s).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0F).tex(t, s).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        bufferbuilder.pos(x + width, y, 0F).tex(t, v).color(texture.getColor().getRed() / 255f, texture.getColor().getGreen() / 255f, texture.getColor().getBlue() / 255f, texture.getColor().getAlpha() / 255f).endVertex();
        tessellator.draw();

        RenderHelper.disableStandardItemLighting();
    }

    public static void renderPrimitive(Texture texture) {
        renderPrimitive(texture, false);
    }
}