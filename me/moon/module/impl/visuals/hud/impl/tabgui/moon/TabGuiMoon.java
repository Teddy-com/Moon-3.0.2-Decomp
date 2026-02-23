/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.hud.impl.tabgui.moon;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.module.Module;
import me.moon.module.impl.visuals.Blur;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.TabComponent;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.impl.CategoryComponent;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.components.impl.ModuleComponent;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class TabGuiMoon {
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private final ArrayList<TabComponent> components = new ArrayList();
    private float posX;
    private float posY;
    private float width;
    private float scrollY;
    private boolean isExtended;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public TabGuiMoon(float posX, float posY, float width) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
    }

    public void init() {
        float y = this.posY;
        for (Module.Category category : Module.Category.values()) {
            this.components.add(new CategoryComponent(this, category, this.posX, y, this.width, Fonts.jelloLight.getHeight() + 2));
            y += (float)(Fonts.jelloLight.getHeight() + 6);
        }
        this.components.forEach(TabComponent::init);
    }

    public void onDraw(ScaledResolution resolution) {
        if (Blur.blurTabGUI.getValue().booleanValue()) {
            MCBlurUtil.drawBLURRRR((int)this.posX, (int)this.posY - 4, (int)this.width, (Fonts.jelloLight.getHeight() + 6) * 5, 25.0f);
        }
        RenderUtil.drawRect((int)this.posX, (int)this.posY - 4, (int)this.width, (Fonts.jelloLight.getHeight() + 6) * 5, new Color(21, 21, 21, 95).getRGB());
        GL11.glEnable((int)3042);
        RenderUtil.drawRect(this.posX, (float)this.animationUtil.getPosY(), (int)this.width, Fonts.jelloLight.getHeight() + 6, 0x44000000);
        RenderUtil.drawRect(this.posX, (float)this.animationUtil.getPosY(), (int)this.width, Fonts.jelloLight.getHeight() + 6, new Color(21, 21, 21, 65).getRGB());
        this.animationUtil.interpolate(this.scrollY, this.posY - 4.0f + (float)((Fonts.jelloLight.getHeight() + 6) * Math.min(this.getSelectedCategory().ordinal(), 4)), 15.0f / (float)Minecraft.getDebugFPS());
        if (!this.isExtended) {
            GL11.glEnable((int)3089);
            RenderUtil.prepareScissorBox(resolution, this.posX, this.posY - 4.0f, this.width + 10.0f, (Fonts.jelloLight.getHeight() + 6) * 5);
        }
        GL11.glEnable((int)3042);
        this.components.forEach(tabComponent -> tabComponent.onDraw(resolution));
        if (!this.isExtended) {
            GL11.glDisable((int)3089);
        }
        float y = (float)((double)this.posY + this.animationUtil.getPosX());
        for (TabComponent component : this.components) {
            component.setPosY(y);
            y += (float)(Fonts.jelloLight.getHeight() + 6);
        }
    }

    public void blur() {
        this.components.forEach(TabComponent::blur);
    }

    public void onKeyPress(int key) {
        this.components.forEach(tabComponent -> tabComponent.onKeyPress(key));
        switch (key) {
            case 208: {
                if (this.isExtended) break;
                if (this.getSelectedCategory().ordinal() >= 4 && this.getSelectedCategory().ordinal() != Module.Category.values().length - 1) {
                    this.updateDownwards();
                } else if (this.getSelectedCategory().ordinal() == Module.Category.values().length - 1) {
                    this.scrollY = 0.0f;
                }
                this.setSelectedCategory(Module.Category.values()[(this.getSelectedCategory().ordinal() + 1) % Module.Category.values().length]);
                break;
            }
            case 200: {
                if (this.isExtended) break;
                if (this.getSelectedCategory().ordinal() >= 5 && this.scrollY != 0.0f) {
                    this.updateUpwards();
                } else if (this.getSelectedCategory().ordinal() == 0) {
                    this.scrollY -= (float)((Fonts.jelloLight.getHeight() + 6) * (Module.Category.values().length - 5));
                }
                this.setSelectedCategory(Module.Category.values()[this.getSelectedCategory().ordinal() - 1 < 0 ? Module.Category.values().length - 1 : this.getSelectedCategory().ordinal() - 1]);
                break;
            }
            case 205: {
                if (this.isExtended) break;
                this.isExtended = true;
                break;
            }
            case 203: {
                if (!this.isExtended) break;
                this.isExtended = false;
            }
        }
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Module.Category getSelectedCategory() {
        return this.selectedCategory;
    }

    public void updateDownwards() {
        this.scrollY -= (float)(Fonts.jelloLight.getHeight() + 6);
    }

    public void updateUpwards() {
        this.scrollY += (float)(Fonts.jelloLight.getHeight() + 6);
    }

    public float getPosY() {
        return this.posY;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getWidth() {
        return this.width;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public void setPosY(float posY) {
        this.posY = posY;
        float y = posY;
        for (TabComponent tabComponent : this.components) {
            tabComponent.setPosY(posY);
            if (!(tabComponent instanceof CategoryComponent)) continue;
            CategoryComponent categoryComponent = (CategoryComponent)tabComponent;
            for (ModuleComponent component : categoryComponent.components) {
                component.setPosY(y);
                y += (float)(Fonts.jelloLight.getHeight() + 6);
            }
            y = posY;
        }
    }
}

