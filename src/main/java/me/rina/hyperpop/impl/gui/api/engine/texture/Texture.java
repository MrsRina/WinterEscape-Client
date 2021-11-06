package me.rina.hyperpop.impl.gui.api.engine.texture;

import me.rina.turok.render.image.TurokImage;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
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

    public Texture(String path, float x, float y, float width, float height) {
        super(x, y, width, height);

        this.path = path;
    }

    public Texture(String path, String tag, float x, float y, float width, float height) {
        super(tag, x, y, width, height);

        this.path = path;
    }

    public Texture(String path, float x, float y) {
        super(x, y);

        this.path = path;
    }

    public Texture(String path, String tag, float x, float y) {
        super(tag, x, y);

        this.path = path;
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

    public void load() {
        try {
            this.bufferedImage = ImageIO.read(Texture.class.getResourceAsStream(this.path));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        this.dynamicTexture = new DynamicTexture(this.bufferedImage);
        this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("hyperpop/textures/", this.dynamicTexture);
    }
}
