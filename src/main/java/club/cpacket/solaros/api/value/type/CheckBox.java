package club.cpacket.solaros.api.value.type;

import club.cpacket.solaros.api.value.Value;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:40
 **/
public class CheckBox extends Value {
    public static final int CHECK_BOX = 19;

    private boolean value;

    public CheckBox(String tag, String description, boolean value) {
        super(tag, description, CHECK_BOX);

        this.setValue(value);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
