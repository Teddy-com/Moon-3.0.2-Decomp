/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.materialui.plane.impl;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.gui.materialui.component.Component;
import me.moon.gui.materialui.component.impl.CategoryComponent;
import me.moon.gui.materialui.plane.Plane;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

public class MainPlane
extends Plane {
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private ArrayList<Component> components = new ArrayList();
    private AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);

    public MainPlane(String label, float posX, float posY, float width, float height) {
        super(label, posX, posY, width, height);
    }

    @Override
    public void initializePlane() {
        super.initializePlane();
        for (Module.Category category : Module.Category.values()) {
            this.components.add(new CategoryComponent(category, this.getPosX(), this.getPosY(), 46.5f, 45.0f, this.getWidth() - 46.5f, this.getHeight() - 45.0f));
        }
        this.components.forEach(Component::initializeComponent);
    }

    @Override
    public void planeMoved(float movedX, float movedY) {
        super.planeMoved(movedX, movedY);
        this.components.forEach(component -> component.componentMoved(movedX, movedY));
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        if (this.isDragging()) {
            this.setPosX((float)mouseX + this.getLastPosX());
            this.setPosY((float)mouseY + this.getLastPosY());
            this.planeMoved(this.getPosX(), this.getPosY());
        }
        if (this.getPosX() < 0.0f) {
            this.setPosX(0.0f);
            this.planeMoved(this.getPosX(), this.getPosY());
        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.getPosX() + this.getWidth() > (float)scaledResolution.getScaledWidth()) {
            this.setPosX((float)new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - this.getWidth());
            this.planeMoved(this.getPosX(), this.getPosY());
        }
        if (this.getPosY() < 0.0f) {
            this.setPosY(0.0f);
            this.planeMoved(this.getPosX(), this.getPosY());
        }
        ScaledResolution scaledResolution2 = new ScaledResolution(Minecraft.getMinecraft());
        if (this.getPosY() + this.getHeight() > (float)scaledResolution2.getScaledHeight()) {
            this.setPosY((float)new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - this.getHeight());
            this.planeMoved(this.getPosX(), this.getPosY());
        }
        RenderUtil.drawRoundedRect(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), 0.0, new Color(35, 35, 35, 255).getRGB());
        float categoryOffsetY = this.getPosY() + 50.0f;
        float categoryOffsetY1 = this.getPosY() + 50.0f;
        for (Module.Category category : Module.Category.values()) {
            if (this.getSelectedCategory() == category) {
                RenderUtil.drawRect(this.getPosX(), MathUtils.round(this.transUtil.getPosY(), 0) - 4.0, 105.5, (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f, new Color(25, 25, 25, 255).getRGB());
                RenderUtil.drawRect(this.getPosX(), MathUtils.round(this.transUtil.getPosY(), 0) - 4.0, 1.0, (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f, new Color(-15305263).getRGB());
                if (this.transUtil.getPosY() == 0.0 || this.isDragging()) {
                    this.transUtil.setPosY(categoryOffsetY - (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f / 4.0f);
                }
                this.transUtil.interpolate(0.0, categoryOffsetY - (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f / 4.0f, 35.0f / (float)Minecraft.getDebugFPS());
            }
            categoryOffsetY += (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f;
        }
        for (Module.Category category : Module.Category.values()) {
            Fonts.clickGuiIconFontB.drawStringWithShadow(category.getCharacter(), this.getPosX() + 5.0f, categoryOffsetY1 - 1.0f, this.getSelectedCategory() == category ? new Color(-14575885).getRGB() : new Color(229, 229, 223, 255).getRGB());
            Fonts.sectioBigger.drawStringWithShadow(StringUtils.capitalize((String)category.name().toLowerCase()), this.getPosX() + 26.0f, (double)categoryOffsetY1 + 2.5, this.getSelectedCategory() == category ? new Color(-14575885).getRGB() : new Color(229, 229, 223, 255).getRGB());
            categoryOffsetY1 += (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f;
        }
        RenderUtil.drawRect((double)this.getPosX() + 103.5, this.getPosY(), 97.0, this.getHeight(), new Color(25, 25, 25, 255).getRGB());
        RenderUtil.drawRect((double)this.getPosX() + 103.5, this.getPosY(), 97.0, 14.0, -15305263);
        Fonts.sectioBigger.drawStringWithShadow(StringUtils.capitalize((String)this.getSelectedCategory().name().toLowerCase()), (double)this.getPosX() + 103.5 + 48.0 - (double)((float)Fonts.sectioBigger.getStringWidth(StringUtils.capitalize((String)this.getSelectedCategory().name().toLowerCase())) / 2.0f), this.getPosY() + 4.0f, new Color(229, 229, 223, 255).getRGB());
        for (Component component : this.getComponents()) {
            CategoryComponent categoryComponent;
            if (!(component instanceof CategoryComponent) || (categoryComponent = (CategoryComponent)component).getCategory() != this.getSelectedCategory()) continue;
            categoryComponent.onDrawScreen(mouseX, mouseY, partialTicks);
        }
        Fonts.sectioBigger2.drawStringWithShadow("Moon", (double)this.getPosX() + 50.25 - (double)((float)Fonts.sectioBigger2.getStringWidth("Moon") / 2.0f), this.getPosY() + 13.0f, new Color(-15305263).getRGB());
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), 15.0);
        if (button == 0) {
            if (hovered) {
                this.setLastPosX(this.getPosX() - (float)mouseX);
                this.setLastPosY(this.getPosY() - (float)mouseY);
                this.setDragging(true);
            }
            float categoryOffsetY = this.getPosY() + 50.0f;
            for (Module.Category category : Module.Category.values()) {
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), categoryOffsetY - (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f / 4.0f - 4.0f, 103.5, (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f)) {
                    this.setSelectedCategory(category);
                }
                categoryOffsetY += (float)Fonts.clickGuiIconFontB.getHeight() * 2.5f;
            }
        }
        for (Component component : this.getComponents()) {
            CategoryComponent categoryComponent;
            if (!(component instanceof CategoryComponent) || (categoryComponent = (CategoryComponent)component).getCategory() != this.getSelectedCategory()) continue;
            categoryComponent.onMouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && this.isDragging()) {
            this.setDragging(false);
        }
        for (Component component : this.getComponents()) {
            CategoryComponent categoryComponent;
            if (!(component instanceof CategoryComponent) || (categoryComponent = (CategoryComponent)component).getCategory() != this.getSelectedCategory()) continue;
            categoryComponent.onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        for (Component component : this.getComponents()) {
            CategoryComponent categoryComponent;
            if (!(component instanceof CategoryComponent) || (categoryComponent = (CategoryComponent)component).getCategory() != this.getSelectedCategory()) continue;
            categoryComponent.onKeyTyped(character, keyCode);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Component component : this.getComponents()) {
            CategoryComponent categoryComponent;
            if (!(component instanceof CategoryComponent) || (categoryComponent = (CategoryComponent)component).getCategory() != this.getSelectedCategory()) continue;
            categoryComponent.onGuiClosed();
        }
    }

    public Module.Category getSelectedCategory() {
        return this.selectedCategory;
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }
}

