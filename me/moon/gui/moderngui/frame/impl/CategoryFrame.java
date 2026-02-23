/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.moderngui.frame.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.moderngui.ModernClickGui;
import me.moon.gui.moderngui.component.Component;
import me.moon.gui.moderngui.component.impl.ModuleComponent;
import me.moon.gui.moderngui.frame.Frame;
import me.moon.module.Module;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategoryFrame
extends Frame {
    private final Module.Category moduleCategory;
    public ModuleComponent extendedComponent;
    public AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);
    public float maxScroll;
    public float scrollBefore;
    public float notAnimated = 288.0f;
    public float bottomHeight = 288.0f;
    private AnimationUtil testUtil = new AnimationUtil(0.0, 0.0);

    public CategoryFrame(Module.Category moduleCategory, float posX, float posY, float width, float height) {
        super(StringUtils.capitalize((String)moduleCategory.name().toLowerCase()), posX, posY, width, height);
        this.moduleCategory = moduleCategory;
    }

    @Override
    public void init() {
        float offsetY = this.getHeight() + 1.0f;
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.getModuleCategory())) {
            this.getComponents().add(new ModuleComponent(module, this.getPosX(), this.getPosY(), 0.0f, offsetY, this.getWidth(), 16.0f, this));
            offsetY += 14.0f;
        }
        super.init();
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.testUtil.interpolate(this.notAnimated, 0.0, 20.0f / (float)Minecraft.getDebugFPS());
        this.bottomHeight = (float)this.testUtil.getPosX();
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getPosX() - 1.0f, this.getPosY(), this.getWidth() + 2.0f, this.getHeight() + (this.isExtended() ? MathHelper.clamp_float(this.getCurrentHeight(), 0.0f, clickGUI.noScroll.getValue().booleanValue() ? (this.extendedComponent != null ? this.extendedComponent.getCurrentHeight() + this.extendedComponent.getHeight() : this.getCurrentHeight()) : this.bottomHeight) : 0.0f), new Color(25, 25, 25).getRGB());
        Fonts.moonMiddle.drawCenteredString(this.getLabel(), this.getPosX() + this.getWidth() / 2.0f, this.getPosY() + this.getHeight() / 2.0f - (float)Fonts.moonMiddle.getStringHeight(this.getLabel()) / 2.0f + 1.0f, new Color(200, 200, 200).getRGB());
        if (this.extendedComponent != null && !this.extendedComponent.isExtended()) {
            this.extendedComponent = null;
        }
        if (this.isExtended()) {
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + this.getHeight(), this.getWidth(), (this.getCurrentHeight() > this.bottomHeight ? this.bottomHeight : this.getCurrentHeight()) + 1.0f) && this.getCurrentHeight() > this.bottomHeight) {
                if (!clickGUI.noScroll.getValue().booleanValue()) {
                    int wheel = Mouse.getDWheel();
                    if (wheel < 0) {
                        if ((float)(this.getScrollY() - 12) < -(this.getCurrentHeight() - Math.min(this.getCurrentHeight(), this.bottomHeight))) {
                            this.setScrollY((int)(-(this.getCurrentHeight() - Math.min(this.getCurrentHeight(), this.bottomHeight))));
                        } else {
                            this.setScrollY(this.getScrollY() - 12);
                        }
                    } else if (wheel > 0) {
                        this.setScrollY(this.getScrollY() + 12);
                    }
                } else {
                    this.setScrollY(0);
                }
            }
            if (!clickGUI.noScroll.getValue().booleanValue()) {
                if (this.getScrollY() > 0) {
                    this.setScrollY(0);
                }
                if (this.getCurrentHeight() > this.bottomHeight) {
                    if ((float)(this.getScrollY() - 12) < -(this.getCurrentHeight() - this.bottomHeight)) {
                        this.setScrollY((int)(-(this.getCurrentHeight() - this.bottomHeight)));
                    }
                } else if (this.getScrollY() < 0) {
                    this.setScrollY(0);
                }
            }
            this.transUtil.interpolate(0.0, this.getScrollY(), 21.0f / (float)Minecraft.getDebugFPS());
            GL11.glPushMatrix();
            GL11.glEnable((int)3089);
            RenderUtil.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), this.getPosX(), this.getPosY() + this.getHeight(), this.getWidth(), (clickGUI.noScroll.getValue().booleanValue() ? (this.extendedComponent != null ? this.extendedComponent.getCurrentHeight() + this.extendedComponent.getHeight() : this.getCurrentHeight()) : this.bottomHeight) - 1.0f);
            if (this.extendedComponent != null) {
                this.extendedComponent.setOffsetY(this.getHeight() - 1.0f);
                this.extendedComponent.moved(this.getPosX(), this.getPosY());
                this.extendedComponent.setWidth(this.getWidth());
                this.extendedComponent.drawScreen(mouseX, mouseY, partialTicks);
                if (clickGUI.noScroll.getValue().booleanValue()) {
                    this.setScrollY(0);
                } else {
                    this.setScrollY((int)Math.max((float)this.getScrollY(), this.maxScroll));
                }
            } else {
                this.getComponents().forEach(component -> {
                    component.setWidth(this.getWidth());
                    component.drawScreen(mouseX, mouseY, partialTicks);
                });
            }
            GL11.glDisable((int)3089);
            GL11.glPopMatrix();
        }
        this.updatePositions();
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight())) {
            for (Component component2 : this.getComponents()) {
                component2.setDragging(true);
                for (Component component1 : ((ModuleComponent)component2).getComponents()) {
                    component1.setDragging(true);
                }
            }
        }
        if (this.extendedComponent != null) {
            this.extendedComponent.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }
        if (this.isExtended()) {
            double d = this.getPosX();
            double d2 = this.getPosY() + this.getHeight();
            double d3 = this.getWidth();
            float f = this.getCurrentHeight() > this.bottomHeight ? (clickGUI.noScroll.getValue().booleanValue() ? this.getCurrentHeight() : this.bottomHeight) : this.getCurrentHeight();
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, d, d2, d3, f + 1.0f)) {
                this.getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
            }
        }
        if (!this.isExtended() && ModernClickGui.extendedFrame == this) {
            this.setWidth(110.0f);
            ModernClickGui.extendedFrame = null;
            this.getComponents().forEach(component -> {
                component.setExtended(false);
                component.forceAnimation();
            });
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight())) {
            for (Component component : this.getComponents()) {
                component.setDragging(false);
                for (Component component1 : ((ModuleComponent)component).getComponents()) {
                    component1.setDragging(false);
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void updatePositions() {
        ClickGui clickGui = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGui");
        float offsetY = this.getHeight() - 1.0f;
        if (this.extendedComponent == null) {
            for (Component component : this.getComponents()) {
                component.setOffsetY(offsetY);
                component.moved(this.getPosX(), (float)((double)this.getPosY() + this.transUtil.getPosY()));
                if (component instanceof ModuleComponent && component.isExtended()) {
                    for (Component component1 : ((ModuleComponent)component).getComponents()) {
                        if (component1.getHidden().booleanValue()) continue;
                        offsetY += component1.getHeight();
                    }
                }
                offsetY += component.getHeight();
            }
        }
    }

    private float getCurrentHeight() {
        float cHeight = 0.0f;
        for (Component component : this.getComponents()) {
            if (component instanceof ModuleComponent && component.isExtended()) {
                for (Component component1 : ((ModuleComponent)component).getComponents()) {
                    if (component1.getHidden().booleanValue()) continue;
                    cHeight += component1.getHeight();
                }
            }
            cHeight += component.getHeight();
        }
        return cHeight;
    }

    private float getOriginalHeight() {
        float oHeight = 0.0f;
        for (Component component : this.getComponents()) {
            oHeight += component.getHeight();
        }
        return oHeight;
    }

    public Module.Category getModuleCategory() {
        return this.moduleCategory;
    }
}

