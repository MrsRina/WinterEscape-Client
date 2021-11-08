package me.rina.hyperpop.impl.gui;

import event.bus.EventListener;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.event.ClientTickEvent;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.impl.frame.ModuleFrame;
import me.rina.hyperpop.impl.module.impl.client.ModuleHUDEditor;
import me.rina.hyperpop.impl.module.impl.client.ModuleUserInterface;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.util.TurokDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 10/09/2021 at 15:30
 **/
public class GUI extends GuiScreen {
    public static TurokFont FONT_BIG_NORMAL = new TurokFont(new Font("Verdana", 0, 18), true, true);
    public static TurokFont FONT_NORMAL = new TurokFont(new Font("Verdana", 0, 16), true, true);

    public static Color SHADOW_COLOR = new Color(0, 0, 0, 50);

    private final List<Frame> loadedFrameList = new ArrayList<>();
    private Frame focusedFrame;

    private final TurokMouse mouse;
    private final TurokDisplay display;

    private boolean isUpdate;
    private int distance;

    public static int SCALE_FACTOR = 2;

    public static int HEIGHT_LIMIT = 500;
    public static boolean HUD_EDITOR = false;

    public static boolean BACKGROUND_MINECRAFT = true;
    public static boolean CUSTOM_BACKGROUND = false;

    public static boolean DRAW_WATERMARK = true;

    public static double VERSION = 0.6;
    public static String GUI_WATERMARK = "User Interface " + VERSION + " alpha.";

    public GUI() {
        this.mouse = new TurokMouse();
        this.display = new TurokDisplay(Minecraft.getMinecraft());

        this.setDistance(1);
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public TurokMouse getMouse() {
        return mouse;
    }

    public TurokDisplay getDisplay() {
        return display;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate() {
        this.isUpdate = true;
    }

    public void unsetUpdate() {
        this.isUpdate = false;
    }

    public void matrixMoveFocusedFrameToLast() {
        if (this.focusedFrame == null) {
            return;
        }

        this.loadedFrameList.remove(this.focusedFrame);
        this.loadedFrameList.add(this.focusedFrame);
    }

    public void init() {
        int offsetSpace = 10;

        for (int i = 0; i < ModuleType.SIZE; i++) {
            ModuleFrame frame = new ModuleFrame(this, i);

            frame.init();

            frame.getRect().setX(offsetSpace);
            frame.getRect().setY(10);

            offsetSpace += frame.getRect().getWidth() + 1;

            this.loadedFrameList.add(frame);
        }
    }

    @Override
    public void initGui() {
        for (Frame frames : this.loadedFrameList) {
            if (!frames.getFlag().isEnabled()) {
                continue;
            }

            frames.onOpen();
        }
    }

    @Override
    public void onGuiClosed() {
        this.unsetUpdate();

        for (Frame frames : this.loadedFrameList) {
            if (!frames.getFlag().isEnabled()) {
                continue;
            }

            frames.onClose();
        }

        ModuleUserInterface.INSTANCE.unsetListener();
        ModuleHUDEditor.INSTANCE.unsetListener();
    }

    @Override
    public void keyTyped(char charCode, int keyCode) {
        if (!this.isUpdate() && keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);

            return;
        }

        for (Frame frames : this.loadedFrameList) {
            if (!frames.getFlag().isEnabled()) {
                continue;
            }

            frames.onKeyboard(charCode, keyCode);
        }

        if (this.focusedFrame != null) {
            this.focusedFrame.onCustomKeyboard(charCode, keyCode);
        }
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            if (!frames.getFlag().isEnabled()) {
                continue;
            }

            frames.onMouseReleased(button);
        }

        if (this.focusedFrame != null) {
            this.focusedFrame.onCustomMouseReleased(button);
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);
        }

        if (this.focusedFrame != null) {
            this.focusedFrame.onCustomMouseClicked(button);
        }
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        this.display.setPartialTicks(partialTicks);
        this.display.update();

        this.mouse.setPos(mx, my);

        if (!HUD_EDITOR && BACKGROUND_MINECRAFT) {
            this.drawDefaultBackground();
        }

        Statement.matrix();
        Statement.translate(this.display.getScaledWidth(), this.display.getScaledHeight(), 0);

        Statement.scale(0.5f, 0.5f, 0.5f);
        Statement.refresh();

        Statement.set(GL11.GL_TEXTURE_2D);

        for (Frame frames : this.loadedFrameList) {
            frames.onUpdate();
            
            if (frames.getFlag().isEnabled()) {
                // Shadow.
                if (this.focusedFrame == frames) {
                    Processor.prepare(GUI.SHADOW_COLOR);
                    Processor.outline(this.focusedFrame.getRect().x + 0.4f, this.focusedFrame.getRect().y + 0.4f, this.focusedFrame.getRect().width + 1, this.focusedFrame.getRect().height + 1);
                }

                frames.onRender();

                if (frames.getFlag().isFocusing(frames.getRect().collideWithMouse(this.getMouse()))) {
                    this.focusedFrame = frames;
                }
            }

            frames.clear();
        }


        if (DRAW_WATERMARK) {
            TurokFontManager.render(GUI.FONT_NORMAL, GUI_WATERMARK, 1, this.display.getScaledHeight() - 1 - TurokFontManager.getStringHeight(GUI.FONT_NORMAL, GUI_WATERMARK), true, new Color(255, 255, 255, 255));
        }

        if (this.focusedFrame != null) {
            this.focusedFrame.onCustomUpdate();
            this.focusedFrame.onCustomRender();
        }

        // Post hud editor render.
        if (HUD_EDITOR) {

        }

        Statement.set(GL11.GL_TEXTURE_2D);
        Statement.unset(GL11.GL_BLEND);
    }
}