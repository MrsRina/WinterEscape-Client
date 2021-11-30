package me.rina.zyra.api.event;

/**
 * @author SrRina
 * @since 07/09/2021 at 15:50
 **/
public class Event extends event.bus.Event { // Its not a feature...
    private int stage;

    public Event() {}

    public void setPre() {
        this.stage = 0;
    }

    public boolean isPre() {
        return stage == 0;
    }

    public void setPost() {
        this.stage = 1;
    }

    public boolean isPost() {
        return stage == 1;
    }
}
