package club.cpacket.solaros.impl.gui.api.base.frame;

import club.cpacket.solaros.impl.gui.api.IGUI;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 09/09/2021 at 18:31
 **/
public class Frame implements IGUI {
    protected final TurokRect rect;

    public Frame(String tag) {
        this.rect = new TurokRect(tag, 0, 0, 0, 0);
    }

    public TurokRect getRect() {
        return rect;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onCustomKeyboard(char charCode, int keyCode) {

    }

    @Override
    public void onMouseReleased(int button) {

    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {

    }

    @Override
    public void onCustomMouseClicked(int button) {

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

    }
}