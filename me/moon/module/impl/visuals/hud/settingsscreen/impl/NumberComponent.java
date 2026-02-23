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
import me.moon.utils.MathUtils;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class NumberComponent
extends Component {
    private NumberValue numberValue;
    private boolean dragging;
    private TimerUtil timer = new TimerUtil();

    public NumberComponent(NumberValue numberValue, float posX, float posY) {
        super(numberValue.getLabel(), posX, posY);
        this.numberValue = numberValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float sliderwidth = 74.0f;
        float length = MathHelper.floor_double((((Number)this.numberValue.getValue()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) / (((Number)this.numberValue.getMaximum()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) * sliderwidth);
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 80.0, 10.0);
        RenderUtil.drawBorderedRect(this.getPosX(), this.getPosY(), 80.0, 10.0, 0.5, new Color(150, 150, 150, 255).getRGB(), isHovered ? new Color(-11184811).getRGB() : new Color(-12303292).getRGB());
        RenderUtil.drawRect(this.getPosX() + length + 1.0f, this.getPosY() + 1.0f, 4.0, 8.0, new Color(150, 150, 150, 255).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(this.numberValue.getLabel() + ": " + this.numberValue.getValue().toString(), (int)this.getPosX() + 40 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.numberValue.getLabel() + ": " + this.numberValue.getValue().toString()) / 2, (float)((int)this.getPosY()) + 1.5f, -1);
        if (this.dragging) {
            if (this.numberValue.getValue() instanceof Double) {
                this.numberValue.setValue(MathUtils.round((double)((float)mouseX - this.getPosX()) * (((Number)this.numberValue.getMaximum()).doubleValue() - ((Number)this.numberValue.getMinimum()).doubleValue()) / (double)sliderwidth + ((Number)this.numberValue.getMinimum()).doubleValue(), 2));
            }
            if (this.numberValue.getValue() instanceof Float) {
                this.numberValue.setValue(Float.valueOf((float)MathUtils.round(((float)mouseX - this.getPosX()) * (((Number)this.numberValue.getMaximum()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) / sliderwidth + ((Number)this.numberValue.getMinimum()).floatValue(), 2)));
            }
            if (this.numberValue.getValue() instanceof Long) {
                this.numberValue.setValue((long)MathUtils.round(((float)mouseX - this.getPosX()) * (float)(((Number)this.numberValue.getMaximum()).longValue() - ((Number)this.numberValue.getMinimum()).longValue()) / sliderwidth + (float)((Number)this.numberValue.getMinimum()).longValue(), 2));
            }
            if (this.numberValue.getValue() instanceof Integer) {
                this.numberValue.setValue((int)MathUtils.round(((float)mouseX - this.getPosX()) * (float)(((Number)this.numberValue.getMaximum()).intValue() - ((Number)this.numberValue.getMinimum()).intValue()) / sliderwidth + (float)((Number)this.numberValue.getMinimum()).intValue(), 2));
            }
            if (this.numberValue.getValue() instanceof Short) {
                this.numberValue.setValue((short)MathUtils.round(((float)mouseX - this.getPosX()) * (float)(((Number)this.numberValue.getMaximum()).shortValue() - ((Number)this.numberValue.getMinimum()).shortValue()) / sliderwidth + (float)((Number)this.numberValue.getMinimum()).shortValue(), 2));
            }
            if (this.numberValue.getValue() instanceof Byte) {
                this.numberValue.setValue((byte)MathUtils.round(((float)mouseX - this.getPosX()) * (float)(((Number)this.numberValue.getMaximum()).byteValue() - ((Number)this.numberValue.getMinimum()).byteValue()) / sliderwidth + (float)((Number)this.numberValue.getMinimum()).byteValue(), 2));
            }
        }
        if (isHovered) {
            if (Keyboard.isKeyDown((int)203) && this.timer.sleep(100L)) {
                if (this.numberValue.getValue() instanceof Double) {
                    this.numberValue.setValue(MathUtils.round(((Number)this.numberValue.getValue()).doubleValue() - ((Number)this.numberValue.getInc()).doubleValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Float) {
                    this.numberValue.setValue(Float.valueOf((float)MathUtils.round(((Number)this.numberValue.getValue()).floatValue() - ((Number)this.numberValue.getInc()).floatValue(), 2)));
                }
                if (this.numberValue.getValue() instanceof Long) {
                    this.numberValue.setValue((long)MathUtils.round(((Number)this.numberValue.getValue()).longValue() - ((Number)this.numberValue.getInc()).longValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Integer) {
                    this.numberValue.setValue((int)MathUtils.round(((Number)this.numberValue.getValue()).intValue() - ((Number)this.numberValue.getInc()).intValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Short) {
                    this.numberValue.setValue((short)MathUtils.round(((Number)this.numberValue.getValue()).shortValue() - ((Number)this.numberValue.getInc()).shortValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Byte) {
                    this.numberValue.setValue((byte)MathUtils.round(((Number)this.numberValue.getValue()).byteValue() - ((Number)this.numberValue.getInc()).byteValue(), 2));
                }
            }
            if (Keyboard.isKeyDown((int)205) && this.timer.sleep(100L)) {
                if (this.numberValue.getValue() instanceof Double) {
                    this.numberValue.setValue(MathUtils.round(((Number)this.numberValue.getValue()).doubleValue() + ((Number)this.numberValue.getInc()).doubleValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Float) {
                    this.numberValue.setValue(Float.valueOf((float)MathUtils.round(((Number)this.numberValue.getValue()).floatValue() + ((Number)this.numberValue.getInc()).floatValue(), 2)));
                }
                if (this.numberValue.getValue() instanceof Long) {
                    this.numberValue.setValue((long)MathUtils.round(((Number)this.numberValue.getValue()).longValue() + ((Number)this.numberValue.getInc()).longValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Integer) {
                    this.numberValue.setValue((int)MathUtils.round(((Number)this.numberValue.getValue()).intValue() + ((Number)this.numberValue.getInc()).intValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Short) {
                    this.numberValue.setValue((short)MathUtils.round(((Number)this.numberValue.getValue()).shortValue() + ((Number)this.numberValue.getInc()).shortValue(), 2));
                }
                if (this.numberValue.getValue() instanceof Byte) {
                    this.numberValue.setValue((byte)MathUtils.round(((Number)this.numberValue.getValue()).byteValue() + ((Number)this.numberValue.getInc()).byteValue(), 2));
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 80.0, 10.0);
        if (isHovered) {
            this.dragging = true;
            Moon.INSTANCE.getComponentManager().saveComps();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.dragging = false;
        }
    }
}

