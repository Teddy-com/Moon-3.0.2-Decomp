/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.module.impl.visuals.hud.settingsscreen.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.settingsscreen.Component;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ColorComponent
extends Component {
    private ColorValue colorValue;
    private boolean pressedhue;
    private float pos;
    private float hue;
    private float saturation;
    private float brightness;
    private boolean hovered;

    public ColorComponent(ColorValue colorValue, float posX, float posY) {
        super(colorValue.getLabel(), posX, posY);
        this.colorValue = colorValue;
        float[] hsb = new float[3];
        Color clr = new Color((Integer)colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Keyboard.enableRepeatEvents((boolean)true);
        this.hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + 1.0f, 90.0, 5.0);
        float i = 0.0f;
        while (i + 1.0f < 100.0f) {
            float posx = this.getPosX() + i;
            int color = Color.getHSBColor(i / 100.0f, this.saturation, this.brightness).getRGB();
            RenderUtil.drawRect(posx, this.getPosY() + 1.0f, 1.0, 5.0, color);
            if ((float)mouseX == posx && this.pressedhue) {
                this.colorValue.setValue(color);
                this.hue = i / 100.0f;
            }
            if (0.001 * Math.floor((double)(i / 100.0f) * 1000.0) == 0.001 * Math.floor((double)this.hue * 1000.0)) {
                this.pos = i;
            }
            i += 0.5f;
        }
        RenderUtil.drawRect(this.getPosX() + this.pos, this.getPosY() + 1.0f, 2.0, 5.0, -14606047);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getComponentName(), this.getPosX() + 105.0f, this.getPosY(), -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (!this.hovered) {
            return;
        }
        switch (key) {
            case 200: {
                if (Keyboard.isKeyDown((int)42)) {
                    if ((double)this.brightness + 0.01 <= 1.0) {
                        this.brightness = (float)((double)this.brightness + 0.01);
                    }
                } else if ((double)this.saturation + 0.01 <= 1.0) {
                    this.saturation = (float)((double)this.saturation + 0.01);
                }
                this.colorValue.setValue(Color.HSBtoRGB(this.hue, this.saturation, this.brightness));
                break;
            }
            case 208: {
                if (Keyboard.isKeyDown((int)42)) {
                    if ((double)this.brightness - 0.01 >= 0.0) {
                        this.brightness = (float)((double)this.brightness - 0.01);
                    }
                } else if ((double)this.saturation - 0.01 >= 0.0) {
                    this.saturation = (float)((double)this.saturation - 0.01);
                }
                this.colorValue.setValue(Color.HSBtoRGB(this.hue, this.saturation, this.brightness));
                break;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + 1.0f, 90.0, 5.0);
        if (mouseButton == 0 && hovered) {
            this.pressedhue = true;
            Moon.INSTANCE.getComponentManager().saveComps();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && this.pressedhue) {
            this.pressedhue = false;
        }
    }
}

