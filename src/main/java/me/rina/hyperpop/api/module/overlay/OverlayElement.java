package me.rina.hyperpop.api.module.overlay;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.hyperpop.impl.gui.GUI;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.base.Flag;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.util.TurokMath;
import net.minecraft.entity.passive.AbstractHorse;
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
import org.apache.commons.lang3.CharUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import org.lwjgl.opengl.GL11;

/**
 * @author Rina
 * @since 21/10/2021 at 00:23am
 **/
public class OverlayElement extends Module implements IGUI {
	protected final TurokRect rect = new TurokRect("overlay", 0, 0);
	protected final boolean useString;

	protected TurokRect.Dock dock = TurokRect.Dock.TOP_LEFT;
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

	public void string(String string, int x, int alignedY) {
		if (!this.useString) {
			return;
		}

		final Color color = this.color.is("HUD") ? ModuleHUDEditor.COLOR_HUD : (this.color.is("RGB") ? Client.getCycleColor() : (this.color.is("HUE") ? null : new Color(255, 0, 255, 255)));
	
		this.string(string, x, alignedY, color);
	}

	public void string(String string, int x, int alignedY, Color color) {
		if (!this.useString) {
			return;
		}

		int alignedX = this.getAlignedPositionX(x, this.getStringWidth(string));

		if (color == null) {
			TurokFontManager.render(Client.OVERLAY_FONT, string, this.rect.x + alignedX, this.rect.y + alignedY, this.shadowFont.getValue(), 20);
			// this.drawCustomHUE(string, this.rect.x + alignedX, this.rect.y + alignedY);

			return;
		}

		Statement.matrix();
		Statement.set(GL11.GL_TEXTURE_2D);
		Statement.blend();

		if (this.customFont.getValue()) {
			if (this.shadowFont.getValue()) {
				Client.OVERLAY_FONT.drawStringWithShadow(string, (int) this.rect.x + alignedX, (int) this.rect.y + alignedY, color.getRGB());
			} else {
				Client.OVERLAY_FONT.drawString(string, (int) this.rect.x + alignedX, (int) this.rect.y + alignedY, color.getRGB());
			}
		} else {
			if (this.shadowFont.getValue()) {
				mc.fontRenderer.drawStringWithShadow(string, (int) this.rect.x + alignedX, (int) this.rect.y + alignedY, color.getRGB());
			} else {
				mc.fontRenderer.drawString(string, (int) this.rect.x + alignedX, (int) this.rect.y + alignedY, color.getRGB());
			}
		}

		Statement.refresh();
	}

	public void drawCustomHUE(String string, float alignedX, float alignedY) {
		int cycleColor = Color.HSBtoRGB((System.currentTimeMillis() % (360 * 32)) / (360f * 32), 1, 1);

		Color currentColor = new Color(((cycleColor >> 16) & 0xFF), ((cycleColor >> 8) & 0xFF), ((cycleColor) & 0xFF));

		float hueIncrement = 1.0f / 8f;
		float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
		float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
		float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];

		float currentWidth = 0;

		boolean shouldRainbow = true;
		boolean shouldContinue = false;

		ChatFormatting colorCache = ChatFormatting.GRAY;

		for (int i = 0; i < string.length(); ++i) {
			char currentChar = string.charAt(i);
			char nextChar = string.charAt(TurokMath.clamp(i + 1, 0, string.length() - 1));

			String nextFormatting = (String.valueOf(currentChar) + nextChar);

			if (nextFormatting.equals("\u00a7r") && !shouldRainbow) {
				shouldRainbow = true;
			} else {
				if (String.valueOf(currentChar).equals("\u00a7")) {
					shouldRainbow = false;
				}
			}
			if (shouldContinue) {
				shouldContinue = false;

				continue;
			}

			if (String.valueOf(currentChar).equals("\u00a7")) {
				shouldContinue = true;

				colorCache = ChatFormatting.getByChar(CharUtils.toChar(nextFormatting.replaceAll("\u00a7", "")));

				continue;
			}

			Statement.matrix();
			Statement.set(GL11.GL_TEXTURE_2D);
			Statement.blend();

			if (this.customFont.getValue()) {
				if (this.shadowFont.getValue()) {
					Client.OVERLAY_FONT.drawStringWithShadow(string, (int) alignedX, (int) alignedY, currentColor.getRGB());
				} else {
					Client.OVERLAY_FONT.drawString(string, (int) alignedX, (int) alignedY, currentColor.getRGB());
				}
			} else {
				if (this.shadowFont.getValue()) {
					mc.fontRenderer.drawStringWithShadow(string, (int) alignedX, (int) alignedY, currentColor.getRGB());
				} else {
					mc.fontRenderer.drawString(string, (int) alignedX, (int) alignedY, currentColor.getRGB());
				}
			}

			Statement.refresh();

			currentWidth += this.getStringWidth(String.valueOf(currentChar));

			if (String.valueOf(currentChar).equals(" ")) {
				continue;
			}

			currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
			currentHue += hueIncrement;
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

	@Override
	public void onOverlayRender(float partialTicks) {
        float[] currentSystemCycle = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32f)
        };

        int currentColorCycle = Color.HSBtoRGB(currentSystemCycle[0], 1, 1);

		this.onRender(partialTicks);
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

	@Override
	public TurokRect getRect() {
		return this.rect;
	}

	@Override
	public Flag getFlag() {
		return null;
	}

	@Override
	public GUI getGUI() {
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