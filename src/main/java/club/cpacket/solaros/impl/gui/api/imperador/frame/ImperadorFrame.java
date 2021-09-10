package club.cpacket.solaros.impl.gui.api.imperador.frame;

import club.cpacket.solaros.impl.gui.GUI;
import club.cpacket.solaros.impl.gui.api.IGUI;
import club.cpacket.solaros.impl.gui.api.base.Flag;
import club.cpacket.solaros.impl.gui.api.base.frame.Frame;
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

    public ImperadorFrame(GUI gui, String tag) {
        super(gui, tag);
    }

    public List<IGUI> getElementList() {
        return elementList;
    }

    public void add(IGUI element) {
        this.elementList.add(element);
    }

    public void setDrag(TurokRect rect) {
        final TurokMouse mouse = this.master.getMouse();

        this.setDragX(mouse.getX() - rect.getX());
        this.setDragY(mouse.getY() - rect.getY());
    }

    public void updateDrag() {
        final TurokMouse mouse = this.master.getMouse();

        if (this.flag.isDraggable() && this.flag.isDragging()) {
            this.rect.setX(mouse.getX() - this.getDragX());
            this.rect.setY(mouse.getY() - this.getDragY());
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
