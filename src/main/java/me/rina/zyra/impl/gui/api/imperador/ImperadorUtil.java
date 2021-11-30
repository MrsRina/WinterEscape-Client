package me.rina.zyra.impl.gui.api.imperador;

import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.util.TurokRect;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * @author SrRina
 * @since 28/08/2021 at 19:05
 **/
public class ImperadorUtil {
    public static String get() {
        final java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(null);

        if (content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return content.getTransferData(DataFlavor.stringFlavor).toString();
            } catch (UnsupportedFlavorException | IOException exc) {
                return null;
            }
        }

        return null;
    }

    public static void set(final String string) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(new StringSelection(string), null);
    }

    public static TurokRect.Dock verifyResizeDock(TurokMouse mouse, TurokRect rect, float offset, String pattern) {
        final float x = mouse.getX();
        final float y = mouse.getY();

        TurokRect.Dock dock = null;

        if (x <= rect.getX() + offset && pattern.contains("left")) {
            dock = TurokRect.Dock.CENTER_LEFT;
        }

        if (y <= rect.getY() + offset && pattern.contains("up")) {
            dock = TurokRect.Dock.TOP_CENTER;
        }

        if (x >= rect.getX() + rect.getWidth() - offset && pattern.contains("right")) {
            dock = TurokRect.Dock.CENTER_RIGHT;
        }

        if (y >= rect.getY() + rect.getHeight() - offset && pattern.contains("down")) {
            dock = TurokRect.Dock.BOTTOM_CENTER;
        }

        return dock;
    }
}

