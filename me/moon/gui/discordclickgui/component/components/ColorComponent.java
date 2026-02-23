/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.discordclickgui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.ModuleComponent;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ColorComponent
extends Component {
    private final ColorValue colorValue;
    private boolean pressedhue;
    private boolean pressedhue2;
    private float pos;
    private float hue;
    private float saturation;
    private float brightness;
    private float xPos;
    private float yPos;
    private float xPos2;
    private float alpha;
    private boolean hovered;

    public ColorComponent(ModuleComponent parent, ColorValue value, float posX, float posY, float offsetX, float offsetY, float height) {
        super(value.getLabel(), posX, posY, offsetX, offsetY, height);
        this.colorValue = value;
        float[] hsb = new float[3];
        this.setPicker();
        Color clr = new Color((Integer)this.colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (float)MathUtils.round(this.brightness, 2);
        this.xPos2 = MathHelper.clamp_float(this.getHeight() - 55.0f - this.alpha * (this.getHeight() - 12.0f), -38.5f, 37.5f);
        this.pos = 0.0f;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.onDrawScreen(mouseX, mouseY, scaledResolution);
        Fonts.clickfont14.drawString(ChatFormatting.GRAY + this.getLabel(), this.getFinishedX(), this.getFinishedY() - 0.5f, -11645101);
        this.hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() - 5.0f, this.getWidth() - 10.0f, 4.0);
        RenderUtil.drawRect(this.getFinishedX() + this.getWidth() / 2.0f + 54.5f, this.getFinishedY() + 4.5f, 3.0, this.getHeight() - 9.0f, -11645101);
        RenderUtil.drawGradient2(this.getFinishedX() + this.getWidth() / 2.0f + 55.0f, this.getFinishedY() + 5.0f, this.getFinishedX() + this.getWidth() / 2.0f + 57.0f, this.getFinishedY() + this.getHeight() - 5.0f, -1, -16777216);
        RenderUtil.drawBorderedRect(this.getFinishedX() + this.getWidth() / 2.0f + 54.0f, this.getFinishedY() + 43.0f + this.xPos2, 4.0, 4.0, 0.5, -1, 0);
        if (this.isInCircle(this.getFinishedX() + this.getWidth() / 2.0f, this.getFinishedY() + this.getHeight() / 2.0f, 40.0, mouseX, mouseY) && this.pressedhue2) {
            this.xPos = (float)mouseX - (this.getFinishedX() + this.getWidth() / 2.0f);
            this.yPos = (float)mouseY - (this.getFinishedY() + this.getHeight() / 2.0f);
            this.colorValue.setValue(this.getColorPicked(mouseX, mouseY));
        }
        if (this.pressedhue) {
            double positionX = MathHelper.clamp_double(((float)mouseY - this.getFinishedY() + 5.0f) / (this.getHeight() - 10.0f), 0.0, 1.0);
            this.xPos2 = MathHelper.clamp_float((float)mouseY - (this.getFinishedY() + 5.0f + (this.getHeight() - 10.0f) / 2.0f), -38.5f, 37.5f);
            this.alpha = (float)(1.0 - positionX);
            this.colorValue.setValue(this.getColorPicked(mouseX, mouseY));
        }
        if (((Integer)this.colorValue.getValue()).intValue() != this.getColorPicked((int)this.xPos, (int)this.yPos)) {
            this.setPicker();
            this.colorValue.setValue(this.getColorPicked((int)this.xPos, (int)this.yPos));
        }
        GL11.glPushMatrix();
        RenderUtil.drawCircle2(this.getFinishedX() + this.getWidth() / 2.0f, this.getFinishedY() + this.getHeight() / 2.0f, 40.0, new Color(25, 25, 25).getRGB());
        RenderUtil.drawColorPickerCircle(this.getFinishedX() + this.getWidth() / 2.0f, this.getFinishedY() + this.getHeight() / 2.0f, 40.0, this.alpha);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        RenderUtil.drawCircle(this.getFinishedX() + this.getWidth() / 2.0f + this.xPos - 4.0f, this.getFinishedY() + this.getHeight() / 2.0f + this.yPos - 4.0f, 8.0f, -1);
        RenderUtil.drawCircle(this.getFinishedX() + this.getWidth() / 2.0f + this.xPos - 3.0f, this.getFinishedY() + this.getHeight() / 2.0f + this.yPos - 3.0f, 6.0f, (Integer)this.colorValue.getValue());
        GL11.glPopMatrix();
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + this.getWidth() / 2.0f + 55.0f, this.getFinishedY() + 5.0f, 2.0, this.getHeight() - 10.0f);
        boolean hovered2 = this.isInCircle(this.getFinishedX() + this.getWidth() / 2.0f, this.getFinishedY() + this.getHeight() / 2.0f, 40.0, mouseX, mouseY);
        if (mouseButton == 0) {
            if (hovered) {
                this.pressedhue = true;
            }
            if (hovered2) {
                this.pressedhue2 = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.onMouseReleased(mouseX, mouseY, mouseButton);
        if (this.pressedhue) {
            this.pressedhue = false;
        }
        if (this.pressedhue2) {
            this.pressedhue2 = false;
        }
    }

    public ColorValue getColorValue() {
        return this.colorValue;
    }

    private int getColorPicked(int mouseX, int mouseY) {
        return Color.getHSBColor(this.getHue(), (float)(Math.hypot(this.xPos, this.yPos) / 40.0), this.alpha).getRGB();
    }

    private float getHue() {
        return (float)(Math.toDegrees(Math.atan2(this.xPos, this.yPos)) % 360.0) / 360.0f;
    }

    private boolean isInCircle(double x, double y, double radius, double mouseX, double mouseY) {
        return (mouseX - x) * (mouseX - x) + (mouseY - y) * (mouseY - y) <= radius * radius;
    }

    private void setPicker() {
        float[] hsb = Color.RGBtoHSB(this.colorValue.getColor().getRed(), this.colorValue.getColor().getGreen(), this.colorValue.getColor().getBlue(), null);
        this.xPos = (float)((double)(hsb[1] * 40.0f) * (Math.sin(Math.toRadians(hsb[0] * 360.0f)) / Math.sin(Math.toRadians(90.0))));
        this.yPos = (float)((double)(hsb[1] * 40.0f) * (Math.sin(Math.toRadians(90.0f - hsb[0] * 360.0f)) / Math.sin(Math.toRadians(90.0))));
    }

    public float getWidth() {
        return 137.0f;
    }
}

