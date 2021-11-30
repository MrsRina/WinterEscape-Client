package me.rina.zyra.impl.gui.api.base;

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
    private boolean isSelected;

    private boolean isResizable;
    private boolean isResizing;

    private boolean isDragging;
    private boolean isDraggable;

    private boolean isMouseClickedLeft;
    private boolean isMouseClickedRight;
    private boolean isMouseClickedMiddle;

    private TurokRect.Dock resizeDock;
    private String resize = "left-right-top-down";

    public Flag() {}

    public boolean isFocusing(boolean forced) {
        if (this.isLocked() || !this.isEnabled()) {
            return false;
        }

        return this.isMouseOver() || forced;
    }

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

    public void setMouseClickedLeft(boolean mouseClickedLeft) {
        isMouseClickedLeft = mouseClickedLeft;
    }

    public boolean isMouseClickedLeft() {
        return isMouseClickedLeft;
    }

    public void setMouseClickedRight(boolean mouseClickedRight) {
        isMouseClickedRight = mouseClickedRight;
    }

    public boolean isMouseClickedRight() {
        return isMouseClickedRight;
    }

    public void setMouseClickedMiddle(boolean mouseClickedMiddle) {
        isMouseClickedMiddle = mouseClickedMiddle;
    }

    public boolean isMouseClickedMiddle() {
        return isMouseClickedMiddle;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
