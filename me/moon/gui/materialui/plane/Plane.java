/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.materialui.plane;

public class Plane {
    private String label;
    private float posX;
    private float posY;
    private float lastPosX;
    private float lastPosY;
    private float width;
    private float height;
    private boolean dragging;

    public Plane(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void initializePlane() {
    }

    public void planeMoved(float movedX, float movedY) {
    }

    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
    }

    public void onMouseReleased(int mouseX, int mouesY, int button) {
    }

    public void onKeyTyped(char character, int keyCode) {
    }

    public void onGuiClosed() {
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

