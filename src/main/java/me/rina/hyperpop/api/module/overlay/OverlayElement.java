package me.rina.hyperpop.api.module.overlay;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.value.type.CheckBox;
import me.rina.hyperpop.api.value.type.Combobox;
import me.rina.hyperpop.api.value.type.Entry;
import me.rina.hyperpop.api.value.type.Slider;
import me.rina.hyperpop.impl.module.impl.client.ModuleHUDEditor;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Rina
 * @since 21/10/2021 at 00:23am
 **/
public class OverlayElement extends Module {
	private final TurokRect rect = new TurokRect("overlay", 0, 0);
	private final boolean useString;

	protected TurokRect.Dock dock;
	protected boolean postFX;

	protected CheckBox customFont;
	protected CheckBox shadowFont;
	protected Combobox color;

	protected Slider positionX;
	protected Slider positionY;

	protected Entry lastDock;

	public OverlayElement(String tag, String description, boolean useString) {
		super(tag + "OverlayComponent", description, ModuleType.HUD);
		
		this.rect.setTag(tag);
		this.useString = useString;

		if (this.useString) {
			this.registry(this.customFont = new CheckBox("CustomFont", "Set for render with custom font.", false));
			this.registry(this.shadowFont = new CheckBox("ShadowFont", "Set shadow effect for render font.", true));
			this.registry(this.color = new Combobox("Color", "Set the color for render.", "HUD", "RGB", "HUE", "HUD"));
		}

		this.registry(this.positionX = new Slider("RectPositionX", "The position x.", 5, -12000, 12000));
		this.registry(this.positionY = new Slider("RectPositionY", "The position y.", 5, -12000, 12000));
		this.registry(this.lastDock = new Entry("RectLastDock", "The last dock.", this.dock.name()));

		this.positionX.setShow(false);
		this.positionY.setShow(false);
		this.lastDock.setShow(false);
	}

	public String getName() {
		return this.rect.getTag();
	}

	public boolean useString() {
		return useString;
	}

	public void resetPosition$Dock() {
		this.rect.setX(this.positionX.getValue().intValue());
		this.rect.setY(this.positionY.getValue().intValue());

		for (Enum values : TurokRect.Dock.values()) {
			if (values.name().equalsIgnoreCase(this.lastDock.getValue())) {
				this.dock = (TurokRect.Dock) values;

				break;
			}
		}
	}

	public void string(String string, int alignedX, int alignedY) {
		if (!this.useString) {
			return;
		}

		final Color color = this.color.is("HUD") ? ModuleHUDEditor.COLOR_HUD : (this.color.is("RGB") ? new Color(255, 0, 255, 100) : (this.color.is("HUE") ? null : new Color(255, 0, 255, 255)));
	
		this.string(string, alignedX, alignedY, color);
	}

	public void string(String string, int x, int y, Color color) {
		if (!this.useString) {
			return;
		}

		int alignedX = this.getAlignedPositionX(x, this.getStringWidth(string));

		if (this.customFont.getValue()) {
            TurokFontManager.render(Client.OVERLAY_FONT, string, this.rect.x + alignedX, this.rect.y + y, this.shadowFont.getValue(), color);
		} else {
			if (this.shadowFont.getValue()) {
			    mc.fontRenderer.drawStringWithShadow(string, this.rect.x + alignedX, this.rect.y + y, color.getRGB());
			} else {
			    mc.fontRenderer.drawString(string, (int) this.rect.x + alignedX, (int) this.rect.y + y, color.getRGB());
			}
		}
	}

	public int getStringWidth(String string) {
	    if (!this.useString) {
	        return 0;
        }

	    if (this.customFont.getValue()) {
	        return TurokFontManager.getStringWidth(Client.OVERLAY_FONT, string);
        }

	    return mc.fontRenderer.getStringWidth(string);
    }

	public int getStringHeight(String string) {
	    if (!this.useString) {
	        return 0;
        }

	    if (this.customFont.getValue()) {
	        return TurokFontManager.getStringHeight(Client.OVERLAY_FONT, string);
        }

	    return mc.fontRenderer.FONT_HEIGHT;
    }

	public void fill(Color color) {
		this.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public void fill(int red, int green, int blue, int alpha) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
	}

	public void solid(int x, int y, int sizeW, int sizeH) {
		int alignedX = this.getAlignedPositionX(x, sizeW);
		int alignedY = y;

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex3f(this.rect.x + alignedX, this.rect.y + alignedY, 0);
		GL11.glVertex3f(this.rect.x + alignedX, this.rect.y + alignedY + sizeH, 0);

		GL11.glVertex3f(this.rect.x + alignedX + sizeW, this.rect.y + alignedY + sizeH, 0);
		GL11.glVertex3f(this.rect.x + alignedX + sizeW, this.rect.y + alignedY, 0);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public void onRender(float partialTicks) {

	}

	public void collision(TurokDisplay display, int offset) {
		int x = offset;
		int y = offset;

		int w = display.getScaledWidth() - offset;
		int h = display.getScaledHeight() - offset;

		int lastX = (int) this.rect.x;
		int lastY = (int) this.rect.y;

		int memoryW = (int) this.rect.width;
		int memoryH = (int) this.rect.height;

		if (lastX <= x) {
			lastX = x;
		}

		if (lastY <= y) {
			lastY = y;
		}

		if (lastX + memoryW >= w) {
			lastX = w - memoryW;
		}

		if (lastY + memoryH >= h) {
			lastY = h - memoryH;
		}

		int currentX = lastX;
		int currentY = lastY;

		this.rect.setX(currentX);
		this.rect.setY(currentY);
	}

	/* Start utilities functions of overlay element. */
	public int getAlignedPositionX(int unalignedX, int sizeW) {
		int alignedX = unalignedX;

		return this.dock == TurokRect.Dock.TOP_RIGHT || this.dock == TurokRect.Dock.BOTTOM_RIGHT ? (int) (this.rect.width - sizeW - unalignedX) : alignedX;
	}
	/* End of utilities functions of overlay element. */
}