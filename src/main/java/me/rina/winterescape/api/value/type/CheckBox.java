package me.rina.winterescape.api.value.type;

import me.rina.winterescape.api.value.Value;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:40
 **/
public class CheckBox extends Value {
    private boolean value;

    public CheckBox(String tag, String description, boolean value) {
        super(tag, description, ValueType.CHECK_BOX);

        this.setValue(value);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
