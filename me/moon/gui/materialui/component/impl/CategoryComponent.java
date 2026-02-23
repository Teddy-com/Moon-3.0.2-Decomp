/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.materialui.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.gui.materialui.component.Component;
import me.moon.gui.materialui.component.impl.subcomponents.ModuleComponent;
import me.moon.module.Module;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategoryComponent
extends Component {
    private Module.Category category;
    private ArrayList<Component> components = new ArrayList();
    private int scrollY;

    public CategoryComponent(Module.Category category, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(StringUtils.capitalize((String)category.name().toLowerCase()), posX, posY, offsetX, offsetY, width, height);
        this.category = category;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        float moduleOffsetY = 0.0f;
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.category)) {
            this.components.add(new ModuleComponent(this, module, this.getPosX() + 60.0f, this.getPosY() - 20.0f, 0.0f, moduleOffsetY, 90.0f, 20.0f));
            moduleOffsetY += 20.0f;
        }
        this.components.forEach(Component::initializeComponent);
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
        this.components.forEach(component -> component.componentMoved(this.getPosX() + 60.0f, this.getPosY() - 20.0f));
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        double scrollbarHeight = this.getHeight() / (float)this.getComponentHeight() * this.getHeight() + 20.0f;
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + 60.0f, this.getPosY() - 20.0f, 95.0, this.getHeight() + 20.0f) && (float)this.getComponentHeight() >= this.getHeight()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if ((float)(this.getScrollY() - 16) < -((float)this.getComponentHeight() - this.getHeight())) {
                    this.setScrollY((int)(-((float)this.getComponentHeight() - this.getHeight())));
                } else {
                    this.setScrollY(this.getScrollY() - 16);
                }
            } else if (wheel > 0) {
                this.setScrollY(this.getScrollY() + 16);
            }
        }
        if (this.getScrollY() > 0) {
            this.setScrollY(0);
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), this.getPosX() - 2.0f, this.getPosY() - 22.0f, this.getWidth(), this.getHeight() + 20.0f);
        for (Component component2 : this.getComponents()) {
            if (!(component2 instanceof ModuleComponent)) continue;
            ModuleComponent moduleComponent = (ModuleComponent)component2;
            moduleComponent.onDrawScreen(mouseX, mouseY, partialTicks);
            moduleComponent.setOffsetY(component2.getOriginalOffsetY() + (float)this.getScrollY());
            moduleComponent.componentMoved(this.getPosX() + 60.0f, this.getPosY() - 20.0f);
        }
        if ((float)this.getComponentHeight() >= this.getHeight()) {
            RenderUtil.drawRect(this.getPosX() + 158.0f, this.getPosY() - 26.0f, 2.0, this.getHeight() + 26.0f, new Color(55, 55, 55, 255).getRGB());
            RenderUtil.drawRect(this.getPosX() + 158.0f, (double)(this.getPosY() - 26.0f) - ((double)(this.getHeight() + 20.0f) - (scrollbarHeight - 4.0)) / (double)((float)this.getComponentHeight() - (this.getHeight() + 20.0f)) * (double)this.getScrollY(), 2.0, scrollbarHeight, new Color(40, 40, 40, 255).getRGB());
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        if (this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).isExtended()).toArray().length > 0) {
            ModuleComponent moduleComponent = (ModuleComponent)this.getComponents().stream().filter(component -> component instanceof ModuleComponent && ((ModuleComponent)component).isExtended()).collect(Collectors.toList()).get(0);
            Fonts.clickGuiSmallFont.drawStringWithShadow(StringUtils.capitalize((String)moduleComponent.getModule().getCategory().name().toLowerCase()) + "/" + moduleComponent.getModule().getLabel(), this.getPosX() + 160.0f, this.getPosY() - 36.0f, new Color(155, 155, 155, 255).getRGB());
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() - 2.0f, this.getPosY() - 22.0f, this.getWidth(), this.getHeight() + 22.0f)) {
            for (Component component : this.getComponents()) {
                if (!(component instanceof ModuleComponent)) continue;
                ModuleComponent moduleComponent = (ModuleComponent)component;
                moduleComponent.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        for (Component component : this.getComponents()) {
            if (!(component instanceof ModuleComponent)) continue;
            ModuleComponent moduleComponent = (ModuleComponent)component;
            moduleComponent.onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        for (Component component : this.getComponents()) {
            if (!(component instanceof ModuleComponent)) continue;
            ModuleComponent moduleComponent = (ModuleComponent)component;
            moduleComponent.onKeyTyped(character, keyCode);
        }
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
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
            h = (int)((float)h + component.getHeight());
        }
        return h;
    }
}

