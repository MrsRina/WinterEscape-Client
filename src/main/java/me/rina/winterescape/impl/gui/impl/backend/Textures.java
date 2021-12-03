package me.rina.winterescape.impl.gui.impl.backend;

import me.rina.winterescape.api.module.type.ModuleType;
import me.rina.winterescape.impl.gui.api.engine.texture.Texture;
import me.rina.winterescape.impl.gui.api.engine.texture.Texturing;

/**
 * @author SrRina
 * @since 14/11/2021 at 15:32
 **/
public class Textures {
    public static final String UI_CHECKBOX = "/assets/ui/checkboxtrue.png";
    public static final String UI_ARROW_UP = "/assets/ui/arrowup.png";
    public static final String UI_ARROW_DOWN = "/assets/ui/arrowdown.png";

    public static void init$load() {
        Texturing.load(UI_CHECKBOX).load();
        Texturing.load(UI_ARROW_UP).load();
        Texturing.load(UI_ARROW_DOWN).load();

        for (int i = 0; i < ModuleType.SIZE; i++) {
            Texturing.load("/assets/generic/" + ModuleType.toString(i).toLowerCase() + ".png");
        }
    }

    public static boolean set(Texture origin, Texture copy) {
        origin.setPath(copy.getPath());
        origin.setDynamicTexture(copy.getDynamicTexture());
        origin.setBufferedImage(copy.getBufferedImage());
        origin.setResourceLocation(copy.getResourceLocation());

        return true;
    }
}
