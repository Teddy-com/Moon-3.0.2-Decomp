/*
 * Decompiled with CFR 0.152.
 */
package me.moon.notification;

import java.util.ArrayList;
import me.moon.Moon;
import me.moon.notification.Notification;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
    private final ArrayList<Notification> notifications = new ArrayList();

    public ArrayList<Notification> getNotifications() {
        return this.notifications;
    }

    public void addNotification(String text, long duration) {
        this.getNotifications().add(new Notification(text, duration));
    }

    public void renderNotifications() {
        ScaledResolution sr = RenderUtil.translateGuiScale(new ScaledResolution(Minecraft.getMinecraft()));
        float neededY = sr.getScaledHeight() - 30;
        for (int i = 0; i < Moon.INSTANCE.getNotificationManager().getNotifications().size(); ++i) {
            Moon.INSTANCE.getNotificationManager().getNotifications().get(i).renderNotification(neededY -= 30.0f);
        }
    }

    public void bloom() {
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        this.notifications.forEach(Notification::bloom);
    }

    public void blur() {
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        this.notifications.forEach(Notification::blur);
    }
}

