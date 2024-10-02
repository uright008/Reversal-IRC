package cn.stars.starx.util.misc;

import cn.stars.starx.util.StarXLogger;

import java.awt.*;
import java.net.URI;

public class WebUtil {
    public static void openWebPage(String url) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI(url);
                desktop.browse(uri);
                StarXLogger.info("(GuiMainMenuNew) Opened web page: " + uri);
            } catch (final Exception e) {
                StarXLogger.error("(GuiMainMenuNew) Error while opening web page.", e);
            }
        }
    }
}
