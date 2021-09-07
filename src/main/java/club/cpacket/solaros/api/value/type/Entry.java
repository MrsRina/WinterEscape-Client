package club.cpacket.solaros.api.value.type;

import club.cpacket.solaros.api.value.Value;

/**
 * @author SrRina
 * @since 06/09/2021 at 21:02
 **/
public class Entry extends Value {
    public static final int ENTRY = 22;

    private String value;

    public Entry(String tag, String description, String text) {
        super(tag, description, ENTRY);

        this.setValue(text);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
