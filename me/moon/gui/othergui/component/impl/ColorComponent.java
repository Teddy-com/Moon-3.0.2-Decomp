/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.othergui.component.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.othergui.component.Component;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ColorComponent
extends Component {
    private final ColorValue colorValue;
    private boolean pressedhue;
    private boolean pressedhue2;
    private boolean pressedhue3;
    private float pos;
    private float hue;
    private float saturation;
    private float brightness;
    private float pos2;
    private boolean hovered;

    public ColorComponent(ColorValue colorValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(colorValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.colorValue = colorValue;
        float[] hsb = new float[3];
        Color clr = new Color((Integer)colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public void init() {
        float[] hsb = new float[3];
        Color clr = new Color((Integer)this.colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getFinishedX(), this.getFinishedY(), this.getWidth(), 100.0, clickGUI.getColor());
        Fonts.astolfoFont.drawStringWithShadow(this.getLabel(), this.getFinishedX() + 5.0f, this.getFinishedY() + 3.0f, -1);
        RenderUtil.drawBorderedRect(this.getFinishedX() + 105.0f, this.getFinishedY() + 12.5f, 9.0, 9.0, 0.5, new Color(25, 25, 25).getRGB(), (Integer)this.colorValue.getValue());
        RenderUtil.drawRect(this.getFinishedX() + 100.0f - 15.5f, this.getFinishedY() + 12.0f, 5.0, 75.0, new Color(25, 25, 25).getRGB());
        Keyboard.enableRepeatEvents((boolean)false);
        this.hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY() + 12.0f, this.getWidth() - 70.0f, 4.0);
        for (float i = 0.0f; i < 74.0f; i += 1.0f) {
            int color = Color.getHSBColor(i / 74.0f, 1.0f, 1.0f).getRGB();
            RenderUtil.drawRect(this.getFinishedX() + 100.0f - 15.0f, this.getFinishedY() + i + 12.5f, 4.0, 1.0, color);
            if ((float)mouseY == (float)((int)(this.getFinishedY() + 12.0f)) + i && this.pressedhue) {
                this.hue = i / 74.0f;
            }
            if (0.001 * Math.floor((double)(i / 74.0f) * 1000.0) != 0.001 * Math.floor((double)this.hue * 1000.0)) continue;
            this.pos = i;
        }
        RenderUtil.drawRect(this.getFinishedX() + 100.0f - 15.5f, this.getFinishedY() + 12.5f + this.pos, 5.0, 1.0, -1);
        int colCalculated = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
        int colCaltulated2 = Color.HSBtoRGB(this.hue, 0.0f, 1.0f);
        int colCalculated3 = Color.HSBtoRGB(this.hue, 0.0f, 0.0f);
        int colCaltulated4 = Color.HSBtoRGB(this.hue, 0.0f, 0.0f);
        RenderUtil.drawRect((double)this.getFinishedX() + 4.5, this.getFinishedY() + 12.5f, 76.0, 74.0, new Color(25, 25, 25).getRGB());
        RenderUtil.drawGradientCPicker(this.getFinishedX() + 5.0f, this.getFinishedY() + 13.0f, this.getFinishedX() - 17.0f + 100.0f - 3.0f, this.getFinishedY() - 11.0f + 100.0f - 3.0f, colCalculated, colCalculated3, colCaltulated4, colCaltulated2);
        if (this.pressedhue2) {
            double xPos = MathHelper.clamp_double(((float)mouseX - this.getFinishedX()) / 75.0f, 0.0, 1.0);
            double yPos = MathHelper.clamp_double(((float)mouseY - this.getFinishedY() + 30.0f - 50.0f) / 75.0f, 0.0, 1.0);
            this.colorValue.setValue(Color.HSBtoRGB(this.hue, (float)xPos, 1.0f - (float)yPos));
        }
        GL11.glDisable((int)3008);
        RenderUtil.drawRect(this.getFinishedX() + 100.0f - 5.5f, this.getFinishedY() + 12.0f, 5.0, 75.0, new Color(25, 25, 25).getRGB());
        RenderUtil.drawGradient2(this.getFinishedX() + 100.0f - 5.0f, this.getFinishedY() + 12.5f, this.getFinishedX() + 100.0f - 5.0f + 4.0f, this.getFinishedY() + 12.0f + 100.0f - 25.0f, new Color((Integer)this.colorValue.getValue()).getRGB(), 0);
        this.pos2 = Math.max(this.getFinishedY() + 12.5f + 73.0f * -this.brightness + 100.0f - 27.0f, this.getFinishedY() + 12.5f);
        RenderUtil.drawRect(this.getFinishedX() + 100.0f - 5.5f, this.pos2, 5.0, 1.0, -1);
        if (this.pressedhue3) {
            double positionX = 1.0 - MathHelper.clamp_double(((float)mouseY - this.getFinishedY() - 13.5f) / 75.0f, 0.0, 1.0);
            this.pos2 = Math.max((float)((double)(this.getFinishedY() + 12.5f) + 73.0 * -positionX) + 100.0f - 27.0f, this.getFinishedY() + 12.5f);
            Color color = this.colorValue.getColor();
            Color alphaColor = new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)positionX);
            this.brightness = (float)positionX;
            this.colorValue.setValue(alphaColor.getRGB());
        }
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 100.0f - 15.5f, this.getFinishedY() + 12.0f, 5.0, 75.0);
        boolean hovered2 = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY() + 12.0f, 75.0, 75.0);
        boolean hovered3 = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 100.0f - 5.5f, this.getFinishedY() + 12.0f, 5.0, 75.0);
        if (mouseButton == 0) {
            if (hovered) {
                this.pressedhue = true;
            }
            if (hovered2) {
                this.pressedhue2 = true;
            }
            if (hovered3) {
                this.pressedhue3 = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (this.pressedhue) {
            this.pressedhue = false;
        }
        if (this.pressedhue2) {
            this.pressedhue2 = false;
        }
        if (this.pressedhue3) {
            this.pressedhue3 = false;
        }
    }

    public ColorValue getColorValue() {
        return this.colorValue;
    }
}

