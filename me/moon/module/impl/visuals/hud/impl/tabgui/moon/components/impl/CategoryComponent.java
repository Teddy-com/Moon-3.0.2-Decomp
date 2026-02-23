/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.impl;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.module.Module;
import me.moon.module.impl.visuals.Blur;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.TabGuiMoon;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.TabComponent;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.impl.ModuleComponent;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class CategoryComponent
extends TabComponent {
    private Module selectedModule;
    private Module.Category category;
    private TabGuiMoon tabGui;
    private AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);
    public ArrayList<ModuleComponent> components = new ArrayList();

    public CategoryComponent(TabGuiMoon tabGui, Module.Category category, float posX, float posY, float width, float height) {
        super(category.name(), posX, posY, width, height);
        this.tabGui = tabGui;
        this.category = category;
        this.selectedModule = Moon.INSTANCE.getModuleManager().getModulesInCategory(category).isEmpty() ? null : Moon.INSTANCE.getModuleManager().getModulesInCategory(category).get(0);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        double yOfSelector = 0.0;
        for (ModuleComponent component : this.components) {
            if (this.selectedModule != component.getModule()) continue;
            yOfSelector = component.getPosY();
        }
        if (this.tabGui.getSelectedCategory() == this.category) {
            this.transUtil.interpolate(this.getPosX() + 8.0f, yOfSelector - 4.0, 15.0f / (float)Minecraft.getDebugFPS());
        } else {
            this.transUtil.interpolate(this.getPosX() + 4.0f, yOfSelector - 4.0, 15.0f / (float)Minecraft.getDebugFPS());
        }
        if (this.testWithinBounds((int)this.transUtil.getPosX(), (int)this.getPosY(), this.tabGui.getPosX(), (int)this.tabGui.getPosY() - 4, (int)this.tabGui.getWidth(), (Fonts.jelloLight.getHeight() + 6) * 5)) {
            Fonts.moonLittleBigger.drawString(this.category.getName(), (float)this.transUtil.getPosX(), this.getPosY(), -1);
        }
        if (this.tabGui.isExtended() && this.tabGui.getSelectedCategory() == this.category) {
            if (Blur.blurTabGUI.getValue().booleanValue()) {
                MCBlurUtil.drawBLURRRR((int)((float)((int)this.getPosX()) + this.getWidth() + 6.0f), (int)this.tabGui.getPosY() - 4, 100, (Fonts.jelloLight.getHeight() + 6) * this.components.size(), 25.0f);
            }
            RenderUtil.drawRect((int)((float)((int)this.getPosX()) + this.getWidth() + 6.0f), (int)this.tabGui.getPosY() - 4, 100.0, (Fonts.jelloLight.getHeight() + 6) * this.components.size(), new Color(21, 21, 21, 95).getRGB());
            GL11.glEnable((int)3042);
            RenderUtil.drawRect(this.getPosX() + this.getWidth() + 6.0f, (float)this.transUtil.getPosY(), 100.0, Fonts.jelloLight.getHeight() + 6, 0x44000000);
            RenderUtil.drawRect(this.getPosX() + this.getWidth() + 6.0f, (float)this.transUtil.getPosY(), 100.0, Fonts.jelloLight.getHeight() + 6, new Color(21, 21, 21, 65).getRGB());
            this.components.forEach(moduleComponent -> moduleComponent.onDraw(scaledResolution));
        }
        super.onDraw(scaledResolution);
    }

    @Override
    public void blur() {
        if (this.tabGui.isExtended() && this.tabGui.getSelectedCategory() == this.category) {
            RenderUtil.drawRect((int)((float)((int)this.getPosX()) + this.getWidth() + 6.0f), (int)this.tabGui.getPosY() - 4, 100.0, (Fonts.jelloLight.getHeight() + 6) * this.components.size(), -1);
        }
    }

    @Override
    public void init() {
        float y = this.tabGui.getPosY();
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category)) {
            this.components.add(new ModuleComponent(this, module, this.getPosX() + this.getWidth() + 6.0f, y, 100.0f, Fonts.jelloLight.getHeight() + 2));
            y += (float)(Fonts.jelloLight.getHeight() + 6);
        }
        this.components.forEach(TabComponent::init);
        super.init();
    }

    @Override
    public void onKeyPress(int key) {
        if (this.tabGui.isExtended() && this.tabGui.getSelectedCategory() == this.category) {
            this.components.forEach(component -> component.onKeyPress(key));
            switch (key) {
                case 208: {
                    this.setSelectedModule(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).get((Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) + 1) % Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).size()));
                    break;
                }
                case 200: {
                    this.setSelectedModule(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).get(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) - 1 < 0 ? Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).size() - 1 : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) - 1));
                }
            }
        }
        super.onKeyPress(key);
    }

    public Module getSelectedModule() {
        return this.selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public boolean testWithinBounds(int textX, int textY, double x, double y, double width, double height) {
        return (double)textX >= x && (double)textX <= x + width && (double)textY >= y && (double)textY <= y + height;
    }
}

