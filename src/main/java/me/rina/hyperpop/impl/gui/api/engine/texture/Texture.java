package me.rina.hyperpop.impl.gui.api.engine.texture;

import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.turok.render.image.TurokImage;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author SrRina
 * @since 05/11/2021 at 20:30
 **/
public class Texture extends TurokRect {
    private ResourceLocation resourceLocation;
    private String path;

    private BufferedImage bufferedImage;
    private DynamicTexture dynamicTexture;

    private int textureWidth;
    private int textureHeight;

    private Color color = new Color(255, 255, 255, 255);

    public Texture(String path, float x, float y, float width, float height) {
        super(x, y, width, height);

        this.path = path;
        this.reload();
    }

    public Texture(String path, String tag, float x, float y, float width, float height) {
        super(tag, x, y, width, height);

        this.path = path;
        this.reload();
    }

    public Texture(String path, float x, float y) {
        super(x, y);

        this.path = path;
        this.reload();
    }

    public Texture(String path, String tag, float x, float y) {
        super(tag, x, y);

        this.path = path;
        this.reload();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setDynamicTexture(DynamicTexture dynamicTexture) {
        this.dynamicTexture = dynamicTexture;
    }

    public DynamicTexture getDynamicTexture() {
        return dynamicTexture;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public void setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public void setColor(int red, int green, int blue, int alpha) {
        if (this.color.getRed() != red || this.color.getGreen() != green || this.color.getBlue() != blue || this.color.getAlpha() != alpha) {
            this.color = new Color(Processor.clamp(red), Processor.clamp(green), Processor.clamp(blue), Processor.clamp(alpha));
        }
    }

    public Color getColor() {
        return this.color;
    }

    public void reload() {
        try {
            final BufferedImage bufferedImage = ImageIO.read(Texture.class.getResourceAsStream(this.path));

            this.bufferedImage = bufferedImage;
        } catch (IOException exc) {

        }
    }

    public void load() {
        this.dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("/hyperpop/", this.dynamicTexture);
    }
}
