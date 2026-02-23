/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.editscreen.notification;

import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.TimerUtil;

public class Notification {
    public String notificationText;
    public float posX;
    public float posY;
    public AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);
    public TimerUtil timerUtil = new TimerUtil();
    public int duration = 0;
    public boolean shouldShow = true;

    public boolean shouldShow() {
        return this.shouldShow;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public AnimationUtil getAnimationUtil() {
        return this.animationUtil;
    }

    public TimerUtil getTimerUtil() {
        return this.timerUtil;
    }

    public String getNotificationText() {
        return this.notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

