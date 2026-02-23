/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.tab.component.impl;

import java.util.ArrayList;
import me.moon.Moon;
import me.moon.gui.tab.TabGUI;
import me.moon.gui.tab.component.Component;
import me.moon.gui.tab.component.impl.ModuleComponent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.FontUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

public class CategoryComponent
extends Component {
    private ArrayList<ModuleComponent> moduleComponents = new ArrayList();
    private Module.Category category;
    private TabGUI mainTab;
    private Module selectedModule;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public CategoryComponent(TabGUI mainTab, Module.Category category, float posX, float posY, float width, float height) {
        super(StringUtils.capitalize((String)category.name().toLowerCase()), posX, posY, width, height);
        this.category = category;
        this.mainTab = mainTab;
        this.selectedModule = Moon.INSTANCE.getModuleManager().getModulesInCategory(category).isEmpty() ? null : Moon.INSTANCE.getModuleManager().getModulesInCategory(category).get(0);
        for (ModuleComponent component : this.moduleComponents) {
            if (this.selectedModule != component.getModule()) continue;
            this.animationUtil.setPosY(component.getPosY());
        }
        this.animationUtil.setPosX(this.getPosX() + (float)(mainTab.getSelectedCategory() == this.getCategory() ? 5 : 2));
    }

    @Override
    public void init() {
        float y = this.getPosY();
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category)) {
            this.moduleComponents.add(new ModuleComponent(this, module, this.getPosX() + this.getWidth() + 2.0f, y, Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 22.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            y += FontUtil.getStringHeight("COMBAT") + 5.0f;
        }
        this.moduleComponents.forEach(Component::init);
    }

    @Override
    public void blur() {
        if (this.mainTab.isExtendedModule() && this.mainTab.getSelectedCategory() == this.getCategory()) {
            RenderUtil.drawRect(this.getPosX() + this.getWidth() + 2.0f - 10.0f, this.getPosY(), Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 12.0f, (FontUtil.getHeight() + 3.0f) * (float)this.moduleComponents.size() + 2.0f, -1155456735);
        }
        this.moduleComponents.forEach(Component::blur);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (ModuleComponent component : this.moduleComponents) {
            if (this.selectedModule != component.getModule()) continue;
            this.animationUtil.interpolate(this.getPosX() + (float)(this.mainTab.getSelectedCategory() == this.getCategory() ? 5 : 2), component.getPosY(), 14.0f / (float)Minecraft.getDebugFPS());
        }
        FontUtil.drawShadowedString(this.getLabel(), (float)this.animationUtil.getPosX(), this.getPosY() + 7.0f - FontUtil.getHeight() / 2.0f, -1);
        if (this.mainTab.isExtendedModule() && this.mainTab.getSelectedCategory() == this.getCategory()) {
            RenderUtil.drawRect(this.getPosX() + this.getWidth() + 2.0f - 10.0f, this.getPosY(), Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 12.0f, (FontUtil.getHeight() + 3.0f) * (float)this.moduleComponents.size() + 2.0f, -1155456735);
            RenderUtil.drawRect(this.getPosX() + this.getWidth() + 2.0f - 10.0f, this.animationUtil.getPosY(), Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 12.0f, FontUtil.getHeight() + 3.0f + 2.0f, HUD.getColorHUD());
            this.moduleComponents.forEach(moduleComponent -> moduleComponent.onDraw(scaledResolution));
        }
        float y = this.getPosY();
        for (Component component : this.moduleComponents) {
            component.setPosY(y);
            y += FontUtil.getHeight() + 3.0f;
        }
    }

    @Override
    public void onKeyPress(int key) {
        if (this.moduleComponents.isEmpty()) {
            return;
        }
        if (this.mainTab.isExtendedModule() && this.mainTab.getSelectedCategory() == this.getCategory()) {
            switch (key) {
                case 208: {
                    if (this.mainTab.isExtendedValue() || this.mainTab.isExtendedValueDynamic()) break;
                    this.setSelectedModule(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).get((Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) + 1) % Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).size()));
                    break;
                }
                case 200: {
                    if (this.mainTab.isExtendedValue() || this.mainTab.isExtendedValueDynamic()) break;
                    this.setSelectedModule(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).get(Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) - 1 < 0 ? Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).size() - 1 : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category).indexOf(this.getSelectedModule()) - 1));
                    break;
                }
                case 28: {
                    if (this.mainTab.isExtendedValue() || this.mainTab.isExtendedValueDynamic()) break;
                    this.getSelectedModule().setEnabled(!this.getSelectedModule().isEnabled());
                    break;
                }
                case 205: {
                    if (this.mainTab.isExtendedValue() || this.mainTab.isExtendedValueDynamic() || this.getSelectedModule().getValues().isEmpty()) break;
                    this.mainTab.setExtendedValue(true);
                    break;
                }
                case 203: {
                    if (!this.mainTab.isExtendedValue() || this.mainTab.isExtendedValueDynamic()) break;
                    this.mainTab.setExtendedValue(false);
                }
            }
            this.moduleComponents.forEach(moduleComponent -> moduleComponent.onKeyPress(key));
        }
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
        float y = this.getPosY();
        for (Component component : this.moduleComponents) {
            component.setPosY(y);
            y += FontUtil.getHeight() + 3.0f;
        }
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public Module getSelectedModule() {
        return this.selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public TabGUI getMainTab() {
        return this.mainTab;
    }
}

