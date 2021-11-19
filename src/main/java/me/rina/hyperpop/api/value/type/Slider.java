package me.rina.hyperpop.api.value.type;

import me.rina.hyperpop.api.value.Value;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:45
 **/
public class Slider extends Value {
    private Number value;

    private Number minimum;
    private Number maximum;

    public Slider(String tag, String description, Number value, Number minimum, Number maximum) {
        super(tag, description, ValueType.SLIDER);

        this.setValue(value);

        this.setMinimum(minimum);
        this.setMaximum(maximum);
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    public void setMinimum(Number minimum) {
        this.minimum = minimum;
    }

    public Number getMinimum() {
        return minimum;
    }

    public void setMaximum(Number maximum) {
        this.maximum = maximum;
    }

    public Number getMaximum() {
        return maximum;
    }
}
