/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.keystrokes;

import java.util.ArrayList;
import me.moon.module.impl.visuals.components.keystrokes.KeystrokesCircle;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Keystroke {
    private float x;
    private float y;
    private float width;
    private float height;
    private float offsetY;
    private int key;
    private boolean shouldRequest = true;
    private boolean lastPressed = false;
    private ArrayList<KeystrokesCircle> keystrokesCircles = new ArrayList();

    public Keystroke(int key, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
        this.offsetY = 0.0f;
    }

    public void blur() {
        RenderUtil.drawRect(this.x, this.y + this.offsetY, this.width, this.height, -1);
    }

    public void bloom() {
        RenderUtil.drawRect(this.x, this.y + this.offsetY, this.width, this.height, -1);
    }

    public void update() {
        for (KeystrokesCircle keystrokesCircle : this.keystrokesCircles) {
            if (this.shouldRequest) {
                if (keystrokesCircle.getRadius() > 47.0f && this.keystrokesCircles.size() > 1) {
                    keystrokesCircle.setPressed(true);
                    this.keystrokesCircles.remove(0);
                    return;
                }
                if (keystrokesCircle.getRadius() > 47.0f && !Keyboard.isKeyDown((int)Math.max(this.key, 0))) {
                    keystrokesCircle.setPressed(false);
                    if (!(keystrokesCircle.getAlpha() < 1.0f)) continue;
                    this.keystrokesCircles.remove(0);
                    return;
                }
                keystrokesCircle.setPressed(true);
                continue;
            }
            boolean shouldRemove = false;
            if (this.key == -99) {
                shouldRemove = !this.lastPressed;
            } else if (this.key == -100) {
                boolean bl = shouldRemove = !this.lastPressed;
            }
            if (keystrokesCircle.getRadius() > 47.0f && this.keystrokesCircles.size() > 1) {
                keystrokesCircle.setPressed(false);
                this.keystrokesCircles.remove(0);
                return;
            }
            if (keystrokesCircle.getRadius() > 47.0f && shouldRemove) {
                keystrokesCircle.setPressed(false);
                if (!(keystrokesCircle.getAlpha() < 1.0f)) continue;
                this.keystrokesCircles.remove(0);
                return;
            }
            keystrokesCircle.setPressed(true);
        }
    }

    public void render() {
        RenderUtil.drawRect(this.x, this.y + this.offsetY, this.width, this.height, 0x67000000);
        String keyToPrint = "";
        switch (this.key) {
            case -99: {
                keyToPrint = "R";
                break;
            }
            case -100: {
                keyToPrint = "L";
                break;
            }
            default: {
                keyToPrint = Keyboard.getKeyName((int)this.key);
            }
        }
        switch (this.key) {
            case -99: {
                this.shouldRequest = false;
                if (this.lastPressed && !Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) {
                    this.lastPressed = false;
                }
                if (!Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) break;
                if (!this.lastPressed) {
                    this.keystrokesCircles.add(new KeystrokesCircle(this.x, this.y + this.offsetY, this.width, this.height));
                }
                this.lastPressed = true;
                break;
            }
            case -100: {
                this.shouldRequest = false;
                if (this.lastPressed && !Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed) {
                    this.lastPressed = false;
                }
                if (!Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed) break;
                if (!this.lastPressed) {
                    this.keystrokesCircles.add(new KeystrokesCircle(this.x, this.y + this.offsetY, this.width, this.height));
                }
                this.lastPressed = true;
                break;
            }
            default: {
                this.shouldRequest = true;
            }
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        float yToSiccor = this.y + this.offsetY;
        if (!Minecraft.getMinecraft().isFullScreen()) {
            yToSiccor += 0.5f;
        }
        RenderUtil.prepareScissorBox(sr, this.x, yToSiccor, this.width, this.height);
        this.keystrokesCircles.forEach(KeystrokesCircle::render);
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        Fonts.jelloLight.drawCenteredString(keyToPrint, this.x + this.width / 2.0f, this.y + this.offsetY + this.height / 2.0f - (float)Fonts.jelloFont.getHeight() / 2.0f, -572662307);
    }

    public void onKey() {
        this.keystrokesCircles.add(new KeystrokesCircle(this.x, this.y + this.offsetY, this.width, this.height));
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getY() {
        return this.y;
    }

    public float getX() {
        return this.x;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetY() {
        return this.offsetY;
    }
}

