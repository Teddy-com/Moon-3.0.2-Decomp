/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl.tabgui.jello.components.impl;

import me.moon.module.Module;
import me.moon.module.impl.visuals.hud.impl.tabgui.jello.components.TabComponent;
import me.moon.module.impl.visuals.hud.impl.tabgui.jello.components.impl.CategoryComponent;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleComponent
extends TabComponent {
    private CategoryComponent categoryComponent;
    private Module.Category subCategory;
    private Module module;
    private AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);

    public ModuleComponent(CategoryComponent categoryComponent, Module module, float posX, float posY, float width, float height) {
        super(module.getLabel(), posX, posY, width, height);
        this.module = module;
        this.categoryComponent = categoryComponent;
    }

    @Override
    public void onKeyPress(int key) {
        if (this.categoryComponent.getSelectedModule() == this.module) {
            switch (key) {
                case 28: 
                case 205: {
                    this.module.toggle();
                }
            }
        }
        super.onKeyPress(key);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        if (this.transUtil.getPosX() == 0.0) {
            this.transUtil.setPosX(this.getPosX() + 4.0f);
        }
        if (this.categoryComponent.getSelectedModule() == this.module) {
            this.transUtil.interpolate(this.getPosX() + 12.0f, 0.0, 15.0f / (float)Minecraft.getDebugFPS());
        } else {
            this.transUtil.interpolate(this.getPosX() + 4.0f, 0.0, 15.0f / (float)Minecraft.getDebugFPS());
        }
        if (this.module.isEnabled()) {
            Fonts.jelloLightBold.drawString(this.module.getLabel(), (float)this.transUtil.getPosX(), this.getPosY(), -1);
        } else {
            Fonts.jelloLight.drawString(this.module.getLabel(), (float)this.transUtil.getPosX(), this.getPosY(), -1);
        }
        super.onDraw(scaledResolution);
    }

    @Override
    public void init() {
        super.init();
    }

    public Module getModule() {
        return this.module;
    }
}

