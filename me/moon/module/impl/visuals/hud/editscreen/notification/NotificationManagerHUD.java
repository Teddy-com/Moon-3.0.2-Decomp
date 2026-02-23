/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.editscreen.notification;

import java.util.ArrayList;
import me.moon.module.impl.visuals.hud.editscreen.notification.Notification;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManagerHUD {
    private ArrayList<Notification> notifications = new ArrayList();

    public void onDraw(ScaledResolution sr) {
        for (Notification notification : this.notifications) {
            AnimationUtil util = notification.getAnimationUtil();
            TimerUtil timerUtil = notification.getTimerUtil();
            if (notification.shouldShow()) {
                util.interpolate(0.0, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (timerUtil.hasReached(2000L)) {
                util.interpolate(-201.0, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
                notification.shouldShow = false;
            }
            RenderUtil.drawRect(util.getPosX(), sr.getScaledHeight() - 30, Minecraft.getMinecraft().fontRendererObj.getStringWidth(notification.getNotificationText()) + 5, 20.0, -14606047);
            RenderUtil.drawRect(util.getPosX() + (double)Minecraft.getMinecraft().fontRendererObj.getStringWidth(notification.getNotificationText()) + 5.0, sr.getScaledHeight() - 30, 1.0, 20.0, -15374912);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(notification.getNotificationText(), (float)util.getPosX() + 2.0f, sr.getScaledHeight() - 24, -1);
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

