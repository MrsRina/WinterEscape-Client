package me.rina.hyperpop.impl.gui.impl.frame;

import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.Flag;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokRect;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 16/11/2021 at 15:08
 **/
public class PopupMenuFrame extends Frame {
    private final ArrayList<String> buttonList = new ArrayList<>();

    private String origin;
    private String callback;
    private String over;

    private boolean isFocusedByCPU;
    protected int interpolatedAlphaTick;

    private boolean releasedCallback;

    public PopupMenuFrame(GUI master) {
        super(master, null);
    }

    public void callPopup(String instance, float x, float y, float w, String origin, List<String> list) {
        this.clearButtonList();

        this.rect.setTag(instance);
        this.rect.setWidth(w);

        float size = this.master.getDistance();

        for (String buttons : list) {
            size += 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, buttons);
        }

        this.rect.setHeight(size);

        this.rect.setX(x + this.rect.getWidth() >= this.master.getDisplay().getScaledWidth() ? this.master.getDisplay().getScaledWidth() - this.rect.getWidth() - 1 : x);
        this.rect.setY(y + this.rect.getHeight() >= this.master.getDisplay().getScaledHeight() ? this.master.getDisplay().getScaledHeight() - this.rect.getHeight() - 1 : y);

        this.setOrigin(origin);
        this.newButtonList(list);

        this.flag.setEnabled(true);
    }

    public void clearButtonList() {
        this.buttonList.clear();
    }

    public void newButtonList(List<String> list) {
        this.buttonList.addAll(list);
    }

    public void setFocusedByCPU(boolean focusedByCPU) {
        isFocusedByCPU = focusedByCPU;
    }

    public boolean isFocusedByCPU() {
        return isFocusedByCPU;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCallback() {
        return callback;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setReleasedCallback(boolean releasedCallback) {
        this.releasedCallback = releasedCallback;
    }

    public boolean isReleasedCallback() {
        return releasedCallback;
    }

    public boolean isMouseOver(float x, float y, float w, float h) {
        float mx = this.master.getMouse().getX();
        float my = this.master.getMouse().getY();

        return mx >= x && my >= y && mx <= x + w && my <= y + h;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {
        this.setReleasedCallback(false);
        this.flag.setEnabled(false);
        this.setCallback("null");
        this.setOrigin("null");
        this.clearButtonList();
    }

    @Override
    public void onKeyboard(char charCode, int keyCode) {
        if (this.flag.isEnabled() && keyCode == Keyboard.KEY_ESCAPE) {
            this.onClose();
        }
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
        if (!this.flag.isEnabled()) {
            return;
        }

        if (!this.flag.isMouseOver()) {
            this.onClose();

            return;
        }

        if (this.over == null) {
            return;
        }

        setCallback(this.over);
        setReleasedCallback(true);
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()) && this.flag.isEnabled());

        if (this.flag.isEnabled()) {
            this.master.setUpdate();
            this.setFocusedByCPU(true);
        } else {
            if (this.isFocusedByCPU()) {
                this.master.unsetUpdate();
                this.setFocusedByCPU(false);
            }
        }
    }

    @Override
    public void onCustomUpdate() {
        this.interpolatedAlphaTick = Processor.interpolation(this.interpolatedAlphaTick, this.flag.isEnabled() ? 255 : 0, this.master.getDisplay());
    }

    @Override
    public void onRender() {
        if (this.interpolatedAlphaTick <= 10) {
            return;
        }

        // Background.
        Processor.prepare(Theme.INSTANCE.getFocused(Processor.clamp(this.interpolatedAlphaTick, 0, Theme.INSTANCE.focused.getAlpha())));
        Processor.solid(this.rect);

        this.over = null;

        float y = 0;

        for (String buttons : this.buttonList) {
            float h = 6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, buttons);

            // Selected/origin.
            if (buttons.equalsIgnoreCase(this.origin)) {
                Processor.prepare(Theme.INSTANCE.getSelected(Processor.clamp(this.interpolatedAlphaTick, 0, Theme.INSTANCE.selected.getAlpha())));
                Processor.solid(this.rect.getX(), this.rect.getY() + y, this.rect.getWidth(), h);
            }

            // Highlight.
            if (this.isMouseOver(this.rect.getX(), this.rect.getY() + y, this.rect.getWidth(), h)) {
                this.over = buttons;

                Processor.prepare(Theme.INSTANCE.getHighlight(Processor.clamp(this.interpolatedAlphaTick, 0, Theme.INSTANCE.highlight.getAlpha())));
                Processor.solid(this.rect.getX(), this.rect.getY() + y, this.rect.getWidth(), h);
            }

            // Name.
            Processor.string(GUI.FONT_NORMAL, buttons, this.rect.getX() + 1f, this.rect.getY() + y + 3, Processor.clamp(this.interpolatedAlphaTick, 0, Theme.INSTANCE.string.getAlpha()));

            y += h;
        }
    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void clear() {

    }
}
