/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.materialui.component;

public class Component {
    private String label;
    private float posX;
    private float posY;
    private float offsetX;
    private float offsetY;
    private float originalOffsetX;
    private float originalOffsetY;
    private float width;
    private float height;
    private boolean hidden;

    public Component(String label, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        this.label = label;
        this.posX = posX + offsetX;
        this.posY = posY + offsetY;
        this.offsetX = this.originalOffsetX = offsetX;
        this.offsetY = this.originalOffsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public void initializeComponent() {
    }

    public void componentMoved(float movedX, float movedY) {
        this.posX = movedX + this.offsetX;
        this.posY = movedY + this.offsetY;
    }

    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
    }

    public void onMouseReleased(int mouseX, int mouseY, int button) {
    }

    public void onKeyTyped(char character, int keyCode) {
    }

    public void onGuiClosed() {
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

    public float getOriginalOffsetX() {
        return this.originalOffsetX;
    }

    public float getOriginalOffsetY() {
        return this.originalOffsetY;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

