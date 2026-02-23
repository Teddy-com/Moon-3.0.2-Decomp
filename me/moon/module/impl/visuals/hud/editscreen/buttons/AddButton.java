/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.editscreen.buttons;

import net.minecraft.client.gui.ScaledResolution;

public abstract class AddButton {
    private float posX;
    private float posY;
    private String displayString;

    public AddButton(String buttonString, float posY) {
        this.displayString = buttonString;
        this.posY = posY;
    }

    public float getPosY() {
        return this.posY;
    }

    public float getPosX() {
        return this.posX;
    }

    public String getDisplayString() {
        return this.displayString;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public abstract void onDraw(int var1, int var2, ScaledResolution var3);

    public abstract void onRun();
}

