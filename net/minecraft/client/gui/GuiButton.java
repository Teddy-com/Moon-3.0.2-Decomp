/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.awt.Color;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton
extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected int width = 200;
    protected int height = 20;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected boolean hovered;
    protected boolean isMainMenu;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText, false);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, false);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean isMainMenu) {
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.isMainMenu = isMainMenu;
    }

    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            if (!this.isMainMenu) {
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 0xE0E0E0;
                if (!this.enabled) {
                    j = 0xA0A0A0;
                } else if (this.hovered) {
                    j = 0xFFFFA0;
                }
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            } else {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                RenderUtil.drawBorderedRect(this.xPosition, this.yPosition, this.width, this.height, 0.5, -15374912, -15395563);
                this.mouseDragged(mc, mouseX, mouseY);
                if (this.hovered) {
                    RenderUtil.drawBorderedRect(this.xPosition, this.yPosition, this.width, this.height, 0.5, new Color(-15374912).brighter().getRGB(), -13619152);
                }
                if (this.displayString.equals("a") || this.displayString.equals("b") || this.displayString.equals("c") || this.displayString.equals("d")) {
                    Fonts.mainMenuIcons.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2) - (this.displayString.equals("d") ? 0.5f : 0.0f), (float)(this.yPosition + (this.height - 8) / 2) - (this.displayString.equals("b") ? 0.5f : 0.0f), -1979711489);
                } else {
                    Fonts.moon.drawCenteredString(this.displayString, (float)this.xPosition + (float)this.width / 2.0f, (float)this.yPosition + ((float)this.height - 5.0f) / 2.0f, -1979711489);
                }
            }
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

