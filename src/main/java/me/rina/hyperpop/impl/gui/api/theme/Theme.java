package me.rina.hyperpop.impl.gui.api.theme;

import java.awt.*;

/**
 * @author SrRina
 * @since 15/10/2021 at 12:10
 **/
public class Theme {
    public static Theme INSTANCE;

    public Color background = new Color(45, 45, 45, 255);
    public Color selected = new Color(255, 255, 0, 255);

    public Color highlight = new Color(255, 255, 255, 50);
    public Color pressed = new Color(255, 255, 0, 50);

    public Theme() {
        INSTANCE = this;
    }

    public void setBackground(int red, int green, int blue, int alpha) {
        if (this.background.getRed() != red || this.background.getGreen() != green || this.background.getBlue() != blue || this.background.getAlpha() != alpha) {
            this.background = new Color(red, green, blue, alpha);
        }
    }

    public void setSelected(int red, int green, int blue, int alpha) {
        if (this.selected.getRed() != red || this.selected.getGreen() != green || this.selected.getBlue() != blue || this.selected.getAlpha() != alpha) {
            this.selected = new Color(red, green, blue, alpha);
        }
    }

    public void setHighlight(int red, int green, int blue, int alpha) {
        if (this.highlight.getRed() != red || this.highlight.getGreen() != green || this.highlight.getBlue() != blue || this.highlight.getAlpha() != alpha) {
            this.highlight = new Color(red, green, blue, alpha);
        }
    }

    public void setPressed(int red, int green, int blue, int alpha) {
        if (this.pressed.getRed() != red || this.pressed.getGreen() != green || this.pressed.getBlue() != blue || this.pressed.getAlpha() != alpha) {
            this.pressed = new Color(red, green, blue, alpha);
        }
    }

    public void updateAllColor() {

    }
}
