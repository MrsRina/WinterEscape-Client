package me.rina.hyperpop.api.value.type;

import me.rina.hyperpop.api.value.Value;

/**
 * @author SrRina
 * @since 06/09/2021 at 21:02
 **/
public class Entry extends Value {
    private String value;
    private String set;

    private boolean focused;

    public Entry(String tag, String description, String text) {
        super(tag, description, ValueType.ENTRY);

        this.setValue(text);
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public void motherfuck(String set) {
        this.value = set;
    }

    public String lastSet() {
        return set;
    }

    public void unset() {
        this.set = null;
    }

    public void setValue(String value) {
        this.value = value;
        this.set = value;
    }

    public String getValue() {
        return value;
    }
}
