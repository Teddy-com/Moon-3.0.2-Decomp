/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.account.notification;

import java.util.ArrayList;
import me.moon.gui.account.notification.Notification;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
    private ArrayList<Notification> notifications = new ArrayList();

    public void onDraw(ScaledResolution sr) {
        for (Notification notification : this.notifications) {
            AnimationUtil util = notification.getAnimationUtil();
            TimerUtil timerUtil = notification.getTimerUtil();
            if (notification.shouldShow()) {
                util.interpolate(0.0, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (timerUtil.hasReached(2000L)) {
                util.interpolate(-Fonts.sectioNormal.getStringWidth(notification.getNotificationText()) - 8, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
                notification.shouldShow = false;
            }
            RenderUtil.drawRect(util.getPosX(), sr.getScaledHeight() - 30, Fonts.sectioNormal.getStringWidth(notification.getNotificationText()) + 5, 20.0, -14606047);
            RenderUtil.drawRect(util.getPosX() + (double)Fonts.sectioNormal.getStringWidth(notification.getNotificationText()) + 5.0, sr.getScaledHeight() - 30, 1.0, 20.0, -15374912);
            Fonts.sectioNormal.drawStringWithShadow(notification.getNotificationText(), (float)util.getPosX() + 2.0f, sr.getScaledHeight() - 23, -1);
        }
    }

    public void addNotification(String notificationText, int time) {
        Notification notification = new Notification();
        notification.setNotificationText(notificationText);
        notification.setDuration(time);
        notification.shouldShow = true;
        notification.getAnimationUtil().setPosX(-198.0);
        this.notifications.add(notification);
    }
}

