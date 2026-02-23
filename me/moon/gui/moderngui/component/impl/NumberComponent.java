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
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class NumberComponent
extends Component {
    private final NumberValue numberValue;
    private boolean sliding;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public NumberComponent(NumberValue numberValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;
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
        Fonts.moonSmaller.drawString(this.getLabel() + " - " + this.getNumberValue().getValue().toString(), this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.moonSmaller.getStringHeight(this.getLabel()) / 2.0f - 2.0f, -1);
        float length = MathHelper.floor_double((((Number)this.getNumberValue().getValue()).floatValue() - this.getNumberValue().getMinimum().floatValue()) / (this.getNumberValue().getMaximum().floatValue() - this.getNumberValue().getMinimum().floatValue()) * (this.getWidth() - 10.0f));
        if (this.animationUtil.getPosX() == 0.0) {
            this.animationUtil.setPosX(length);
        } else {
            this.animationUtil.interpolate(length, 0.0, 20.0f / (float)Minecraft.getDebugFPS());
        }
        RenderUtil.drawRoundedRect(this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() - 5.0f, this.getWidth() - 10.0f, 2.0, 1.0, new Color(25, 25, 25).getRGB());
        RenderUtil.drawRoundedRect(this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() - 5.0f, this.animationUtil.getPosX(), 2.0, 1.0, (Integer)clickGUI.color.getValue());
        RenderUtil.drawCircle((float)((double)(this.getFinishedX() + 4.0f) + this.animationUtil.getPosX()), this.getFinishedY() + this.getHeight() - 6.5f, 5.0f, (Integer)clickGUI.color.getValue());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.sliding) {
            if (this.getNumberValue().getValue() instanceof Float) {
                float preval = ((float)mouseX - (this.getFinishedX() + 5.0f)) * (this.getNumberValue().getMaximum().floatValue() - this.getNumberValue().getMinimum().floatValue()) / (this.getWidth() - 10.0f) + this.getNumberValue().getMinimum().floatValue();
                this.getNumberValue().setValue(Float.valueOf((float)this.round(preval, 2)));
            } else if (this.getNumberValue().getValue() instanceof Integer) {
                int preval = (int)(((float)mouseX - (this.getFinishedX() + 5.0f)) * (float)(this.getNumberValue().getMaximum().intValue() - this.getNumberValue().getMinimum().intValue()) / (this.getWidth() - 10.0f) + (float)this.getNumberValue().getMinimum().intValue());
                this.getNumberValue().setValue((int)this.round(preval, 2));
            } else if (this.getNumberValue().getValue() instanceof Double) {
                double preval = (double)((float)mouseX - (this.getFinishedX() + 5.0f)) * (this.getNumberValue().getMaximum().doubleValue() - this.getNumberValue().getMinimum().doubleValue()) / (double)(this.getWidth() - 10.0f) + this.getNumberValue().getMinimum().doubleValue();
                this.getNumberValue().setValue(this.round(preval, 2));
            } else if (this.getNumberValue().getValue() instanceof Long) {
                long preval = (long)((double)((float)mouseX - (this.getFinishedX() + 5.0f)) * (this.getNumberValue().getMaximum().doubleValue() - this.getNumberValue().getMinimum().doubleValue()) / (double)(this.getWidth() - 10.0f) + this.getNumberValue().getMinimum().doubleValue());
                this.getNumberValue().setValue((long)this.round(preval, 2));
            }
        }
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        super.mouseClicked(mx, my, button);
        if (MouseUtil.mouseWithinBounds(mx, my, this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight()) && button == 0) {
            this.setSliding(true);
        }
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        super.mouseReleased(mx, my, button);
        if (this.isSliding()) {
            this.setSliding(false);
        }
    }

    public void setSliding(boolean sliding) {
        this.sliding = sliding;
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

    public NumberValue<Number> getNumberValue() {
        return this.numberValue;
    }

    public boolean isSliding() {
        return this.sliding;
    }
}

