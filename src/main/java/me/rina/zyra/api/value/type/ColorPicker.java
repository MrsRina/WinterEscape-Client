package me.rina.zyra.api.value.type;

import me.rina.zyra.api.value.Value;
import me.rina.zyra.impl.gui.api.engine.Processor;

import java.awt.*;

/**
 * @author SrRina
 * @since 26/11/2021 at 18:46
 **/
public class ColorPicker extends Value {
    private boolean value;
    private boolean isRGB;

    protected boolean containsAdditionalCheckBox;

    private int red;
    private int green;
    private int blue;

    private int alpha;

    private float brightness;
    private float saturation;

    public ColorPicker(String tag, String description, boolean value, Color color) {
        super(tag, description, ValueType.COLOR_PICKER);

        this.containsAdditionalCheckBox = true;
        this.value = value;

        this.setColor(color);
    }

    public ColorPicker(String tag, String description, Color color) {
        super(tag, description, ValueType.COLOR_PICKER);

        this.containsAdditionalCheckBox = false;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setColor(Color color) {
        this.setRed(color.getRed());
        this.setGreen(color.getGreen());
        this.setBlue(color.getBlue());
        this.setAlpha(color.getAlpha());
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getRed() {
        return red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getGreen() {
        return green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getBlue() {
        return blue;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setEffectRGB() {
        this.isRGB = true;
    }

    public void unsetEffectRGB() {
        this.isRGB = false;
    }

    public boolean isRGB() {
        return isRGB;
    }

    public Color getColor() {
        return this.getColor(this.getAlpha());
    }

    public Color getColor(int alpha) {
        if (this.isRGB()) {
            float[] currentSystemCycle = {
                    (System.currentTimeMillis() % (360 * 32)) / (360f * 32f)
            };

            int currentColorCycle = Color.HSBtoRGB(currentSystemCycle[0], this.getSaturation(), this.getBrightness());

            return new Color(((currentColorCycle >> 16) & 0xFF), ((currentColorCycle >> 8) & 0xFF), (currentColorCycle & 0xFF), Processor.clamp(alpha));
       }

        return new Color(Processor.clamp(this.getRed()), Processor.clamp(this.getGreen()), Processor.clamp(this.getBlue()), Processor.clamp(alpha));
    }

    public boolean containsAdditionalCheckBox() {
        return containsAdditionalCheckBox;
    }
}