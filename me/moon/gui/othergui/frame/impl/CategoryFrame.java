/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.othergui.frame.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.othergui.component.Component;
import me.moon.gui.othergui.component.impl.ModuleComponent;
import me.moon.gui.othergui.frame.Frame;
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
    private AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);

    public CategoryFrame(Module.Category moduleCategory, float posX, float posY, float width, float height) {
        super(StringUtils.capitalize((String)moduleCategory.name().toLowerCase()), posX, posY, width, height);
        this.moduleCategory = moduleCategory;
    }

    @Override
    public void init() {
        float offsetY = this.getHeight() + 1.0f;
        for (Module module : Moon.INSTANCE.getModuleManager().getModulesInCategory(this.getModuleCategory())) {
            this.getComponents().add(new ModuleComponent(module, this.getPosX(), this.getPosY(), 0.0f, offsetY, this.getWidth(), 16.0f));
            offsetY += 16.0f;
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
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getPosX() - 1.0f, this.getPosY(), this.getWidth() + 2.0f, this.getHeight() + (this.isExtended() ? MathHelper.clamp_float(this.getCurrentHeight(), 0.0f, 360.0f) : 0.0f), new Color(25, 25, 25).getRGB());
        Fonts.astolfoFont.drawStringWithShadow(this.getLabel(), this.getPosX() + 2.0f, this.getPosY() + this.getHeight() / 2.0f - (float)Fonts.astolfoFont.getStringHeight(this.getLabel()) / 2.0f + 0.5f, new Color(200, 200, 200).getRGB());
        Fonts.clickGuiIconFont.drawString(this.moduleCategory.getCharacter(), this.getPosX() + this.getWidth() - 3.0f - (float)Fonts.clickGuiIconFont.getStringWidth(this.moduleCategory.getCharacter()), this.getPosY() + 0.5f + this.getHeight() / 2.0f - (float)Fonts.clickGuiIconFont.getHeight() / 2.0f, (Integer)clickGUI.color.getValue());
        if (this.isExtended()) {
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + this.getHeight(), this.getWidth(), (this.getCurrentHeight() > 360.0f ? 360.0f : this.getCurrentHeight()) + 1.0f) && this.getCurrentHeight() > 360.0f) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    if ((float)(this.getScrollY() - 12) < -(this.getCurrentHeight() - Math.min(this.getCurrentHeight(), 360.0f))) {
                        this.setScrollY((int)(-(this.getCurrentHeight() - Math.min(this.getCurrentHeight(), 360.0f))));
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
            if (this.getCurrentHeight() > 360.0f) {
                if ((float)(this.getScrollY() - 12) < -(this.getCurrentHeight() - 360.0f)) {
                    this.setScrollY((int)(-(this.getCurrentHeight() - 360.0f)));
                }
            } else if (this.getScrollY() < 0) {
                this.setScrollY(0);
            }
            this.transUtil.interpolate(0.0, this.getScrollY(), 8.0f / (float)Minecraft.getDebugFPS());
            GL11.glPushMatrix();
            GL11.glEnable((int)3089);
            RenderUtil.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), this.getPosX(), this.getPosY() + this.getHeight(), this.getWidth(), 359.0f);
            this.getComponents().forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
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
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isExtended() && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() + this.getHeight(), this.getWidth(), (this.getCurrentHeight() > 360.0f ? 360.0f : this.getCurrentHeight()) + 1.0f)) {
            this.getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void updatePositions() {
        ClickGui clickGui = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGui");
        float offsetY = this.getHeight() - 1.0f;
        for (Component component : this.getComponents()) {
            component.setOffsetY(offsetY);
            component.moved(this.getPosX(), this.getPosY() + (float)this.getScrollY());
            if (component instanceof ModuleComponent && component.isExtended()) {
                for (Component component1 : ((ModuleComponent)component).getComponents()) {
                    if (component1.getHidden().booleanValue()) continue;
                    offsetY += component1.getHeight();
                }
            }
            offsetY += component.getHeight();
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

