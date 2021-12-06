package me.rina.winterescape.impl.gui.api.theme;

import me.rina.winterescape.impl.module.impl.client.ModuleUserInterface;

import java.awt.*;

/**
 * @author SrRina
 * @since 15/10/2021 at 12:10
 **/
public class Theme {
    public static Theme INSTANCE;

    public Color background = new Color(45, 45, 45, 255);
    public Color focused = new Color(20, 20, 20, 255);

    public Color selected = new Color(97, 97, 232, 200);
    public Color highlight = new Color(255, 255, 255, 50);
 
    public Color pressed = new Color(255, 255, 0, 50);
    public Color string = new Color(255, 255, 255, 255);

    public Theme() {
        INSTANCE = this;
    }

    public Color getBackground(int alpha) {
        return new Color(this.background.getRed(), this.background.getGreen(), this.background.getBlue(), clamp(alpha));
    }

    public void setBackground(int red, int green, int blue, int alpha) {
        if (this.background.getRed() != red || this.background.getGreen() != green || this.background.getBlue() != blue || this.background.getAlpha() != alpha) {
            this.background = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public Color getFocused(int alpha) {
        return new Color(this.focused.getRed(), this.focused.getGreen(), this.focused.getBlue(), clamp(alpha));
    }

    public void setFocused(int red, int green, int blue, int alpha) {
        if (this.focused.getRed() != red || this.focused.getGreen() != green || this.focused.getBlue() != blue || this.focused.getAlpha() != alpha) {
            this.focused = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public Color getSelected(int alpha) {
        return new Color(this.selected.getRed(), this.selected.getGreen(), this.selected.getBlue(), clamp(alpha));
    }

    public void setSelected(int red, int green, int blue, int alpha) {
        if (this.selected.getRed() != red || this.selected.getGreen() != green || this.selected.getBlue() != blue || this.selected.getAlpha() != alpha) {
            this.selected = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public Color getHighlight(int alpha) {
        return new Color(this.highlight.getRed(), this.highlight.getGreen(), this.highlight.getBlue(), clamp(alpha));
    }

    public void setHighlight(int red, int green, int blue, int alpha) {
        if (this.highlight.getRed() != red || this.highlight.getGreen() != green || this.highlight.getBlue() != blue || this.highlight.getAlpha() != alpha) {
            this.highlight = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public Color getPressed(int alpha) {
        return new Color(this.pressed.getRed(), this.pressed.getGreen(), this.pressed.getBlue(), clamp(alpha));
    }

    public void setPressed(int red, int green, int blue, int alpha) {
        if (this.pressed.getRed() != red || this.pressed.getGreen() != green || this.pressed.getBlue() != blue || this.pressed.getAlpha() != alpha) {
            this.pressed = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public Color getString(int alpha) {
        return new Color(this.string.getRed(), this.string.getGreen(), this.string.getBlue(), clamp(alpha));
    }

    public void setString(int red, int green, int blue, int alpha) {
        if (this.string.getRed() != red || this.string.getGreen() != green || this.string.getBlue() != blue || this.string.getAlpha() != alpha) {
            this.string = new Color(clamp(red), clamp(green), clamp(blue), clamp(alpha));
        }
    }

    public boolean shadow$True$False(Color background) {
        return background.getAlpha() <= 100;
    }

    public int clamp(int v) {
        return v >= 255 ? 255 : Math.max(v, 0);
    }

    public void updateAllColor() {
        this.background = ModuleUserInterface.settingBackground.getColor();
        this.focused = ModuleUserInterface.settingFocused.getColor();
        this.selected = ModuleUserInterface.settingSelected.getColor();
        this.highlight = ModuleUserInterface.settingHighlight.getColor();
        this.pressed = ModuleUserInterface.settingPressed.getColor();
        this.string = ModuleUserInterface.settingString.getColor();
    }
}
