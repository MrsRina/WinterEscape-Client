package club.cpacket.solaros.impl.gui.api;

import club.cpacket.solaros.impl.gui.GUI;
import club.cpacket.solaros.impl.gui.api.base.Flag;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 09/09/2021 at 17:14
 **/
public interface IGUI {
    TurokRect getRect();
    Flag getFlag();
    GUI getGUI();

    void onOpen();
    void onClose();

    void onKeyboard(char charCode, int keyCode);
    void onCustomKeyboard(char charCode, int keyCode);

    void onMouseReleased(int button);
    void onCustomMouseReleased(int button);

    void onMouseClicked(int button);
    void onCustomMouseClicked(int button);

    void onUpdate();
    void onCustomUpdate();

    void onRender();
    void onCustomRender();
}
