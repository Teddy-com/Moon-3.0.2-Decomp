/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.discordclickgui.frame;

import java.util.ArrayList;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.utils.game.MouseUtil;
import net.minecraft.client.gui.ScaledResolution;

public class Frame {
    private float posX;
    private float posY;
    private float lastPosX;
    private float lastPosY;
    private float width;
    private float height;
    private String label;
    private boolean dragging;
    private ArrayList<Component> components = new ArrayList();

    public Frame(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void init() {
        this.components.forEach(Component::init);
    }

    public void updatePosition() {
        this.components.forEach(component -> component.updatePosition(this.getPosX(), this.getPosY()));
    }

    public void drawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        if (this.isDragging()) {
            this.setPosX((float)mouseX + this.getLastPosX());
            this.setPosY((float)mouseY + this.getLastPosY());
            this.updatePosition();
        }
        if (this.getPosX() < 0.0f) {
            this.setPosX(0.0f);
            this.updatePosition();
        }
        if (this.getPosX() + this.getWidth() > (float)scaledResolution.getScaledWidth()) {
            this.setPosX((float)scaledResolution.getScaledWidth() - this.getWidth());
            this.updatePosition();
        }
        if (this.getPosY() < 0.0f) {
            this.setPosY(0.0f);
            this.updatePosition();
        }
        if (this.getPosY() + this.getHeight() > (float)scaledResolution.getScaledHeight()) {
            this.setPosY((float)scaledResolution.getScaledHeight() - this.getHeight());
            this.updatePosition();
        }
    }

    public void onBloom() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), 12.0);
        switch (mouseButton) {
            case 0: {
                if (!hovered) break;
                this.setDragging(true);
                this.setLastPosX(this.getPosX() - (float)mouseX);
                this.setLastPosY(this.getPosY() - (float)mouseY);
                break;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        switch (mouseButton) {
            case 0: {
                if (!this.isDragging()) break;
                this.setDragging(false);
                break;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public ArrayList<Component> getComponents() {
        return this.components;
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

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}

