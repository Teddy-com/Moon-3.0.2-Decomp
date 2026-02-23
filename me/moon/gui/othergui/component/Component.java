/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.othergui.component;

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
    private final float width;
    private final float height;
    private boolean extended;
    private boolean dragging;

    public Component(String label, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.finishedX = posX + offsetX;
        this.finishedY = posY + offsetY;
    }

    public void init() {
    }

    public void moved(float posX, float posY) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFinishedX(this.getPosX() + this.getOffsetX());
        this.setFinishedY(this.getPosY() + this.getOffsetY());
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void keyTyped(char character, int keyCode) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
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
        return this.height;
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
}

