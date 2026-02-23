/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl.tabgui.jello.components;

import me.moon.Moon;
import me.moon.module.impl.visuals.HUD;
import net.minecraft.client.gui.ScaledResolution;

public class TabComponent {
    private float posX;
    private float posY;
    private float width;
    private float height;
    private String label;
    public HUD hud;
    private boolean hidden = false;

    public TabComponent(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
    }

    public void init() {
    }

    public void onDraw(ScaledResolution scaledResolution) {
    }

    public void onKeyPress(int key) {
    }

    public void blur() {
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

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

