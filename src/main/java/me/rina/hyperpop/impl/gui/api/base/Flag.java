package me.rina.hyperpop.impl.gui.api.base;

import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 10/09/2021 at 14:08
 **/
public class Flag {
    private boolean isMouseOver;
    private boolean isMouseOverDraggable;
    private boolean isMouseOverResizable;

    private boolean isLocked;
    private boolean isEnabled;

    private boolean isResizable;
    private boolean isResizing;

    private boolean isDragging;
    private boolean isDraggable;

    private TurokRect.Dock resizeDock;
    private String resize = "left-right-top-down";

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

    public void setMouseOverDraggable(boolean mouseOverDraggable) {
        isMouseOverDraggable = mouseOverDraggable;
    }

    public boolean isMouseOverDraggable() {
        return isMouseOverDraggable;
    }

    public void setResizeDock(TurokRect.Dock resizeDock) {
        this.resizeDock = resizeDock;
    }

    public TurokRect.Dock getResizeDock() {
        return resizeDock;
    }

    public String getResize() {
        return resize;
    }

    public void setResize(String resize) {
        this.resize = resize;
    }

    public void setResizing(boolean resizing) {
        isResizing = resizing;
    }

    public boolean isResizing() {
        return isResizing;
    }

    public void setMouseOverResizable(boolean mouseOverResizable) {
        isMouseOverResizable = mouseOverResizable;
    }

    public boolean isMouseOverResizable() {
        return isMouseOverResizable;
    }

    public void setResizable(boolean resizable) {
        isResizable = resizable;
    }

    public boolean isResizable() {
        return isResizable;
    }
}