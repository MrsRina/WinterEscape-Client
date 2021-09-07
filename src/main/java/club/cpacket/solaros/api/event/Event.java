package club.cpacket.solaros.api.event;

import javax.swing.plaf.PanelUI;

/**
 * @author SrRina
 * @since 07/09/2021 at 15:50
 **/
public class Event { // Its not a feature...
    private boolean cancelled;
    private int stage;

    public Event() {}

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

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
