/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.settingsscreen;

public class Component {
    private String componentName;
    private float posX;
    private float posY;
    private float height;

    public Component(String componentName, float posX, float posY) {
        this.componentName = componentName;
        this.posX = posX;
        this.posY = posY;
    }

    public void init() {
    }

    public void drawScreen(int mouseX, int mouseY, float pTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mButton) {
    }

    public void keyTyped(char typedChar, int key) {
    }

    public String getComponentName() {
        return this.componentName;
    }

    public float getHeight() {
        return this.height;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

