package me.rina.hyperpop.api.value;

import me.rina.hyperpop.api.value.type.ValueType;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:38
 **/
public class ValueGeneric <T> extends Value {
    private T value;

    public ValueGeneric(String tag, String description, T generic) {
        super(tag, description, ValueType.GENERIC);

        this.setValue(generic);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
