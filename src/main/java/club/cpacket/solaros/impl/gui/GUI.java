package club.cpacket.solaros.impl.gui;

import club.cpacket.solaros.impl.event.ClientTickEvent;
import club.cpacket.solaros.impl.gui.api.IGUI;
import club.cpacket.solaros.impl.gui.api.base.Flag;
import event.bus.EventListener;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author SrRina
 * @since 08/09/2021 at 15:19
 **/
public class GUI extends GuiScreen implements IGUI {
    private final TurokMouse mouse;
    private final TurokDisplay display;

    public GUI() {
        this.mouse = new TurokMouse();
        this.display = new TurokDisplay(mc);
    }

    public TurokDisplay getDisplay() {
        return display;
    }

    public TurokMouse getMouse() {
        return mouse;
    }

    @EventListener
    public void onClientTickEvent(ClientTickEvent event) {
        this.onUpdate();
        this.onCustomUpdate();
    }

    @Override
    public void initGui() {
        this.onOpen();
    }

    @Override
    public void onGuiClosed() {
        this.onClose();
    }

    @Override
    public void keyTyped(char charCode, int keyCode) {
        this.onKeyboard(charCode, keyCode);
        this.onCustomKeyboard(charCode, keyCode);
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        this.onMouseReleased(button);
        this.onCustomMouseReleased(button);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        this.onMouseClicked(button);
        this.onCustomMouseClicked(button);
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        this.mouse.setPos(mx, my);

        this.display.update();
        this.display.setPartialTicks(partialTicks);

        this.onRender();
        this.onCustomRender();
    }

    @Override
    public TurokRect getRect() {
        return null;
    }

    @Override
    public Flag getFlag() {
        return null;
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
