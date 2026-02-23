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
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class NumberComponent
extends Component {
    private NumberValue value;
    private ModuleComponent parent;
    private boolean sliding;

    public NumberComponent(ModuleComponent parent, NumberValue value, float posX, float posY, float offsetX, float offsetY, float height) {
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
        float length = MathHelper.floor_double((((Number)this.value.getValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        RenderUtil.drawRoundedRect(this.getFinishedX(), this.getFinishedY() + 10.0f, 137.0, 4.0, 2.0, -11578276);
        RenderUtil.drawRoundedRect(this.getFinishedX(), this.getFinishedY() + 10.0f, length, 4.0, 2.0, -9270822);
        RenderUtil.drawRoundedRect(this.getFinishedX() + length - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0, 2.0, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + length - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0) || this.sliding) {
            RenderUtil.drawRoundedRect(this.getFinishedX() + length - (float)(Fonts.clickfont14.getStringWidth(this.value.getValue().toString()) / 2) - 2.0f, this.getFinishedY() - 3.0f, Fonts.clickfont14.getStringWidth(this.value.getValue().toString()) + 4, Fonts.clickfont14.getHeight() + 4, 2.0, -9275779);
            Fonts.clickfont14.drawString(this.value.getValue().toString(), this.getFinishedX() + length - (float)(Fonts.clickfont14.getStringWidth(this.value.getValue().toString()) / 2), this.getFinishedY(), -1);
        }
        if (this.sliding) {
            if (this.value.getValue() instanceof Double) {
                this.value.setValue(this.round((double)((float)mouseX - this.getFinishedX()) * (((Number)this.value.getMaximum()).doubleValue() - ((Number)this.value.getMinimum()).doubleValue()) / 137.0 + ((Number)this.value.getMinimum()).doubleValue(), 2));
            } else if (this.value.getValue() instanceof Float) {
                this.value.setValue(Float.valueOf((float)this.round(((float)mouseX - this.getFinishedX()) * (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / 137.0f + ((Number)this.value.getMinimum()).floatValue(), 2)));
            } else if (this.value.getValue() instanceof Long) {
                this.value.setValue((long)this.round(((float)mouseX - this.getFinishedX()) * (float)(((Number)this.value.getMaximum()).longValue() - ((Number)this.value.getMinimum()).longValue()) / 137.0f + (float)((Number)this.value.getMinimum()).longValue(), 2));
            } else if (this.value.getValue() instanceof Integer) {
                this.value.setValue((int)this.round(((float)mouseX - this.getFinishedX()) * (float)(((Number)this.value.getMaximum()).intValue() - ((Number)this.value.getMinimum()).intValue()) / 137.0f + (float)((Number)this.value.getMinimum()).intValue(), 2));
            } else if (this.value.getValue() instanceof Short) {
                this.value.setValue((short)this.round(((float)mouseX - this.getFinishedX()) * (float)(((Number)this.value.getMaximum()).shortValue() - ((Number)this.value.getMinimum()).shortValue()) / 137.0f + (float)((Number)this.value.getMinimum()).shortValue(), 2));
            } else if (this.value.getValue() instanceof Byte) {
                this.value.setValue((byte)this.round(((float)mouseX - this.getFinishedX()) * (float)(((Number)this.value.getMaximum()).byteValue() - ((Number)this.value.getMinimum()).byteValue()) / 137.0f + (float)((Number)this.value.getMinimum()).byteValue(), 2));
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        float length = MathHelper.floor_double((((Number)this.value.getValue()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) / (((Number)this.value.getMaximum()).floatValue() - ((Number)this.value.getMinimum()).floatValue()) * 137.0f);
        if ((MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY() + 10.0f, 137.0, 4.0) || MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + length - 2.0f, this.getFinishedY() + 7.0f, 4.0, 9.0)) && button == 0) {
            this.sliding = true;
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (this.sliding) {
            this.sliding = false;
        }
    }

    @Override
    public void onKeyTyped(char keyChar, int key) {
        super.onKeyTyped(keyChar, key);
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

    public NumberValue getValue() {
        return this.value;
    }
}

