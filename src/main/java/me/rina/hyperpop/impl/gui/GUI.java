package me.rina.hyperpop.impl.gui;

import me.rina.hyperpop.Client;
import me.rina.hyperpop.api.module.overlay.OverlayElement;
import me.rina.hyperpop.api.module.type.ModuleType;
import me.rina.hyperpop.impl.gui.api.IGUI;
import me.rina.hyperpop.impl.gui.api.base.frame.Frame;
import me.rina.hyperpop.impl.gui.api.engine.Processor;
import me.rina.hyperpop.impl.gui.api.engine.caller.Statement;
import me.rina.hyperpop.impl.gui.impl.frame.ElementFrame;
import me.rina.hyperpop.impl.gui.impl.frame.ModuleFrame;
import me.rina.hyperpop.impl.gui.impl.frame.PopupMenuFrame;
import me.rina.hyperpop.impl.module.impl.client.ModuleHUDEditor;
import me.rina.hyperpop.impl.module.impl.client.ModuleUserInterface;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokShaderGL;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import me.rina.turok.util.TurokTick;
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
    public static TurokFont FONT_BIG_NORMAL = new TurokFont(new Font("Tahoma", 0, 18), true, true);
    public static TurokFont FONT_NORMAL = new TurokFont(new Font("Tahoma", 0, 16), true, true);

    public static boolean BACKGROUND_MINECRAFT = true;
    public static boolean CUSTOM_BACKGROUND = false;
    public static boolean DRAW_WATERMARK = true;
    public static boolean HUD_EDITOR = false;

    public static double VERSION = 0.6;
    public static String GUI_WATERMARK = "User Interface " + VERSION + " official.";

    public static int SCALE_FACTOR = 2;
    public static int HEIGHT_LIMIT = 250;

    public static Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    private final List<Frame> loadedFrameList = new ArrayList<>();
    private final List<Frame> focusedFrameList = new ArrayList<>();

    private final List<IGUI> elementList = new ArrayList<>();

    private Frame focusedFrame;

    private final TurokMouse mouse;
    private final TurokDisplay display;

    private boolean isUpdate;
    private int distance;

    private final TurokTick slowerCooldownUsingAnWidgetTimer = new TurokTick();
    private final TurokTick theDoubleClickDelay = new TurokTick();

    private final PopupMenuFrame popupMenuFrame;

    protected boolean isMouseClickedLeft;
    protected boolean isMouseClickedRight;

    protected boolean isSelecting;
    protected final TurokRect selectRect = new TurokRect("select", 0, 0);

    protected float startSelectX;
    protected float startSelectY;

    public GUI() {
        this.mouse = new TurokMouse();
        this.display = new TurokDisplay(Minecraft.getMinecraft());

        this.setDistance(1);

        this.popupMenuFrame = new PopupMenuFrame(this);
    }

    public TurokTick getSlowerCooldownUsingAnWidgetTimer() {
        return slowerCooldownUsingAnWidgetTimer;
    }

    public PopupMenuFrame getPopupMenuFrame() {
        return popupMenuFrame;
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

    public void refreshConfiguration() {
        for (Frame frames : this.loadedFrameList) {
            if (frames instanceof ModuleFrame) {
                ModuleFrame frame = (ModuleFrame) frames;

                frame.reloadPositionConfiguration();
            }
        }
    }

    public void onReloadAll() {
        this.elementList.clear();

        for (Frame frames : this.loadedFrameList) {
            if (frames instanceof ModuleFrame && frames.getFlag().isEnabled()) {
                ((ModuleFrame) frames).onReloadAll();
            }
        }
    }

    public void addOverlayElement(OverlayElement element) {
        ElementFrame frame = new ElementFrame(this, element);
        frame.init();

        this.loadedFrameList.add(frame);
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
            this.elementList.add(frame);
        }
    }

    public void addElementUI(IGUI element) {
        this.elementList.add(element);
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
        if (keyCode == Keyboard.KEY_F9) {
            this.elementList.clear();
            this.focusedFrameList.clear();
            this.loadedFrameList.clear();

            this.init();
        }

        if (!this.isUpdate() && keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);

            return;
        }

        boolean keyFlagCtrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

        if (keyFlagCtrl && keyCode == Keyboard.KEY_A) {
            this.focusedFrameList.clear();

            for (Frame frames : this.loadedFrameList) {
                if (frames.getFlag().isEnabled()) {
                    this.focusedFrameList.add(frames);
                }
            }
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

        this.popupMenuFrame.onKeyboard(charCode, keyCode);
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

        this.isMouseClickedLeft = button == 99;
        this.isMouseClickedRight = button == 99;

        if (!this.focusedFrameList.isEmpty()) {
            for (Frame frames : this.focusedFrameList) {
                frames.onMouseReleased(button);
            }
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        this.popupMenuFrame.onMouseClicked(button);

        boolean keyFlagCtrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        boolean cancelClick = false;

        if (this.focusedFrame != null) {
            this.focusedFrame.onCustomMouseClicked(button);

            if (!this.focusedFrameList.contains(this.focusedFrame) && keyFlagCtrl && this.focusedFrame.getFlag().isMouseOverDraggable()) {
                this.focusedFrameList.add(this.focusedFrame);
            } else {
                if (keyFlagCtrl && !this.focusedFrame.getFlag().isMouseOverDraggable() && !this.focusedFrameList.isEmpty()) {
                    this.focusedFrameList.clear();

                    cancelClick = true;
                }
            }

            if ((!this.focusedFrame.getFlag().isMouseOverDraggable() && !keyFlagCtrl) || (!keyFlagCtrl && !this.focusedFrameList.contains(this.focusedFrame))) {
                this.focusedFrameList.clear();
            }
        }

        if (!cancelClick) {
            for (Frame frames : this.loadedFrameList) {
                frames.onMouseClicked(button);

                if (!keyFlagCtrl && frames.getFlag().isEnabled() && frames.getFlag().isMouseOverDraggable() && button == 0) {
                    if (this.theDoubleClickDelay.isPassedMS(200)) {
                        this.theDoubleClickDelay.reset();
                    } else {
                        if (!this.focusedFrameList.contains(frames)) {
                            this.focusedFrameList.add(frames);
                        }
                    }
                }
            }
        }

        if (!this.focusedFrameList.isEmpty() && this.focusedFrame != null && this.focusedFrame.getFlag().isMouseOverDraggable()) {
            for (Frame frames : this.focusedFrameList) {
                frames.getFlag().setMouseOverDraggable(true);
                frames.onMouseClicked(button);
            }
        }

        if ((this.focusedFrame == null || !this.focusedFrame.getFlag().isMouseOver()) && (!this.popupMenuFrame.getFlag().isEnabled() || !this.popupMenuFrame.getFlag().isMouseOver())) {
            this.isMouseClickedLeft = button == 0;
            this.isMouseClickedRight = button == 1;

            if (this.isMouseClickedLeft || this.isMouseClickedRight) {
                this.focusedFrameList.clear();

                this.selectRect.x = this.mouse.getX();
                this.selectRect.y = this.mouse.getY();

                this.startSelectX = this.selectRect.getX();
                this.startSelectY = this.selectRect.getY();
            }
        }
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        if (this.getSlowerCooldownUsingAnWidgetTimer().isPassedMS(1000)) {
            this.getSlowerCooldownUsingAnWidgetTimer().reset();
        }

        TurokShaderGL.init(this.display, this.mouse);

        this.display.setPartialTicks(partialTicks);
        this.display.update();

        this.mouse.setPos(mx, my);

        if (!HUD_EDITOR && BACKGROUND_MINECRAFT) {
            this.drawDefaultBackground();
        }

        Statement.unset(GL11.GL_TEXTURE_2D);

        for (IGUI element : this.elementList) {
            element.onUpdate();
        }

        this.focusedFrame = null;

        for (Frame frames : this.loadedFrameList) {
            if (frames.getFlag().isEnabled()) {
                if (!(this.focusedFrame.getFlag().isDragging())) {
                    frames.onRender();
                }

                if (frames.getFlag().isFocusing(frames.getRect().collideWithMouse(this.getMouse()))) {
                    this.focusedFrame = frames;
                }

                if (this.isSelecting) {
                    if (frames.getRect().collideWithRect(this.selectRect)) {
                        if (!this.focusedFrameList.contains(frames)) {
                            this.focusedFrameList.add(frames);
                        }
                    }  else {
                        if (this.focusedFrameList.contains(frames)) {
                            this.focusedFrameList.remove(frames);
                        }
                    }
                }

                if (this.focusedFrameList.contains(frames)) {
                    // Select pre background draw.
                    Processor.prepare(0, 178, 255, 100);
                    Processor.solid(frames.getRect());

                    // Select post background outline draw.
                    Processor.prepare(0, 178, 255, 255);
                    Processor.outline(frames.getRect());
                }
            }

            frames.clear();
        }

        if (this.focusedFrame != null) {
            if (this.focusedFrame.getFlag().isDragging()) {
                for (IGUI element : this.elementList) {
                    element.onRender();
                }
            }

            this.focusedFrame.onCustomUpdate();
            this.focusedFrame.onCustomRender();
        }

        if (DRAW_WATERMARK) {
            TurokFontManager.render(GUI.FONT_NORMAL, GUI_WATERMARK, 1, this.display.getScaledHeight() - 1 - TurokFontManager.getStringHeight(GUI.FONT_NORMAL, GUI_WATERMARK), true, new Color(255, 255, 255, 255));
        }

        // POST RENDER.
        this.popupMenuFrame.onRender();
        this.popupMenuFrame.onUpdate();
        this.popupMenuFrame.onCustomUpdate();

        if (this.isMouseClickedLeft || this.isMouseClickedRight) {
            this.isSelecting = true;

            float x = this.getMouse().getX();
            float y = this.getMouse().getY();

            if (x > this.startSelectX) {
                this.selectRect.setX(this.startSelectX);
                this.selectRect.setWidth(x - this.startSelectX);
            } else {
                this.selectRect.setX(x);
                this.selectRect.setWidth(this.startSelectX - x);
            }

            if (y > this.startSelectY) {
                this.selectRect.setY(this.startSelectY);
                this.selectRect.setHeight(y - this.startSelectY);
            } else {
                this.selectRect.setY(y);
                this.selectRect.setHeight(this.startSelectY - y);
            }

            // Select pre background draw.
            Processor.prepare(0, 178, 255, 100);
            Processor.solid(this.selectRect);

            // Select post background outline draw.
            Processor.prepare(0, 178, 255, 255);
            Processor.outline(this.selectRect);
        } else {
            this.isSelecting = false;
        }

        // POST hud editor worker & render.
        if (HUD_EDITOR) {
        }

        // POST FX.

        Statement.unset(GL11.GL_TEXTURE_2D);
        Statement.unset(GL11.GL_BLEND);

        Statement.set(GL11.GL_TEXTURE_2D);
        Statement.unset(GL11.GL_BLEND);
    }
}