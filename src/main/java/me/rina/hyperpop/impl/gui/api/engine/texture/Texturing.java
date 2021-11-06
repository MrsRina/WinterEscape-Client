package me.rina.hyperpop.impl.gui.api.engine.texture;

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

    public static void frame(int x, int y, int w, int h, int ow, int oh, boolean repeat) {
        float i = 1;
        float k = 1;

        if (repeat) {
            i = 1f + ((float) w - ow) / ow;
            k = 1f + ((float) h - oh) / oh;
        }

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(i, 0); GL11.glVertex2f(x + w, y);

        GL11.glTexCoord2f(i, k); GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(0, k); GL11.glVertex2f(x, y + h);

        GL11.glEnd();
    }

    public static void render(Texture texture) {

    }
}