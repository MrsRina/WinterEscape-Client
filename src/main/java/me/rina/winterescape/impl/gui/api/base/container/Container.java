package me.rina.winterescape.impl.gui.api.base.container;

import me.rina.winterescape.impl.gui.GUI;
import me.rina.winterescape.impl.gui.api.IGUI;
import me.rina.winterescape.impl.gui.api.base.Flag;
import me.rina.turok.util.TurokRect;

/**
 * @author SrRina
 * @since 09/09/2021 at 18:30
 **/
public class Container implements IGUI {
    protected final TurokRect rect;
    protected final Flag flag;
    protected final GUI master;

    protected float offsetX;
    protected float offsetY;

    protected float offsetW;
    protected float offsetH;

    public Container(GUI gui, String tag) {
        this.rect = new TurokRect(tag, 0, 0, 0, 0);
        this.flag = new Flag();
        this.master = gui;
    }

    public void setOffsetX(float x) {
        this.offsetX = x;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float y) {
        this.offsetY = y;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetW(float w) {
        this.offsetW = w;
    }

    public float getOffsetW() {
        return offsetW;
    }

    public void setOffsetH(float h) {
        this.offsetH = h;
    }

    public float getOffsetH() {
        return offsetH;
    }

    @Override
    public TurokRect getRect() {
        return rect;
    }

    @Override
    public Flag getFlag() {
        return this.flag;
    }

    @Override
    public GUI getGUI() {
        return this.master;
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

    public void clear() {}
}
