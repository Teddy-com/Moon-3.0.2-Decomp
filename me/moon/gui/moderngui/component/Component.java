/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.moderngui.component;

import me.moon.utils.game.AnimationUtil;
import net.minecraft.client.Minecraft;

public class Component {
    private final String label;
    private boolean hidden;
    private float posX;
    private float posY;
    private float finishedX;
    private float finishedY;
    private float offsetX;
    private float offsetY;
    private float lastPosX;
    private float lastPosY;
    private float width;
    private float height;
    private float originalPos;
    private boolean extended;
    private boolean dragging;
    private AnimationUtil util = new AnimationUtil(0.0, 0.0);
    private AnimationUtil util1 = new AnimationUtil(0.0, 0.0);

    public Component(String label, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.util1.setPosX(height);
        this.finishedX = posX + offsetX;
        this.util.setPosX(this.finishedX);
        this.finishedY = posY + offsetY;
        this.util.setPosY(this.finishedY);
    }

    public void init() {
    }

    public void moved(float posX, float posY) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFinishedX(this.getPosX() + this.getOffsetX());
        this.setFinishedY(this.getPosY() + this.getOffsetY());
    }

    public void forceAnimation() {
        this.util.setPosX(this.finishedX);
        this.util.setPosY(this.finishedY);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.util1.interpolate(this.height, 0.0, 20.0f / (float)Minecraft.getDebugFPS());
        if (!this.isDragging()) {
            this.util.interpolate(this.finishedX, this.finishedY, 20.0f / (float)Minecraft.getDebugFPS());
        } else {
            this.util.setPosX(this.finishedX);
            this.util.setPosY(this.finishedY);
        }
    }

    public void keyTyped(char character, int keyCode) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public float getFinishedX() {
        return (float)this.util.getPosX();
    }

    public void setFinishedX(float finishedX) {
        this.finishedX = finishedX;
    }

    public float getFinishedY() {
        return (float)this.util.getPosY();
    }

    public void setFinishedY(float finishedY) {
        this.finishedY = finishedY;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public String getLabel() {
        return this.label;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return (float)this.util1.getPosX();
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getLastPosX() {
        return this.lastPosX;
    }

    public void setLastPosX(float lastPosX) {
        this.lastPosX = lastPosX;
    }

    public float getLastPosY() {
        return this.lastPosY;
    }

    public void setLastPosY(float lastPosY) {
        this.lastPosY = lastPosY;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getHidden() {
        return this.hidden;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setOriginalPos(float originalPos) {
        this.originalPos = originalPos;
    }

    public float getOriginalPos() {
        return this.originalPos;
    }

    public void forceX(float x) {
        this.setPosX(this.posX);
        this.setFinishedX(this.getPosX() + this.getOffsetX());
        this.util.setPosX(x);
    }

    public void forceY(float y) {
        this.setPosY(y);
        this.setFinishedY(this.getPosY() + this.getOffsetY());
        this.util.setPosY(y);
    }
}

