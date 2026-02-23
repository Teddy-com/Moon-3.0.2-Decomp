/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.nostalgia.tab.component.impl;

import java.util.ArrayList;
import me.moon.Moon;
import me.moon.gui.nostalgia.tab.TabGUI;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

public class CategoryComponent
extends Component {
    private ArrayList<ModuleComponent> moduleComponents = new ArrayList();
    private Module.Category category;
    private TabGUI mainTab;
    private Module selectedModule;

    public CategoryComponent(TabGUI mainTab, Module.Category category, float posX, float posY, float width, float height) {
        super(StringUtils.capitalize((String)category.name().toLowerCase()), posX, posY, width, height);
        this.category = category;
        this.mainTab = mainTab;
        this.selectedModule = Moon.INSTANCE.getModuleManager().getModulesInCategory(category).isEmpty() ? null : Moon.INSTANCE.getModuleManager().getModulesInCategory(category).get(0);
    }

    @Override
    public void init() {
        float y = this.getPosY();
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category)) {
            this.moduleComponents.add(new ModuleComponent(this, module, this.getPosX() + this.getWidth() + 2.0f, y, Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 22.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
        this.moduleComponents.forEach(Component::init);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (ModuleComponent moduleComponent2 : this.moduleComponents) {
            moduleComponent2.setPosX(this.getPosX() + this.getWidth() + 2.0f);
            moduleComponent2.setWidth(Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 22.0f);
        }
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel(), this.getPosX() + 2.0f + (float)(this.mainTab.getSelectedCategory() == this.getCategory() ? 7 : 0), this.getPosY() + 2.5f, this.mainTab.getSelectedCategory() == this.getCategory() ? -1 : -8355712);
        if (this.mainTab.isExtendedModule() && this.mainTab.getSelectedCategory() == this.getCategory()) {
            RenderUtil.drawBorderedRect(this.getPosX() + this.getWidth() + 2.0f, this.getPosY(), Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.category) + 22.0f, (this.mc.fontRendererObj.FONT_HEIGHT + 2) * this.moduleComponents.size() + 2, 0.5, -16777216, 0x60000000);
            this.moduleComponents.forEach(moduleComponent -> moduleComponent.onDraw(scaledResolution));
        }
        if (this.mainTab.getSelectedCategory() == this.getCategory()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
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
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
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

