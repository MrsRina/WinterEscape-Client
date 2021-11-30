package me.rina.zyra.api.value.type;

import me.rina.zyra.api.value.Value;

/**
 * @author SrRina
 * @since 07/09/2021 at 14:48
 **/
public class BindBox extends Value {
    private boolean value;
    private int key;

    public BindBox(String tag, String description, boolean state) {
        super(tag, description, ValueType.BIND_BOX);

        this.setValue(state);
        this.setKey(-1);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
