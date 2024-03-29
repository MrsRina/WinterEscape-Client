package me.rina.winterescape.impl.module.impl.hud;

import me.rina.winterescape.api.module.overlay.OverlayElement;

/**
 * @author SrRina
 * @since 18/11/2021 at 00:15
 **/
public class OverlayElementWelcome extends OverlayElement {
    public OverlayElementWelcome() {
        super("Welcome", "Show cool welcome to you.", true);
    }

    private String message;

    private int lastSizeWidth = -1;
    private int lastSizeHeight = -1;

    @Override
    public void onRender(float partialTicks) {
        if (mc.player == null) {
            return;
        }

        this.message = "Welcome " + mc.player.getName();
        this.string(this.message, 0, 0);

        this.lastSizeWidth = this.getStringWidth(this.message);
        this.lastSizeHeight = this.getStringHeight(this.message);

        this.rect.setWidth(this.lastSizeWidth);
        this.rect.setHeight(this.lastSizeHeight);
    }
}
