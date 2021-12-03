package me.rina.winterescape.impl.gui.api.imperador.frame;

import me.rina.winterescape.impl.gui.GUI;
import me.rina.winterescape.impl.gui.api.IGUI;
import me.rina.winterescape.impl.gui.api.base.frame.Frame;
import me.rina.winterescape.impl.gui.api.imperador.ImperadorUtil;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.util.TurokRect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 09/09/2021 at 19:41
 **/
public class ImperadorFrame extends Frame implements IGUI {
    private final List<IGUI> elementList = new ArrayList<>();

    private float dragX;
    private float dragY;

    private float resizeX;
    private float resizeY;

    private float minimumWidth;
    private float minimumHeight;

    private final TurokRect rectResize = new TurokRect("resize:rect", 0, 0);
    protected final TurokRect rectDrag = new TurokRect("drag:rect", 0, 0);

    private float offsetResize;

    public ImperadorFrame(GUI gui, String tag) {
        super(gui, tag);
    }

    public List<IGUI> getElementList() {
        return elementList;
    }

    public void add(IGUI element) {
        this.elementList.add(element);
        this.master.addElementUI(element);
    }

    public TurokRect getRectDrag() {
        return rectDrag;
    }

    public void unset() {
        if (this.flag.isResizing()) {
            this.flag.setResizing(false);
        }

        if (this.flag.isDragging()) {
            this.flag.setDragging(false);
        }

        if (this.flag.isMouseClickedLeft()) {
            this.flag.setMouseClickedLeft(false);
        }

        if (this.flag.isMouseClickedRight()) {
            this.flag.setMouseClickedRight(false);
        }

        if (this.flag.isMouseClickedMiddle()) {
            this.flag.setMouseClickedMiddle(false);
        }
    }

    public void setDrag() {
        final TurokMouse mouse = this.master.getMouse();

        this.setDragX(mouse.getX() - this.rect.getX());
        this.setDragY(mouse.getY() - this.rect.getY());

        this.flag.setDragging(true);
    }

    public void updateDrag() {
        final TurokMouse mouse = this.master.getMouse();

        if (this.flag.isDraggable() && this.flag.isDragging()) {
            this.rect.setX(mouse.getX() - this.getDragX());
            this.rect.setY(mouse.getY() - this.getDragY());
        }
    }

    public void updateMouseOver() {
        final TurokMouse mouse = this.master.getMouse();

        this.flag.setMouseOver((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rect.collideWithMouse(mouse));
        this.flag.setMouseOverDraggable((!this.master.getPopupMenuFrame().getFlag().isEnabled() || !this.master.getPopupMenuFrame().getFlag().isMouseOver()) && this.rectDrag.collideWithMouse(this.master.getMouse()) && !this.rectResize.collideWithMouse(mouse));
    }

    public void setResize() {
        final TurokMouse mouse = this.master.getMouse();

        if (this.flag.isMouseOverResizable() && this.flag.isResizable()) {
            this.flag.setResizeDock(ImperadorUtil.verifyResizeDock(this.master.getMouse(), this.rect, this.getOffsetResize(), this.flag.getResize()));

            this.setResizeX(mouse.getX() - this.rect.getX());
            this.setResizeY(mouse.getY() - this.rect.getY());

            this.flag.setResizing(true);
        }
    }

    public void updateResize() {
        final TurokRect.Dock dock = this.flag.getResizeDock();

        if (this.flag.isResizable() && this.flag.isResizing() && dock != null) {
        }
    }

    public void setDragX(float dragX) {
        this.dragX = dragX;
    }

    public float getDragX() {
        return dragX;
    }

    public void setDragY(float dragY) {
        this.dragY = dragY;
    }

    public float getDragY() {
        return dragY;
    }

    public void setResizeX(float resizeX) {
        this.resizeX = resizeX;
    }

    public float getResizeX() {
        return resizeX;
    }

    public void setResizeY(float resizeY) {
        this.resizeY = resizeY;
    }

    public float getResizeY() {
        return resizeY;
    }

    public void setMinimumWidth(float minimumWidth) {
        this.minimumWidth = minimumWidth;
    }

    public float getMinimumWidth() {
        return minimumWidth;
    }

    public void setMinimumHeight(float minimumHeight) {
        this.minimumHeight = minimumHeight;
    }

    public float getMinimumHeight() {
        return minimumHeight;
    }

    public void setOffsetResize(float offsetResize) {
        this.offsetResize = offsetResize;
    }

    public float getOffsetResize() {
        return offsetResize;
    }

    @Override
    public void onOpen() {
        for (IGUI elements : this.getElementList()) {
            elements.onOpen();
        }
    }

    @Override
    public void onClose() {
        for (IGUI elements : this.getElementList()) {
            elements.onClose();
        }

        this.flag.setDragging(false);
        this.flag.setResizing(false);
        this.flag.setDragging(false);
        this.flag.setMouseClickedLeft(false);
        this.flag.setMouseClickedRight(false);
        this.flag.setMouseClickedMiddle(false);
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        for (IGUI elements : this.getElementList()) {
            elements.onKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {
        for (IGUI elements : this.getElementList()) {
            elements.onCustomKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (IGUI elements : this.getElementList()) {
            elements.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (IGUI elements : this.getElementList()) {
            elements.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (IGUI elements : this.getElementList()) {
            elements.onMouseClicked(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        for (IGUI elements : this.getElementList()) {
            elements.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onUpdate() {
        this.rectResize.set(this.rect.getX() + this.getOffsetResize(), this.rect.getY() + this.getOffsetResize(), this.rect.getWidth() - (this.getOffsetResize() * 2), this.rect.getHeight() - (this.getOffsetResize() * 2));

        this.flag.setMouseOver(false);
        this.flag.setMouseOverDraggable(false);
    }

    @Override
    public void onCustomUpdate() {
    }

    @Override
    public void onRender() {

    }

    @Override
    public void onCustomRender() {
        for (IGUI elements : this.getElementList()) {
            elements.onCustomRender();
        }
    }
}
