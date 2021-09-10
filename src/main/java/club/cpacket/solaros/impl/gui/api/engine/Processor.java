package club.cpacket.solaros.impl.gui.api.engine;

import club.cpacket.solaros.impl.gui.api.engine.caller.Statement;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

/**
 * @author SrRina
 * @since 09/09/2021 at 17:40
 **/
public class Processor {
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
}