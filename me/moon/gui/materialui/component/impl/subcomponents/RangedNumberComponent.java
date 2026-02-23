/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.materialui.component.impl.subcomponents;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.moon.gui.materialui.component.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.util.MathHelper;

public class RangedNumberComponent
extends Component {
    private RangedValue rangedValue;
    private boolean leftDown;
    private boolean rightDown;

    public RangedNumberComponent(RangedValue rangedValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(rangedValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.rangedValue = rangedValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        float startX = this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + 4.0f;
        float sliderwidth = this.getWidth() - (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) - 12.0f;
        float leftX = MathHelper.floor_double((((Number)this.rangedValue.getLeftValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * sliderwidth);
        float rightX = MathHelper.floor_double((((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * sliderwidth);
        Fonts.sectioNormal.drawStringWithShadow(this.getLabel(), this.getPosX(), this.getPosY() + 3.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawRoundedRect(this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + 4.0f, this.getPosY() + 5.0f, this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 12.0f, 2.0, 0.0, new Color(25, 25, 25, 255).getRGB());
        RenderUtil.drawRoundedRect(startX + leftX, this.getPosY() + 5.0f, rightX - leftX, 2.0, 0.0, new Color(-15305263).getRGB());
        RenderUtil.drawCircle(this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + leftX + 2.0f, this.getPosY() + 4.0f, 4.0f, new Color(-15305263).getRGB());
        RenderUtil.drawCircle(this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + rightX + 2.0f, this.getPosY() + 4.0f, 4.0f, new Color(-15305263).getRGB());
        Fonts.sectioNumberSlider.drawStringWithShadow(String.valueOf(this.rangedValue.getLeftValue()), this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + leftX + 4.0f - (float)Fonts.sectioNumberSlider.getStringWidth(String.valueOf(this.rangedValue.getLeftValue())) / 2.0f, this.getPosY() + 12.0f, new Color(229, 229, 223, 255).getRGB());
        Fonts.sectioNumberSlider.drawStringWithShadow(String.valueOf(this.rangedValue.getRightValue()), this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + rightX + 4.0f - (float)Fonts.sectioNumberSlider.getStringWidth(String.valueOf(this.rangedValue.getRightValue())) / 2.0f, this.getPosY() + 12.0f, new Color(229, 229, 223, 255).getRGB());
        if (this.leftDown) {
            if (this.rangedValue.getLeftValue() instanceof Double) {
                this.rangedValue.setLeftValue(this.round((double)((float)mouseX - startX) * (((Number)this.rangedValue.getMaximum()).doubleValue() - ((Number)this.rangedValue.getMinimum()).doubleValue()) / (double)sliderwidth + ((Number)this.rangedValue.getMinimum()).doubleValue()));
            }
            if (this.rangedValue.getLeftValue() instanceof Float) {
                this.rangedValue.setLeftValue(Float.valueOf((float)this.round(((float)mouseX - startX) * (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / sliderwidth + ((Number)this.rangedValue.getMinimum()).floatValue())));
            }
            if (this.rangedValue.getLeftValue() instanceof Long) {
                this.rangedValue.setLeftValue((long)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).longValue() - ((Number)this.rangedValue.getMinimum()).longValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).longValue()));
            }
            if (this.rangedValue.getLeftValue() instanceof Integer) {
                this.rangedValue.setLeftValue((int)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).intValue() - ((Number)this.rangedValue.getMinimum()).intValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).intValue()));
            }
            if (this.rangedValue.getLeftValue() instanceof Short) {
                this.rangedValue.setLeftValue((short)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).shortValue() - ((Number)this.rangedValue.getMinimum()).shortValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).shortValue()));
            }
            if (this.rangedValue.getLeftValue() instanceof Byte) {
                this.rangedValue.setLeftValue((byte)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).byteValue() - ((Number)this.rangedValue.getMinimum()).byteValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).byteValue()));
            }
        }
        if (this.rightDown) {
            if (this.rangedValue.getRightValue() instanceof Double) {
                this.rangedValue.setRightValue(this.round((double)((float)mouseX - startX) * (((Number)this.rangedValue.getMaximum()).doubleValue() - ((Number)this.rangedValue.getMinimum()).doubleValue()) / (double)sliderwidth + ((Number)this.rangedValue.getMinimum()).doubleValue()));
            }
            if (this.rangedValue.getRightValue() instanceof Float) {
                this.rangedValue.setRightValue(Float.valueOf((float)this.round(((float)mouseX - startX) * (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / sliderwidth + ((Number)this.rangedValue.getMinimum()).floatValue())));
            }
            if (this.rangedValue.getRightValue() instanceof Long) {
                this.rangedValue.setRightValue((long)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).longValue() - ((Number)this.rangedValue.getMinimum()).longValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).longValue()));
            }
            if (this.rangedValue.getRightValue() instanceof Integer) {
                this.rangedValue.setRightValue((int)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).intValue() - ((Number)this.rangedValue.getMinimum()).intValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).intValue()));
            }
            if (this.rangedValue.getRightValue() instanceof Short) {
                this.rangedValue.setRightValue((short)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).shortValue() - ((Number)this.rangedValue.getMinimum()).shortValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).shortValue()));
            }
            if (this.rangedValue.getRightValue() instanceof Byte) {
                this.rangedValue.setRightValue((byte)this.round(((float)mouseX - startX) * (float)(((Number)this.rangedValue.getMaximum()).byteValue() - ((Number)this.rangedValue.getMinimum()).byteValue()) / sliderwidth + (float)((Number)this.rangedValue.getMinimum()).byteValue()));
            }
        }
        if (this.rangedValue.getInc() instanceof Double) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).doubleValue() > ((Number)this.rangedValue.getRightValue()).doubleValue() - ((Number)this.rangedValue.getInc()).doubleValue()) {
                this.rangedValue.setLeftValue(((Number)this.rangedValue.getRightValue()).doubleValue() - ((Number)this.rangedValue.getInc()).doubleValue());
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).doubleValue() < ((Number)this.rangedValue.getLeftValue()).doubleValue() + ((Number)this.rangedValue.getInc()).doubleValue()) {
                this.rangedValue.setRightValue(((Number)this.rangedValue.getLeftValue()).doubleValue() + ((Number)this.rangedValue.getInc()).doubleValue());
            }
        }
        if (this.rangedValue.getInc() instanceof Float) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).floatValue() > ((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getInc()).floatValue()) {
                this.rangedValue.setLeftValue(Float.valueOf(((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getInc()).floatValue()));
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).floatValue() < ((Number)this.rangedValue.getLeftValue()).floatValue() + ((Number)this.rangedValue.getInc()).floatValue()) {
                this.rangedValue.setRightValue(Float.valueOf(((Number)this.rangedValue.getLeftValue()).floatValue() + ((Number)this.rangedValue.getInc()).floatValue()));
            }
        }
        if (this.rangedValue.getInc() instanceof Long) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).longValue() > ((Number)this.rangedValue.getRightValue()).longValue() - ((Number)this.rangedValue.getInc()).longValue()) {
                this.rangedValue.setLeftValue(((Number)this.rangedValue.getRightValue()).longValue() - ((Number)this.rangedValue.getInc()).longValue());
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).longValue() < ((Number)this.rangedValue.getLeftValue()).longValue() + ((Number)this.rangedValue.getValue()).longValue()) {
                this.rangedValue.setRightValue(((Number)this.rangedValue.getLeftValue()).longValue() + ((Number)this.rangedValue.getInc()).longValue());
            }
        }
        if (this.rangedValue.getInc() instanceof Integer) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).intValue() > ((Number)this.rangedValue.getRightValue()).intValue() - ((Number)this.rangedValue.getInc()).intValue()) {
                this.rangedValue.setLeftValue(((Number)this.rangedValue.getRightValue()).intValue() - ((Number)this.rangedValue.getInc()).intValue());
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).intValue() < ((Number)this.rangedValue.getLeftValue()).intValue() + ((Number)this.rangedValue.getInc()).intValue()) {
                this.rangedValue.setRightValue(((Number)this.rangedValue.getLeftValue()).intValue() + ((Number)this.rangedValue.getInc()).intValue());
            }
        }
        if (this.rangedValue.getInc() instanceof Short) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).shortValue() > ((Number)this.rangedValue.getRightValue()).shortValue() - ((Number)this.rangedValue.getInc()).shortValue()) {
                this.rangedValue.setLeftValue(((Number)this.rangedValue.getRightValue()).shortValue() - ((Number)this.rangedValue.getInc()).shortValue());
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).shortValue() < ((Number)this.rangedValue.getLeftValue()).shortValue() + ((Number)this.rangedValue.getInc()).shortValue()) {
                this.rangedValue.setRightValue((short)(((Number)this.rangedValue.getLeftValue()).shortValue() + ((Number)this.rangedValue.getInc()).shortValue()));
            }
        }
        if (this.rangedValue.getInc() instanceof Byte) {
            if (this.leftDown && ((Number)this.rangedValue.getLeftValue()).byteValue() > ((Number)this.rangedValue.getRightValue()).byteValue() - ((Number)this.rangedValue.getInc()).byteValue()) {
                this.rangedValue.setLeftValue(((Number)this.rangedValue.getRightValue()).byteValue() - ((Number)this.rangedValue.getInc()).byteValue());
            }
            if (this.rightDown && ((Number)this.rangedValue.getRightValue()).byteValue() < ((Number)this.rangedValue.getLeftValue()).byteValue() + ((Number)this.rangedValue.getInc()).byteValue()) {
                this.rangedValue.setRightValue(((Number)this.rangedValue.getLeftValue()).byteValue() + ((Number)this.rangedValue.getInc()).byteValue());
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            if (this.hoveredLeft(mouseX, mouseY)) {
                this.leftDown = true;
            } else if (this.hoveredRight(mouseX, mouseY)) {
                this.rightDown = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            this.rightDown = false;
            this.leftDown = false;
        }
    }

    private boolean hoveredLeft(int mouseX, int mouseY) {
        float leftX = MathHelper.floor_double((((Number)this.rangedValue.getLeftValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + leftX, this.getPosY() + 4.0f, 5.0, 3.0);
    }

    private boolean hoveredRight(int mouseX, int mouseY) {
        float rightX = MathHelper.floor_double((((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 2.0f + rightX, this.getPosY() + 4.0f, 5.0, 3.0);
    }

    private double round(double val) {
        double v = (double)Math.round(val / ((Number)this.rangedValue.getInc()).doubleValue()) * ((Number)this.rangedValue.getInc()).doubleValue();
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public RangedValue getRangedValue() {
        return this.rangedValue;
    }
}

