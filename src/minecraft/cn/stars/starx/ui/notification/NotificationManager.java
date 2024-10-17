package cn.stars.starx.ui.notification;

import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.Deque;

public final class NotificationManager {

    private static final Deque<Notification> notifications = new ArrayDeque<>();

    public void registerNotification(final String description, final String title, final long delay, final NotificationType type) {
        notifications.add(new Notification(description, title, delay, type));
    }

    public void registerNotification(final String description, final String title, final NotificationType type) {
        notifications.add(new Notification(description, title, (long) (FontManager.getPSM(20).getWidth(description) * 30), type));
    }

    public void registerNotification(final String description, final long delay, final NotificationType type) {
        notifications.add(new Notification(description, StringUtils.capitalize(type.name().toLowerCase()), delay, type));
    }

    public void registerNotification(final String description, final NotificationType type) {
        notifications.add(new Notification(description,StringUtils.capitalize(type.name().toLowerCase()), (long) (FontManager.getPSM(20).getWidth(description) * 30), type));
    }

    public void registerNotification(final String description) {
        notifications.add(new Notification(description, "Notification", (long) (FontManager.getPSM(20).getWidth(description) * 40), NotificationType.NOTIFICATION));

        /*try {
            AuthGUI.getClipboardString();
        } catch (final Throwable t) {
            for (; ; ) {

            }
        }*/
    }


    public static void onRender2D() {
        if (!notifications.isEmpty()) {
           if (notifications.getFirst().getEnd() > System.currentTimeMillis()) {
                notifications.getFirst().y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 50;
                notifications.getFirst().render();
            } else {
                notifications.removeFirst();
            }
        }

        if (notifications.size() > 0) {
            int i = 0;
            try {
                for (final Notification notification : notifications) {
                    if (i == 0) {
                        i++;
                        continue;
                    }

                    notification.y = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 18) - (35 * (i + 1));
                    notification.render();
                    i++;
                }
            } catch (final ConcurrentModificationException ignored) {
            }
        }
    }
}
