package me.rina.hyperpop.api.value.type;

import me.rina.hyperpop.api.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:50
 **/
public class Combobox extends Value {
    private String value;
    private List<String> list;

    public Combobox(String tag, String description, String value, String... values) {
        super(tag, description, ValueType.COMBOBOX);

        this.list = new ArrayList<>();

        this.implement(values);
        this.setValue(value);
    }

    public void setValue(String value) {
        if (this.getList().isEmpty()) {
            this.value = "";

            return;
        }

        String equals = this.getList().get(0);

        for (String elements : this.getList()) {
            if (elements.equalsIgnoreCase(value)) {
                equals = elements;

                break;
            }
        }

        this.value = equals;
    }

    public String getValue() {
        return value;
    }

    public boolean is(String v) {
        return this.getValue().equalsIgnoreCase(v);
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void implement(String... list) {
        for (final String elementsToAdd : list) {
            this.add(elementsToAdd);
        }
    }

    public void add(String elementToAdd) {
        this.getList().add(elementToAdd);
    }
}
