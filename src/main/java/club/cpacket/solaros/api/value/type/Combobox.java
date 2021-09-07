package club.cpacket.solaros.api.value.type;

import club.cpacket.solaros.api.value.Value;

import java.util.List;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:50
 **/
public class Combobox extends Value {
    public static final int COMBOBOX = 244;

    private String value;
    private List<String> list;

    public Combobox(String tag, String description, String value, String... values) {
        super(tag, description, COMBOBOX);

        this.setValue(value);
        this.implement(values);
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
