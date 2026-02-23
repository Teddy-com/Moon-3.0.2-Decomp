/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.discordclickgui.frame.frames;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.ModuleComponent;
import me.moon.gui.discordclickgui.frame.Frame;
import me.moon.module.Module;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class DiscordFrame
extends Frame {
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private int scrollY;

    public DiscordFrame(String label, float posX, float posY, float width, float height) {
        super(label, posX, posY, width, height);
    }

    @Override
    public void init() {
        for (Module.Category category : Module.Category.values()) {
            float y = 34.0f;
            for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(category)) {
                this.getComponents().add(new ModuleComponent(this, module, this.getPosX(), this.getPosY(), 42.0f, y, Fonts.clickfont18.getHeight() * 2));
                y += (float)(Fonts.clickfont18.getHeight() * 2);
            }
        }
        super.init();
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
    }

    @Override
    public void onBloom() {
        ClickGui clickGui = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGui");
        if (Minecraft.getMinecraft().currentScreen == clickGui.getDiscordClickGUI()) {
            RenderUtil.drawRoundedRect(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight() - 1.0f, 6.0, -1);
        }
        super.onBloom();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.drawScreen(mouseX, mouseY, scaledResolution);
        RenderUtil.drawRoundedRect(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), 6.0, -13223618);
        RenderUtil.drawCircle(this.getPosX() + this.getWidth() - 12.0f, this.getPosY() + this.getHeight() - 12.0f, 12.0f, -13223618);
        RenderUtil.drawCircle(this.getPosX() + this.getWidth() - 12.0f, this.getPosY() + this.getHeight() - 12.0f, 12.0f, -13223618);
        RenderUtil.drawRoundedRect(this.getPosX(), this.getPosY(), 45.0, this.getHeight(), 6.0, -14605786);
        RenderUtil.drawCircle(this.getPosX(), this.getPosY() + this.getHeight() - 12.0f, 12.0f, -14605786);
        RenderUtil.drawCircle(this.getPosX(), this.getPosY() + this.getHeight() - 12.0f, 12.0f, -14605786);
        RenderUtil.drawRect(this.getPosX() + 40.0f, this.getPosY(), this.getWidth() / 3.0f, this.getHeight(), -13618890);
        RenderUtil.drawRoundedRect(this.getPosX(), this.getPosY(), this.getWidth(), 12.0, 6.0, -14671323);
        RenderUtil.drawRect(this.getPosX() + this.getWidth() - 12.0f, this.getPosY() + 8.0f, 12.0, 4.0, -14671323);
        RenderUtil.drawCircle(this.getPosX(), this.getPosY(), 12.0f, -14605786);
        RenderUtil.drawCircle(this.getPosX(), this.getPosY(), 12.0f, -14605786);
        RenderUtil.drawCircle(this.getPosX() + this.getWidth() - 12.0f, this.getPosY(), 12.0f, -14605786);
        RenderUtil.drawCircle(this.getPosX() + this.getWidth() - 12.0f, this.getPosY(), 12.0f, -14605786);
        RenderUtil.drawImage(new ResourceLocation("textures/client/DiscordLogo.png"), this.getPosX() + 2.0f, this.getPosY() + 2.0f, 31, 8);
        RenderUtil.drawRect(this.getPosX() + 40.0f, this.getPosY() + 28.0f, this.getWidth() - 40.0f, 0.5, -14671064);
        Fonts.clickfont18.drawString(StringUtils.capitalize((String)this.getSelectedCategory().name().toLowerCase()), this.getPosX() + 44.0f, this.getPosY() + 24.0f - (float)Fonts.clickfont18.getStringHeight(StringUtils.capitalize((String)this.getSelectedCategory().name().toLowerCase())), -1);
        RenderUtil.drawCircle(this.getPosX() + 5.5f, this.getPosY() + 14.5f, 29.0f, -9270822);
        RenderUtil.drawCircle(this.getPosX() + 5.5f, this.getPosY() + 14.5f, 29.0f, -9270822);
        GlStateManager.enableBlend();
        RenderUtil.drawImage(new ResourceLocation("textures/client/Discord.png"), this.getPosX() + 7.5f, this.getPosY() + 16.0f, 25, 25);
        RenderUtil.drawRoundedRect(this.getPosX() + 6.0f, this.getPosY() + 48.0f, 28.0, 2.0, 1.0, -13684935);
        float y = this.getPosY() + 55.0f;
        for (Module.Category category : Module.Category.values()) {
            boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + 5.0f, y, 30.0, 30.0);
            RenderUtil.drawCircle(this.getPosX() + 4.0f, y - 1.0f, 32.0f, new Color(-14671323).brighter().getRGB());
            RenderUtil.drawCircle(this.getPosX() + 4.0f, y - 1.0f, 32.0f, new Color(-14671323).brighter().getRGB());
            GL11.glPushMatrix();
            Color color = new Color(-9270822);
            GL11.glColor4f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)1.0f);
            Fonts.clickGuiIconFontB.drawStringWithShadow(category.getCharacter(), this.getPosX() + 20.0f - (float)Fonts.clickGuiIconFontB.getStringWidth(category.getCharacter()) / 2.0f, y + 16.0f - (float)Fonts.clickGuiIconFontB.getStringHeight(category.getCharacter()) / 2.0f, category == this.getSelectedCategory() ? -9270822 : -1);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glEnable((int)3089);
            RenderUtil.prepareScissorBox(scaledResolution, this.getPosX(), y, 3.0f, 31.0f);
            if (hovered || this.selectedCategory == category) {
                RenderUtil.drawRoundedRect(this.getPosX() - 1.5f, y + (this.selectedCategory == category ? 2.5f : 7.5f), 3.0, this.selectedCategory == category ? 25 : 15, 2.0, -9270822);
            }
            GL11.glDisable((int)3089);
            GL11.glPopMatrix();
            y += 33.5f;
        }
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + 35.0f, this.getPosY() + 30.0f, 100.33, 255.0) && this.getComponentHeight() >= 255) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (this.getScrollY() - 12 < -(this.getComponentHeight() - 256)) {
                    this.setScrollY(-(this.getComponentHeight() - 256));
                } else {
                    this.setScrollY(this.getScrollY() - 12);
                }
            } else if (wheel > 0) {
                this.setScrollY(this.getScrollY() + 12);
            }
        }
        if (this.getScrollY() > 0) {
            this.setScrollY(0);
        }
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(scaledResolution, this.getPosX() + 35.0f, this.getPosY() + 30.0f, this.getWidth(), this.getHeight() - 30.0f);
        this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == this.selectedCategory).forEach(component -> {
            component.onDrawScreen(mouseX, mouseY, scaledResolution);
            component.setOffsetY(component.getBaseOffsetY() + (float)this.getScrollY());
            component.updatePosition(this.getPosX(), this.getPosY());
        });
        GL11.glDisable((int)3089);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float y = this.getPosY() + 55.0f;
        switch (mouseButton) {
            case 0: {
                for (Module.Category category : Module.Category.values()) {
                    if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + 4.0f, y, 30.0, 30.0)) {
                        this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == this.selectedCategory).forEach(component -> {
                            ((ModuleComponent)component).setBinding(false);
                            ((ModuleComponent)component).setExtended(false);
                            ((ModuleComponent)component).setScrollY(0);
                        });
                        this.setSelectedCategory(category);
                        this.setScrollY(0);
                    }
                    y += 33.5f;
                }
                break;
            }
        }
        this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == this.selectedCategory).forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == this.selectedCategory).forEach(component -> component.onMouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == this.selectedCategory).forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    public Module.Category getSelectedCategory() {
        return this.selectedCategory;
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public int getScrollY() {
        return this.scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public int getComponentHeight() {
        int h = 0;
        for (Component component : this.getComponents()) {
            ModuleComponent moduleComponent = (ModuleComponent)component;
            if (moduleComponent.getModule().getCategory() != this.getSelectedCategory()) continue;
            h += 14;
        }
        return h;
    }
}

