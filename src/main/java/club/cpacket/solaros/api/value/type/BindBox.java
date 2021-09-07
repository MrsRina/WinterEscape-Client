package club.cpacket.solaros.api.value.type;

import club.cpacket.solaros.api.value.Value;

/**
 * @author SrRina
 * @since 07/09/2021 at 14:48
 **/
public class BindBox extends Value {
    public static final int BIND_BOX = 666;

    private boolean value;
    private int key;

    public BindBox(String tag, String description, boolean state) {
        super(tag, description, BIND_BOX);

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
