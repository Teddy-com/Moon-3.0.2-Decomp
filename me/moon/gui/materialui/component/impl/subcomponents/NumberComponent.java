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
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.util.MathHelper;

public class NumberComponent
extends Component {
    private NumberValue numberValue;
    private boolean sliding;

    public NumberComponent(NumberValue numberValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        float sliderWidth = MathHelper.floor_double((((Number)this.numberValue.getValue()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) / (((Number)this.numberValue.getMaximum()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) * (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f));
        Fonts.sectioNormal.drawStringWithShadow(this.getLabel(), this.getPosX(), this.getPosY() + 3.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawRoundedRect(this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f, this.getPosY() + 5.0f, this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f, 2.0, 0.0, new Color(25, 25, 25, 255).getRGB());
        RenderUtil.drawRoundedRect(this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f, this.getPosY() + 5.0f, sliderWidth, 2.0, 0.0, new Color(-15305263).getRGB());
        RenderUtil.drawCircle(this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + sliderWidth + 4.0f, this.getPosY() + 4.0f, 4.0f, new Color(-15305263).getRGB());
        Fonts.sectioNumberSlider.drawStringWithShadow(String.valueOf(this.numberValue.getValue()), this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + sliderWidth + 6.0f - (float)Fonts.sectioNumberSlider.getStringWidth(String.valueOf(this.numberValue.getValue())) / 2.0f, this.getPosY() + 12.0f, new Color(229, 229, 223, 255).getRGB());
        if (this.sliding) {
            if (this.numberValue.getValue() instanceof Double) {
                this.numberValue.setValue(this.round((double)((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (((Number)this.numberValue.getMaximum()).doubleValue() - ((Number)this.numberValue.getMinimum()).doubleValue()) / (double)(this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + ((Number)this.numberValue.getMinimum()).doubleValue(), 2));
            } else if (this.numberValue.getValue() instanceof Float) {
                this.numberValue.setValue(Float.valueOf((float)this.round(((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (((Number)this.numberValue.getMaximum()).floatValue() - ((Number)this.numberValue.getMinimum()).floatValue()) / (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + ((Number)this.numberValue.getMinimum()).floatValue(), 2)));
            } else if (this.numberValue.getValue() instanceof Long) {
                this.numberValue.setValue((long)this.round(((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (float)(((Number)this.numberValue.getMaximum()).longValue() - ((Number)this.numberValue.getMinimum()).longValue()) / (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + (float)((Number)this.numberValue.getMinimum()).longValue(), 2));
            } else if (this.numberValue.getValue() instanceof Integer) {
                this.numberValue.setValue((int)this.round(((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (float)(((Number)this.numberValue.getMaximum()).intValue() - ((Number)this.numberValue.getMinimum()).intValue()) / (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + (float)((Number)this.numberValue.getMinimum()).intValue(), 2));
            } else if (this.numberValue.getValue() instanceof Short) {
                this.numberValue.setValue((short)this.round(((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (float)(((Number)this.numberValue.getMaximum()).shortValue() - ((Number)this.numberValue.getMinimum()).shortValue()) / (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + (float)((Number)this.numberValue.getMinimum()).shortValue(), 2));
            } else if (this.numberValue.getValue() instanceof Byte) {
                this.numberValue.setValue((byte)this.round(((float)mouseX - (this.getPosX() + (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) + 4.0f)) * (float)(((Number)this.numberValue.getMaximum()).byteValue() - ((Number)this.numberValue.getMinimum()).byteValue()) / (this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 10.0f) + (float)((Number)this.numberValue.getMinimum()).byteValue(), 2));
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + (float)Fonts.clickGuiFont.getStringWidth(this.getLabel()) + 2.0f, this.getPosY() + 5.0f, this.getWidth() - (float)Fonts.sectioNormal.getStringWidth(this.getLabel()) - 8.0f, 2.0);
        if (button == 0 && hovered) {
            this.sliding = true;
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && this.sliding) {
            this.sliding = false;
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
    }

    private double round(double val, int places) {
        double v = (double)Math.round(val / ((Number)this.numberValue.getInc()).doubleValue()) * ((Number)this.numberValue.getInc()).doubleValue();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public NumberValue getNumberValue() {
        return this.numberValue;
    }
}

