/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.gui.discordclickgui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.ModuleComponent;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class RangedNumberComponent
extends Component {
    private RangedValue value;
    private ModuleComponent parent;
    private boolean leftDown;
    private boolean rightDown;

    public RangedNumberComponent(ModuleComponent parent, RangedValue value, float posX, float posY, float offsetX, float offsetY, float height) {
        super(value.getLabel(), posX, posY, offsetX, offsetY, height);
        this.value = value;
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.onDrawScreen(mouseX, mouseY, scaledResolution);
        Fonts.clickfont14.drawString(ChatFormatting.GRAY + this.getLabel(), this.getFinishedX(), this.getFinishedY() - 0.5f, -11645101);
        float startX = this.getFinishedX();
        float sliderwidth = 137.0f;
        float leftX = MathHelper.floor_double((((Number)this.value.getLeftValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        float rightX = MathHelper.floor_double((((Number)this.value.getRightValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        RenderUtil.drawRoundedRect(this.getFinishedX(), this.getFinishedY() + 10.0f, 137.0, 4.0, 2.0, -11578276);
        RenderUtil.drawRoundedRect(startX + leftX, this.getFinishedY() + 10.0f, rightX - leftX, 4.0, 2.0, -9270822);
        RenderUtil.drawRoundedRect(this.getFinishedX() + leftX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0, 2.0, -1);
        RenderUtil.drawRoundedRect(this.getFinishedX() + rightX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0, 2.0, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + leftX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0) || this.leftDown) {
            RenderUtil.drawRoundedRect(this.getFinishedX() + leftX - (float)(Fonts.clickfont14.getStringWidth(this.value.getLeftValue().toString()) / 2) - 2.0f, this.getFinishedY() - 3.0f, Fonts.clickfont14.getStringWidth(this.value.getLeftValue().toString()) + 4, Fonts.clickfont14.getHeight() + 4, 2.0, -9275779);
            Fonts.clickfont14.drawString(this.value.getLeftValue().toString(), this.getFinishedX() + leftX - (float)(Fonts.clickfont14.getStringWidth(this.value.getLeftValue().toString()) / 2), this.getFinishedY(), -1);
        }
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + rightX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0) || this.rightDown) {
            RenderUtil.drawRoundedRect(this.getFinishedX() + rightX - (float)(Fonts.clickfont14.getStringWidth(this.value.getRightValue().toString()) / 2) - 2.0f, this.getFinishedY() - 3.0f, Fonts.clickfont14.getStringWidth(this.value.getRightValue().toString()) + 4, Fonts.clickfont14.getHeight() + 4, 2.0, -9275779);
            Fonts.clickfont14.drawString(this.value.getRightValue().toString(), this.getFinishedX() + rightX - (float)(Fonts.clickfont14.getStringWidth(this.value.getRightValue().toString()) / 2), this.getFinishedY(), -1);
        }
        if (this.leftDown) {
            if (this.value.getLeftValue() instanceof Double) {
                this.value.setLeftValue(this.round((double)((float)mouseX - startX) * (((Number)this.value.getMaximum()).doubleValue() - ((Number)this.value.getMinimum()).doubleValue()) / 137.0 + ((Number)this.value.getMinimum()).doubleValue(), 2));
            }
            if (this.value.getLeftValue() instanceof Float) {
                this.value.setLeftValue(Float.valueOf((float)this.round(((float)mouseX - startX) * (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / 137.0f + ((Number)this.value.getMinimum()).floatValue(), 2)));
            }
            if (this.value.getLeftValue() instanceof Long) {
                this.value.setLeftValue((long)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).longValue() - ((Number)this.value.getMinimum()).longValue()) / 137.0f + (float)((Number)this.value.getMinimum()).longValue(), 2));
            }
            if (this.value.getLeftValue() instanceof Integer) {
                this.value.setLeftValue((int)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).intValue() - ((Number)this.value.getMinimum()).intValue()) / 137.0f + (float)((Number)this.value.getMinimum()).intValue(), 2));
            }
            if (this.value.getLeftValue() instanceof Short) {
                this.value.setLeftValue((short)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).shortValue() - ((Number)this.value.getMinimum()).shortValue()) / 137.0f + (float)((Number)this.value.getMinimum()).shortValue(), 2));
            }
            if (this.value.getLeftValue() instanceof Byte) {
                this.value.setLeftValue((byte)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).byteValue() - ((Number)this.value.getMinimum()).byteValue()) / 137.0f + (float)((Number)this.value.getMinimum()).byteValue(), 2));
            }
        }
        if (this.rightDown) {
            if (this.value.getRightValue() instanceof Double) {
                this.value.setRightValue(this.round((double)((float)mouseX - startX) * (((Number)this.value.getMaximum()).doubleValue() - ((Number)this.value.getMinimum()).doubleValue()) / 137.0 + ((Number)this.value.getMinimum()).doubleValue(), 2));
            }
            if (this.value.getRightValue() instanceof Float) {
                this.value.setRightValue(Float.valueOf((float)this.round(((float)mouseX - startX) * (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / 137.0f + ((Number)this.value.getMinimum()).floatValue(), 2)));
            }
            if (this.value.getRightValue() instanceof Long) {
                this.value.setRightValue((long)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).longValue() - ((Number)this.value.getMinimum()).longValue()) / 137.0f + (float)((Number)this.value.getMinimum()).longValue(), 2));
            }
            if (this.value.getRightValue() instanceof Integer) {
                this.value.setRightValue((int)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).intValue() - ((Number)this.value.getMinimum()).intValue()) / 137.0f + (float)((Number)this.value.getMinimum()).intValue(), 2));
            }
            if (this.value.getRightValue() instanceof Short) {
                this.value.setRightValue((short)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).shortValue() - ((Number)this.value.getMinimum()).shortValue()) / 137.0f + (float)((Number)this.value.getMinimum()).shortValue(), 2));
            }
            if (this.value.getRightValue() instanceof Byte) {
                this.value.setRightValue((byte)this.round(((float)mouseX - startX) * (float)(((Number)this.value.getMaximum()).byteValue() - ((Number)this.value.getMinimum()).byteValue()) / 137.0f + (float)((Number)this.value.getMinimum()).byteValue(), 2));
            }
        }
        if (this.value.getInc() instanceof Double) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).doubleValue() > ((Number)this.value.getRightValue()).doubleValue() - ((Number)this.value.getInc()).doubleValue()) {
                this.value.setLeftValue(((Number)this.value.getRightValue()).doubleValue() - ((Number)this.value.getInc()).doubleValue());
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).doubleValue() < ((Number)this.value.getLeftValue()).doubleValue() + ((Number)this.value.getInc()).doubleValue()) {
                this.value.setRightValue(((Number)this.value.getLeftValue()).doubleValue() + ((Number)this.value.getInc()).doubleValue());
            }
        }
        if (this.value.getInc() instanceof Float) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).floatValue() > ((Number)this.value.getRightValue()).floatValue() - ((Number)this.value.getInc()).floatValue()) {
                this.value.setLeftValue(Float.valueOf(((Number)this.value.getRightValue()).floatValue() - ((Number)this.value.getInc()).floatValue()));
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).floatValue() < ((Number)this.value.getLeftValue()).floatValue() + ((Number)this.value.getInc()).floatValue()) {
                this.value.setRightValue(Float.valueOf(((Number)this.value.getLeftValue()).floatValue() + ((Number)this.value.getInc()).floatValue()));
            }
        }
        if (this.value.getInc() instanceof Long) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).longValue() > ((Number)this.value.getRightValue()).longValue() - ((Number)this.value.getInc()).longValue()) {
                this.value.setLeftValue(((Number)this.value.getRightValue()).longValue() - ((Number)this.value.getInc()).longValue());
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).longValue() < ((Number)this.value.getLeftValue()).longValue() + ((Number)this.value.getValue()).longValue()) {
                this.value.setRightValue(((Number)this.value.getLeftValue()).longValue() + ((Number)this.value.getInc()).longValue());
            }
        }
        if (this.value.getInc() instanceof Integer) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).intValue() > ((Number)this.value.getRightValue()).intValue() - ((Number)this.value.getInc()).intValue()) {
                this.value.setLeftValue(((Number)this.value.getRightValue()).intValue() - ((Number)this.value.getInc()).intValue());
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).intValue() < ((Number)this.value.getLeftValue()).intValue() + ((Number)this.value.getInc()).intValue()) {
                this.value.setRightValue(((Number)this.value.getLeftValue()).intValue() + ((Number)this.value.getInc()).intValue());
            }
        }
        if (this.value.getInc() instanceof Short) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).shortValue() > ((Number)this.value.getRightValue()).shortValue() - ((Number)this.value.getInc()).shortValue()) {
                this.value.setLeftValue(((Number)this.value.getRightValue()).shortValue() - ((Number)this.value.getInc()).shortValue());
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).shortValue() < ((Number)this.value.getLeftValue()).shortValue() + ((Number)this.value.getInc()).shortValue()) {
                this.value.setRightValue((short)(((Number)this.value.getLeftValue()).shortValue() + ((Number)this.value.getInc()).shortValue()));
            }
        }
        if (this.value.getInc() instanceof Byte) {
            if (this.leftDown && ((Number)this.value.getLeftValue()).byteValue() > ((Number)this.value.getRightValue()).byteValue() - ((Number)this.value.getInc()).byteValue()) {
                this.value.setLeftValue(((Number)this.value.getRightValue()).byteValue() - ((Number)this.value.getInc()).byteValue());
            }
            if (this.rightDown && ((Number)this.value.getRightValue()).byteValue() < ((Number)this.value.getLeftValue()).byteValue() + ((Number)this.value.getInc()).byteValue()) {
                this.value.setRightValue(((Number)this.value.getLeftValue()).byteValue() + ((Number)this.value.getInc()).byteValue());
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (this.hoveredLeft(mouseX, mouseY)) {
                this.leftDown = true;
            } else if (this.hoveredRight(mouseX, mouseY)) {
                this.rightDown = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            this.rightDown = false;
            this.leftDown = false;
        }
    }

    private boolean hoveredLeft(int mouseX, int mouseY) {
        float leftX = MathHelper.floor_double((((Number)this.value.getLeftValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + leftX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0);
    }

    private boolean hoveredRight(int mouseX, int mouseY) {
        float rightX = MathHelper.floor_double((((Number)this.value.getRightValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + rightX - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0);
    }

    private double round(double val, int places) {
        double v = (double)Math.round(val / ((Number)this.value.getInc()).doubleValue()) * ((Number)this.value.getInc()).doubleValue();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public RangedValue getValue() {
        return this.value;
    }
}

