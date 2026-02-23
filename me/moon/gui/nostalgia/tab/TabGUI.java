/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.nostalgia.tab;

import java.util.ArrayList;
import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.CategoryComponent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TabGUI {
    private Minecraft mc = Minecraft.getMinecraft();
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private ArrayList<Component> components = new ArrayList();
    private float posX;
    private float posY;
    private float width;
    private boolean extendedModule;
    private boolean extendedValue;
    private boolean extendedValueDynamic;

    public TabGUI(float posX, float posY, float width) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
    }

    public void init() {
        float y = this.posY;
        for (Module.Category category : Module.Category.values()) {
            this.components.add(new CategoryComponent(this, category, this.posX, y, this.width, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 4);
        }
        this.components.forEach(Component::init);
    }

    public void onDraw(ScaledResolution scaledResolution) {
        RenderUtil.drawBorderedRect(this.posX, this.posY, this.width, (this.mc.fontRendererObj.FONT_HEIGHT + 4) * this.components.size(), 0.5, -16777216, 0x60000000);
        this.components.forEach(component -> component.onDraw(scaledResolution));
    }

    public void onKeyPress(int key) {
        if (key == 203 && this.isExtendedModule() && !this.isExtendedValue()) {
            this.setExtendedModule(false);
        }
        if (this.isExtendedModule()) {
            this.components.forEach(component -> component.onKeyPress(key));
        }
        switch (key) {
            case 208: {
                if (this.isExtendedModule() || this.isExtendedValue()) break;
                this.setSelectedCategory(Module.Category.values()[(this.getSelectedCategory().ordinal() + 1) % Module.Category.values().length]);
                break;
            }
            case 200: {
                if (this.isExtendedModule() || this.isExtendedValue()) break;
                this.setSelectedCategory(Module.Category.values()[this.getSelectedCategory().ordinal() - 1 < 0 ? Module.Category.values().length - 1 : this.getSelectedCategory().ordinal() - 1]);
                break;
            }
            case 205: {
                if (this.isExtendedModule() || this.isExtendedValue() || Moon.INSTANCE.getModuleManager().getModulesInCategory(this.selectedCategory).isEmpty()) break;
                this.setExtendedModule(true);
                break;
            }
        }
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
        float y = this.getPosY();
        for (Component category : this.components) {
            category.setPosY(y);
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
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

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public boolean isExtendedModule() {
        return this.extendedModule;
    }

    public void setExtendedModule(boolean extendedModule) {
        this.extendedModule = extendedModule;
    }

    public boolean isExtendedValue() {
        return this.extendedValue;
    }

    public void setExtendedValue(boolean extendedValue) {
        this.extendedValue = extendedValue;
    }

    public boolean isExtendedValueDynamic() {
        return this.extendedValueDynamic;
    }

    public void setExtendedValueDynamic(boolean extendedValueDynamic) {
        this.extendedValueDynamic = extendedValueDynamic;
    }
}

