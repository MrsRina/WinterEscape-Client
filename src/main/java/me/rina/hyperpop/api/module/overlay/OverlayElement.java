package me.rina.hyperpop.api.module.overlay;

import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.api.value.type.CheckBox;

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

	public OverlayElement(String tag, String description, boolean useString) {
		super(tag + "OverlayComponent", description, ModuleType.HUD);
		
		this.rect.setTag(tag);
		this.useString = useString;

		if (this.useString) {
			this.registry(this.customFont = new CheckBox("CustomFont", "Set for render with custom font.", false));
			this.registry(this.shadowFont = new CheckBox("ShadowFont", "Set shadow effect for render font.", true));
			this.registry(this.color = new Combobox("Color", "Set the color for render.", "HUD", "RGB", "HUE", "HUD"));
		}
	}

	public String getName() {
		return this.rect.getTag();
	}

	public boolean useString() {
		return useString;
	}

	public void string(String string, int alignedX, int alignedY) {
		if (!this.useString) {
			return;
		}

		final Color color = this.color.is("HUD") ? ModuleHUDEditor.COLOR_HUD : (this.color.is("RGB") ? new Color(255, 0, 255, 100) : (this.color.is("HUE") ? null : new Color(255, 0, 255, 255)));
	
		this.string(string, alignedX, alignedY, color);
	}

	public void string(String string, int alignedX, int algnedY, Color color) {
		if (!this.useString) {
			return;
		}

		if (this.customFont.getValue()) {
		} else {
			if (this.shadowFont.getValue()) {

			} else {

			}
		}
	}

	public void fill(Color color) {
		this.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlha());
	}

	public void fill(int red, int green, int blue, int alpha) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFun(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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

		int lastX = this.rect.x;
		int lastY = this.rect.y;

		int memoryW = this.rect.w;
		int memoryH = this.rect.h;

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

		return this.dock == TurokRect.Dock.RIGHT_TOP || this.dock == TurokRect.Dock.RIGHT_DOWN ? this.rect.w - sizeW - unalignedX : alignedX;
	}
	/* End of utilities functions of overlay element. */
}