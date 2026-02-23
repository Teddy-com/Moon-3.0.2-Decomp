/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.materialui.component.impl.subcomponents;

import java.awt.Color;
import me.moon.gui.materialui.component.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ColorComponent
extends Component {
    private ColorValue colorValue;
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
        this.pos = 0.0f;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        Fonts.clickGuiFont.drawStringWithShadow(this.getLabel(), this.getPosX(), this.getPosY() + 3.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawBorderedRect(this.getPosX() + 100.0f, this.getPosY() + 12.5f, 15.0, 15.0, 0.5, new Color(25, 25, 25).getRGB(), (Integer)this.colorValue.getValue());
        RenderUtil.drawRect(this.getPosX() + this.getHeight() - 20.5f, this.getPosY() + 12.0f, 5.0, this.getHeight() - 25.0f, new Color(25, 25, 25).getRGB());
        Keyboard.enableRepeatEvents((boolean)false);
        this.hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + 12.0f, this.getWidth() - 70.0f, 4.0);
        for (float i = 0.0f; i < this.getHeight() - 26.0f; i += 1.0f) {
            int color = Color.getHSBColor(i / (this.getHeight() - 26.0f), 1.0f, 1.0f).getRGB();
            RenderUtil.drawRect(this.getPosX() + this.getHeight() - 20.0f, this.getPosY() + i + 12.5f, 4.0, 1.0, color);
            if ((float)mouseY == (float)((int)(this.getPosY() + 12.0f)) + i && this.pressedhue) {
                this.hue = i / (this.getHeight() - 26.0f);
            }
            if (0.001 * Math.floor((double)(i / (this.getHeight() - 26.0f)) * 1000.0) != 0.001 * Math.floor((double)this.hue * 1000.0)) continue;
            this.pos = i;
        }
        RenderUtil.drawRect(this.getPosX() + this.getHeight() - 20.5f, this.getPosY() + 12.5f + this.pos, 5.0, 1.0, -1);
        int colCalculated = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
        int colCaltulated2 = Color.HSBtoRGB(this.hue, 0.0f, 1.0f);
        int colCalculated3 = Color.HSBtoRGB(this.hue, 0.0f, 0.0f);
        int colCaltulated4 = Color.HSBtoRGB(this.hue, 0.0f, 0.0f);
        RenderUtil.drawRect((double)this.getPosX() - 0.5, this.getPosY() + 12.5f, this.getHeight() - 3.0f - 21.0f, this.getHeight() - 26.0f, new Color(25, 25, 25).getRGB());
        RenderUtil.drawGradientCPicker(this.getPosX(), this.getPosY() + 13.0f, this.getPosX() - 22.0f + this.getHeight() - 3.0f, this.getPosY() - 11.0f + this.getHeight() - 3.0f, colCalculated, colCalculated3, colCaltulated4, colCaltulated2);
        if (this.pressedhue2) {
            double xPos = MathHelper.clamp_double(((float)mouseX - this.getPosX()) / (this.getHeight() - 25.0f), 0.0, 1.0);
            double yPos = MathHelper.clamp_double(((float)mouseY - this.getPosY() + 30.0f - this.getHeight() / 2.0f) / (this.getHeight() - 25.0f), 0.0, 1.0);
            this.colorValue.setValue(Color.HSBtoRGB(this.hue, (float)xPos, 1.0f - (float)yPos));
        }
        GL11.glDisable((int)3008);
        RenderUtil.drawRect(this.getPosX() + this.getHeight() - 10.5f, this.getPosY() + 12.0f, 5.0, this.getHeight() - 25.0f, new Color(25, 25, 25).getRGB());
        RenderUtil.drawGradient2(this.getPosX() + this.getHeight() - 10.0f, this.getPosY() + 12.5f, this.getPosX() + this.getHeight() - 10.0f + 4.0f, this.getPosY() + 12.0f + this.getHeight() - 25.0f, (Integer)this.colorValue.getValue(), 0);
        this.pos2 = Math.max(this.getPosY() + 12.5f + (this.getHeight() - 27.0f) * -this.brightness + this.getHeight() - 27.0f, this.getPosY() + 12.5f);
        RenderUtil.drawRect(this.getPosX() + this.getHeight() - 10.5f, this.pos2, 5.0, 1.0, -1);
        if (this.pressedhue3) {
            double positionX = 1.0 - MathHelper.clamp_double(((float)mouseY - this.getPosY() - 13.5f) / (this.getHeight() - 25.0f), 0.0, 1.0);
            this.pos2 = Math.max((float)((double)(this.getPosY() + 12.5f) + (double)(this.getHeight() - 27.0f) * -positionX) + this.getHeight() - 27.0f, this.getPosY() + 12.5f);
            Color color = this.colorValue.getColor();
            Color alphaColor = new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)positionX);
            this.brightness = (float)positionX;
            this.colorValue.setValue(alphaColor.getRGB());
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + this.getHeight() - 20.5f, this.getPosY() + 12.0f, 5.0, this.getHeight() - 25.0f);
        boolean hovered2 = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + 12.0f, this.getHeight() - 25.0f, this.getHeight() - 25.0f);
        boolean hovered3 = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + this.getHeight() - 10.5f, this.getPosY() + 12.0f, 5.0, this.getHeight() - 25.0f);
        if (button == 0) {
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
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0) {
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
    }

    @Override
    public void onKeyTyped(char keyChar, int key) {
        super.onKeyTyped(keyChar, key);
    }

    public ColorValue getColorValue() {
        return this.colorValue;
    }
}

