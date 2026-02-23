/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.moderngui.component.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.moon.Moon;
import me.moon.gui.moderngui.component.Component;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RangedNumberComponent
extends Component {
    private final RangedValue rangedValue;
    private boolean leftDown;
    private boolean rightDown;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public RangedNumberComponent(RangedValue rangedValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(rangedValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.rangedValue = rangedValue;
    }

    @Override
    public void init() {
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight(), clickGUI.getColor());
        Fonts.moonSmaller.drawString(this.getLabel() + " - " + this.rangedValue.getLeftValue().toString() + " / " + this.rangedValue.getRightValue().toString() + "", this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.moonSmaller.getStringHeight(this.getLabel()) / 2.0f - 2.0f, -1);
        float startX = this.getFinishedX() + 5.0f;
        float sliderwidth = this.getWidth() - 10.0f;
        float leftX = MathHelper.floor_double((((Number)this.rangedValue.getLeftValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * sliderwidth);
        float rightX = MathHelper.floor_double((((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * sliderwidth);
        if (this.animationUtil.getPosX() == 0.0 || this.animationUtil.getPosY() == 0.0) {
            this.animationUtil.setPosX(leftX);
            this.animationUtil.setPosY(rightX);
        } else {
            this.animationUtil.interpolate(leftX, rightX, 20.0f / (float)Minecraft.getDebugFPS());
        }
        RenderUtil.drawRoundedRect(this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() - 5.0f, this.getWidth() - 10.0f, 2.0, 1.0, new Color(25, 25, 25).getRGB());
        RenderUtil.drawRect((double)startX + this.animationUtil.getPosX(), this.getFinishedY() + this.getHeight() - 5.0f, this.animationUtil.getPosY() - this.animationUtil.getPosX(), 2.0, (Integer)clickGUI.color.getValue());
        RenderUtil.drawCircle((float)((double)startX + this.animationUtil.getPosX() - 5.0), this.getFinishedY() + this.getHeight() - 6.5f, 5.0f, (Integer)clickGUI.color.getValue());
        RenderUtil.drawCircle((float)((double)startX + this.animationUtil.getPosY()), this.getFinishedY() + this.getHeight() - 6.5f, 5.0f, (Integer)clickGUI.color.getValue());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            if (this.hoveredLeft(mouseX, mouseY)) {
                this.leftDown = true;
            } else if (this.hoveredRight(mouseX, mouseY)) {
                this.rightDown = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            this.rightDown = false;
            this.leftDown = false;
        }
    }

    private boolean hoveredLeft(int mouseX, int mouseY) {
        float leftX = MathHelper.floor_double((((Number)this.rangedValue.getLeftValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * (this.getWidth() - 10.0f));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + leftX, this.getFinishedY(), 3.0, this.getHeight());
    }

    private boolean hoveredRight(int mouseX, int mouseY) {
        float rightX = MathHelper.floor_double((((Number)this.rangedValue.getRightValue()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) / (((Number)this.rangedValue.getMaximum()).floatValue() - ((Number)this.rangedValue.getMinimum()).floatValue()) * (this.getWidth() - 10.0f));
        return MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 5.0f + rightX, this.getFinishedY(), 3.0, this.getHeight());
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

