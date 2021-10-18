package me.rina.hyperpop.api.value;

import me.rina.hyperpop.api.feature.Feature;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:15
 **/
public class Value extends Feature {
    private int type;

    /*
     * For set the visibility of setting (value).
     */
    private boolean isShow;

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

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isShow() {
        return isShow;
    }
}
