/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.discordclickgui.component;

import net.minecraft.client.gui.ScaledResolution;

public class Component {
    private float posX;
    private float posY;
    private float offsetX;
    private float offsetY;
    private float finishedX;
    private float finishedY;
    private float baseOffsetY;
    private float height;
    private String label;
    private boolean hidden;

    public Component(String label, float posX, float posY, float offsetX, float offsetY, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.height = height;
        this.baseOffsetY = offsetY;
        this.finishedX = posX + offsetX;
        this.finishedY = posY + offsetY;
    }

    public void init() {
    }

    public void updatePosition(float posX, float posY) {
        this.setFinishedX(posX + this.offsetX);
        this.setFinishedY(posY + this.offsetY);
    }

    public void onDrawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
    }

    public void onMouseReleased(int mouseX, int mouseY, int button) {
    }

    public void onKeyTyped(char keyChar, int key) {
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

    public float getFinishedX() {
        return this.finishedX;
    }

    public void setFinishedX(float finishedX) {
        this.finishedX = finishedX;
    }

    public float getFinishedY() {
        return this.finishedY;
    }

    public void setFinishedY(float finishedY) {
        this.finishedY = finishedY;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getBaseOffsetY() {
        return this.baseOffsetY;
    }

    public void setBaseOffsetY(float baseOffsetY) {
        this.baseOffsetY = baseOffsetY;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

