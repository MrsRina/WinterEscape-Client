package club.cpacket.solaros.api.value;

import club.cpacket.solaros.api.feature.Feature;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:15
 **/
public class Value extends Feature {
    private int type;

    public Value(String tag, String description, int type) {
        super(tag, description);

        this.setType(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
