/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.notification;

import me.moon.Moon;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Notification {
    private final TimerUtil timer;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private float x;
    private float y;
    private float width;
    private final String text;
    private final long stayTime;
    private boolean done;
    private float stayBar;
    private final AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public Notification(String text, long stayTime) {
        ScaledResolution sr = RenderUtil.translateGuiScale(new ScaledResolution(mc));
        this.x = sr.getScaledWidth() - 2;
        this.y = sr.getScaledHeight() - 2;
        this.text = text;
        this.stayTime = stayTime;
        this.timer = new TimerUtil();
        this.timer.reset();
        this.stayBar = stayTime;
        this.done = false;
        this.animationUtil.setPosX(!this.done ? (double)(sr.getScaledWidth() - Fonts.novolineFont.getStringWidth(text) - 30) : (double)new ScaledResolution(mc).getScaledWidth());
        this.animationUtil.setPosY(this.y);
    }

    public void renderNotification(float prevY) {
        ScaledResolution sr = RenderUtil.translateGuiScale(new ScaledResolution(mc));
        GL11.glPushMatrix();
        if (this.timer.hasReached(this.stayTime)) {
            this.done = true;
        }
        this.animationUtil.interpolate(!this.done ? (double)(sr.getScaledWidth() - Fonts.novolineFont.getStringWidth(this.text) - 30) : (double)sr.getScaledWidth(), prevY, 35.0f / (float)Minecraft.getDebugFPS());
        float finishedX = (float)this.animationUtil.getPosX();
        float finishedY = (float)this.animationUtil.getPosY();
        this.width = Fonts.novolineFont.getStringWidth(this.text) + 25;
        if (this.done() && !this.done) {
            this.stayBar = this.timer.time();
        }
        MCBlurUtil.drawBLURRRR((int)finishedX, (int)finishedY, (int)this.width, 25, 10.0f);
        RenderUtil.drawRect(finishedX, finishedY, this.width, 25.0, -584834015);
        Fonts.notification.drawString("h", finishedX + 5.0f, finishedY + 8.0f, 0xFFFFFF);
        Fonts.novolineFont.drawString(this.text, finishedX + 22.5f, finishedY + 10.0f, -1);
        if (this.delete()) {
            Moon.INSTANCE.getNotificationManager().getNotifications().remove(this);
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
    }

    public void blur() {
        RenderUtil.drawRect(this.animationUtil.getPosX(), this.animationUtil.getPosY(), this.width, 25.0, -584834015);
    }

    public void bloom() {
        RenderUtil.drawRect(this.animationUtil.getPosX(), this.animationUtil.getPosY(), this.width, 25.0, -584834015);
    }

    public boolean done() {
        return this.animationUtil.getPosX() <= (double)((float)(new ScaledResolution(mc).getScaledWidth() - 2) - this.width);
    }

    public boolean delete() {
        return this.animationUtil.getPosX() >= (double)(new ScaledResolution(mc).getScaledWidth() - 2) && this.done;
    }
}

