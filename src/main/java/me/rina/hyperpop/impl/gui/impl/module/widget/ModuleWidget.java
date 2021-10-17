package me.rina.hyperpop.impl.gui.impl.module.widget;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.base.widget.Widget;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.theme.Theme;
import me.rina.hyperpop.impl.gui.impl.module.frame.ModuleFrame;
import me.rina.turok.render.font.management.TurokFontManager;

/**
 * @author SrRina
 * @since 17/10/2021 at 12:52
 **/
public class ModuleWidget extends Widget {
    private final ModuleFrame mother;
    private final Module module;

    protected int interpolatedSelectedAlpha;
    protected int interpolatedPressedAlpha;
    protected int interpolatedHighlightAlpha;

    public ModuleWidget(GUI gui, ModuleFrame mother, Module module) {
        super(gui, module.getTag());

        this.module = module;
        this.mother = mother;

        this.rect.setHeight(6 + TurokFontManager.getStringHeight(GUI.FONT_NORMAL, this.module.getTag()));
    }

    public void init() {

    }

    public ModuleFrame getMother() {
        return mother;
    }

    public Module getModule() {
        return module;
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
        this.flag.setMouseOver(false);
        this.flag.setResizable(false);
        this.flag.setDraggable(false);

        this.rect.setWidth(this.getMother().getRect().getWidth());
    }

    @Override
    public void onCustomUpdate() {
        this.flag.setMouseOver(this.rect.collideWithMouse(this.master.getMouse()));
    }

    @Override
    public void onRender() {
        // Selected draw.
        this.interpolatedSelectedAlpha = Processor.interpolation(this.interpolatedSelectedAlpha, this.module.isEnabled() ? Theme.INSTANCE.selected.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getSelected(this.interpolatedSelectedAlpha));
        Processor.solid(this.rect);

        // Pressed draw.
        this.interpolatedPressedAlpha = Processor.interpolation(this.interpolatedPressedAlpha, this.flag.isMouseClickedLeft() ? Theme.INSTANCE.pressed.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getPressed(this.interpolatedPressedAlpha));
        Processor.solid(this.rect);

        // Highlight draw.
        this.interpolatedHighlightAlpha = Processor.interpolation(this.interpolatedHighlightAlpha, this.flag.isMouseOver() ? Theme.INSTANCE.highlight.getAlpha() : 0, this.master.getDisplay());

        Processor.prepare(Theme.INSTANCE.getHighlight(this.interpolatedHighlightAlpha));
        Processor.solid(this.rect);
    }

    @Override
    public void onCustomRender() {

    }
}
