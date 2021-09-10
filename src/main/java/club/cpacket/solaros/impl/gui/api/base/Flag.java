package club.cpacket.solaros.impl.gui.api.base;

/**
 * @author SrRina
 * @since 10/09/2021 at 14:08
 **/
public class Flag {
    private boolean isMouseOver;
    private boolean isEnabled;

    private boolean isLocked;
    private boolean isDraggable;

    private boolean isDragging;

    public Flag() {}

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setDraggable(boolean draggable) {
        isDraggable = draggable;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isDragging() {
        return isDragging;
    }
}
