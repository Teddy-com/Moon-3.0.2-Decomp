/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.nostalgia.tab.component.impl.value;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ColorComponent
extends Component {
    private ColorValue colorValue;
    private ModuleComponent parentComponent;
    private float pos;
    private float saturation;
    private float brightness;

    public ColorComponent(ModuleComponent parentComponent, ColorValue colorValue, float posX, float posY, float width, float height) {
        super(colorValue.getLabel(), posX, posY, width, height);
        this.colorValue = colorValue;
        this.parentComponent = parentComponent;
        float[] hsb = new float[3];
        Color clr = new Color((Integer)colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        for (float i = 0.0f; i < this.getWidth(); i += 1.0f) {
            float posx = this.getPosX() + i;
            int color = Color.getHSBColor(i / this.getWidth(), this.saturation, this.brightness).getRGB();
            RenderUtil.drawRect(posx, this.getPosY(), 1.0, this.getHeight(), i == this.pos ? new Color(-1).getRGB() : color);
            if (this.parentComponent.getSelectedValue() != this.getColorValue() || this.pos != i || !this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || (Integer)this.colorValue.getValue() == color) continue;
            this.colorValue.setValue(color);
        }
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        if (this.parentComponent.getSelectedValue() == this.getColorValue()) {
            RenderUtil.drawBorderedRect(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), 0.5, RenderUtil.getRainbow(4500, -30, 1.0f), new Color(0, 0, 0, 0).getRGB());
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (this.parentComponent.getSelectedValue() != this.getColorValue()) {
            return;
        }
        switch (key) {
            case 28: {
                this.parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            }
            case 205: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic()) break;
                if (this.pos + 1.0f > this.getWidth()) {
                    this.pos = 0.0f;
                    break;
                }
                this.pos += 1.0f;
                break;
            }
            case 203: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic()) break;
                if (this.pos - 1.0f < 0.0f) {
                    this.pos = this.getWidth();
                    break;
                }
                this.pos -= 1.0f;
                break;
            }
            case 200: {
                if (Keyboard.isKeyDown((int)42)) {
                    if (!((double)this.brightness + 0.01 <= 1.0)) break;
                    this.brightness = (float)((double)this.brightness + 0.01);
                    break;
                }
                if (!((double)this.saturation + 0.01 <= 1.0)) break;
                this.saturation = (float)((double)this.saturation + 0.01);
                break;
            }
            case 208: {
                if (Keyboard.isKeyDown((int)42)) {
                    if (!((double)this.brightness - 0.01 >= 0.0)) break;
                    this.brightness = (float)((double)this.brightness - 0.01);
                    break;
                }
                if (!((double)this.saturation - 0.01 >= 0.0)) break;
                this.saturation = (float)((double)this.saturation - 0.01);
                break;
            }
        }
    }

    public ColorValue getColorValue() {
        return this.colorValue;
    }
}

