package club.cpacket.solaros.impl.gui.api;

/**
 * @author SrRina
 * @since 09/09/2021 at 17:14
 **/
public interface IGUI {
    boolean isEnabled();

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
