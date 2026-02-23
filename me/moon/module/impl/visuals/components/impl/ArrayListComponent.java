/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.Moon;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ArrayListComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        ArrayList<Module> modulesEnabled = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        modulesEnabled.removeIf(module -> !module.isEnabled());
        modulesEnabled.removeIf(Module::isHidden);
        ArrayList<Module> modulesAstolfo = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        modulesAstolfo.sort(Comparator.comparingDouble(module -> -Fonts.astolfoFont.getStringWidth(this.getModuleString((Module)module))));
        modulesAstolfo.removeIf(Module::isHidden);
        modulesAstolfo.removeIf(module -> !module.isEnabled());
        ArrayList<Module> modules = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        switch (this.getArrayListMode()) {
            case JELLO: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.jelloLight.getStringWidth(module.getLabel())));
                modules.removeIf(Module::isHidden);
                modulesEnabled.sort(Comparator.comparingDouble(module -> -Fonts.jelloLight.getStringWidth(module.getLabel())));
                break;
            }
            case MOON: {
                if (HUD.mcFont.getValue().booleanValue()) {
                    modules.sort(Comparator.comparingDouble(module -> -this.mc.fontRendererObj.getStringWidth(this.getModuleString((Module)module))));
                } else {
                    modules.sort(Comparator.comparingDouble(module -> -Fonts.moon.getStringWidth(this.getModuleString((Module)module))));
                }
                modules.removeIf(module -> !module.isEnabled());
                modules.removeIf(Module::isHidden);
                break;
            }
            case NOVOLINE: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.novolineFont.getStringWidth(this.getModuleString((Module)module))));
                modules.removeIf(Module::isHidden);
                modulesEnabled.sort(Comparator.comparingDouble(module -> -Fonts.novolineFont.getStringWidth(this.getModuleString((Module)module))));
                break;
            }
            case ASTOLFO: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.astolfoFont.getStringWidth(this.getModuleString((Module)module))));
                modules.removeIf(Module::isHidden);
                break;
            }
            case REMIX: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.remixFont.getStringWidth(this.getModuleString((Module)module))));
                modules.removeIf(Module::isHidden);
                modules.removeIf(module -> !module.isEnabled());
                break;
            }
            case XAVE: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.hudNormal.getStringWidth(this.getModuleString((Module)module))));
                modules.removeIf(Module::isHidden);
                modules.removeIf(module -> !module.isEnabled());
                break;
            }
            case FLUX: {
                modules.sort(Comparator.comparingDouble(module -> -Fonts.fluxRoboto.getStringWidth(this.getModuleString((Module)module))));
                modules.removeIf(Module::isHidden);
                modules.removeIf(module -> !module.isEnabled());
            }
        }
        float yPos = 0.0f;
        switch (this.getArrayListMode()) {
            case ASTOLFO: {
                yPos = 1.0f;
                float yNotAnimated = 1.0f;
                for (Module module2 : modules) {
                    if (Float.isNaN(module2.offset)) {
                        module2.offset = 0.0f;
                    }
                    if (!module2.isEnabled() && module2.offset == 0.0f) continue;
                    String moduleString = this.getModuleString(module2);
                    if (HUD.astolfoBack.isEnabled()) {
                        RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset), yPos - 1.0f, Fonts.astolfoFont.getStringWidth(moduleString) + 7, 10.0, new Color(35, 35, 35, (Integer)HUD.backgroundAlpha.getValue()).getRGB());
                    }
                    boolean rightOffset = false;
                    if (HUD.astolfoSide.isEnabled()) {
                        switch ((HUD.SidebarMode)((Object)HUD.sidebarMode.getValue())) {
                            case LEFT: {
                                RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset), yPos - 1.0f, 1.0, 10.0, this.getColor(yNotAnimated, 0.88f));
                                rightOffset = true;
                                break;
                            }
                            case WRAP: {
                                RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset), yPos - 1.0f, 1.0, 10.0, this.getColor(yNotAnimated, 0.88f));
                                rightOffset = true;
                                if (modulesAstolfo.indexOf(module2) == modulesAstolfo.size() - 1) {
                                    RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset), yPos + (float)Fonts.astolfoFont.getHeight() + 2.0f, (float)Fonts.astolfoFont.getStringWidth(moduleString) + 5.5f, 1.0, this.getColor(yNotAnimated, 0.88f));
                                    break;
                                }
                                if (modulesAstolfo.indexOf(module2) + 1 == modulesAstolfo.size() || !module2.isEnabled()) break;
                                Module nextMod = modulesAstolfo.get(modulesAstolfo.indexOf(module2) + 1);
                                float dist = Fonts.astolfoFont.getStringWidth(moduleString) - Fonts.astolfoFont.getStringWidth(this.getModuleString(nextMod));
                                RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset), yPos + (float)Fonts.astolfoFont.getHeight() + 2.0f, dist, 1.0, this.getColor(yNotAnimated, 0.88f));
                                break;
                            }
                            case RIGHT: {
                                RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) - 5.5f + module2.offset + (float)Fonts.astolfoFont.getStringWidth(moduleString) + 4.0f) : (double)((float)(sr.getScaledWidth() + 5) + module2.offset + (float)Fonts.astolfoFont.getStringWidth(moduleString) + 4.0f), yPos - 1.0f, 1.0, 10.0, this.getColor(yNotAnimated, 0.88f));
                            }
                        }
                    }
                    Fonts.astolfoFont.drawStringWithShadow(moduleString, module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)Fonts.astolfoFont.getStringWidth(moduleString) + module2.offset - 4.0f + (rightOffset ? 1.5f : 0.0f) + (float)(HUD.sidebarMode.getValue() != HUD.SidebarMode.RIGHT || !HUD.astolfoSide.isEnabled() ? 1 : 0)) : (double)((float)sr.getScaledWidth() + module2.offset + 4.0f + (rightOffset ? 1.5f : 0.0f) + (float)(HUD.sidebarMode.getValue() != HUD.SidebarMode.RIGHT || !HUD.astolfoSide.isEnabled() ? 1 : 0)), yPos + 1.0f, this.getColor(yNotAnimated, 0.88f), -14606047);
                    yPos += module2.isEnabled() ? 9.0f - ArrayListComponent.map(module2.offset, 0.0f, Fonts.astolfoFont.getStringWidth(moduleString), 0.0f, 9.0f) : -ArrayListComponent.map(module2.offset, 0.0f, Fonts.astolfoFont.getStringWidth(moduleString), 0.0f, 9.0f);
                    yNotAnimated += (float)(Fonts.astolfoFont.getHeight() + 2);
                    float difference = module2.offset < 0.0f ? 0.0f - module2.offset : module2.offset - 0.0f;
                    float xd = Minecraft.getDebugFPS();
                    xd = difference / xd * 75.0f;
                    if (module2.offset > 0.0f) {
                        module2.offset -= xd / 12.0f;
                    }
                    if (module2.offset < 0.0f) {
                        module2.offset += xd / 12.0f;
                    }
                    if (modulesAstolfo.indexOf(module2) != modulesAstolfo.size() - 1) continue;
                    HUD.offset = yPos;
                }
                break;
            }
            case FLUX: {
                yPos = 6.0f;
                float yPosRects = 3.0f;
                for (Module module3 : modules) {
                    String moduleString = this.getModuleString(module3);
                    float width = Fonts.fluxRoboto.getStringWidth(moduleString);
                    float x = (float)sr.getScaledWidth() - width - 2.5f;
                    int color = RenderUtil.getRainbow(4500, (int)(-(yPos * 15.0f)), 0.6f);
                    RenderUtil.drawRect(x - 3.5f, yPosRects, width + 7.0f, Fonts.fluxRoboto.getHeight() + 4, new Color(0, 0, 0, (Integer)HUD.backgroundAlpha.getValue()).getRGB());
                    RenderUtil.drawRect(x - 2.0f + width + 3.0f, yPosRects, 3.0, Fonts.fluxRoboto.getHeight() + 4, color);
                    Fonts.fluxRoboto.drawString(moduleString, x - 1.5f, yPos, color);
                    yPos += (float)Fonts.fluxRoboto.getHeight() + 4.0f;
                    yPosRects += (float)Fonts.fluxRoboto.getHeight() + 4.0f;
                    if (modules.indexOf(module3) != modules.size() - 1) continue;
                    HUD.offset = yPos;
                }
                break;
            }
            case XAVE: {
                yPos = 3.0f;
                for (Module module4 : modules) {
                    if (module4.isShouldAnimate()) {
                        module4.getTransUtil().setPosX(sr.getScaledWidth());
                        module4.setShouldAnimate(false);
                    }
                    String moduleString = this.getModuleString(module4);
                    AnimationUtil animationUtil = module4.getTransUtil();
                    float width = Fonts.hudNormal.getStringWidth(moduleString);
                    animationUtil.linear((float)sr.getScaledWidth() - width - 2.0f, 0.0, 200.0f / (float)Minecraft.getDebugFPS());
                    RenderUtil.drawRect(animationUtil.getPosX() - 2.0, yPos - (modules.indexOf(module4) == 0 ? 1.0f : 0.5f), width + (modules.indexOf(module4) == 0 ? 5.0f : 4.0f), (float)Fonts.hudNormal.getHeight() + (modules.indexOf(module4) == 0 ? 3.0f : 2.5f), new Color(0, 0, 0, (Integer)HUD.backgroundAlpha.getValue()).getRGB());
                    Fonts.hudNormal.drawStringWithShadow(moduleString, animationUtil.getPosX() - 0.5, yPos + 0.5f, this.getColor(yPos, 0.88f));
                    yPos += (float)Fonts.hudNormal.getHeight() + 2.5f;
                    if (modules.indexOf(module4) != modules.size() - 1) continue;
                    HUD.offset = yPos;
                }
                break;
            }
            case MOON: {
                yPos = 5.0f;
                for (Module module5 : modules) {
                    String moduleString = this.getModuleString(module5);
                    AnimationUtil animationUtil = module5.getTransUtil();
                    float height = Fonts.moon.getHeight() + 2;
                    float width = Fonts.moon.getStringWidth(moduleString);
                    if (HUD.mcFont.getValue().booleanValue()) {
                        height = this.mc.fontRendererObj.FONT_HEIGHT + 2;
                        width = this.mc.fontRendererObj.getStringWidth(moduleString);
                    }
                    int color = this.getColor(yPos, HUD.mcFont.getValue() != false ? 0.92f : 0.88f);
                    if (module5.isShouldAnimate()) {
                        animationUtil.setPosY(yPos);
                        animationUtil.setPosX(sr.getScaledWidth() + Fonts.moon.getStringWidth(moduleString));
                        module5.setShouldAnimate(false);
                    }
                    if (HUD.moonBackground.getValue().booleanValue() && !Moon.INSTANCE.getModuleManager().getModule("Blur").isEnabled()) {
                        RenderUtil.drawRect(animationUtil.getPosX() - 1.0, animationUtil.getPosY(), width + 4.0f, height, new Color(0, 0, 0, (Integer)HUD.backgroundAlpha.getValue()).getRGB());
                    }
                    if (HUD.moonSide.getValue().booleanValue() && modules.indexOf(module5) == 0) {
                        int i = 0;
                        while ((float)i < width + 4.0f) {
                            RenderUtil.drawRect(animationUtil.getPosX() - 1.0 + (double)i, animationUtil.getPosY() - 0.5, 1.0, 0.5, this.getColor(i, 0.99f));
                            ++i;
                        }
                    }
                    if (HUD.mcFont.getValue().booleanValue()) {
                        this.mc.fontRendererObj.drawStringWithShadow(moduleString, (float)animationUtil.getPosX() + 1.0f, (float)animationUtil.getPosY() + 1.5f, color);
                    } else {
                        Fonts.moon.drawStringWithShadow(moduleString, animationUtil.getPosX() + 0.5, animationUtil.getPosY() + 1.5, color);
                    }
                    animationUtil.interpolate((float)sr.getScaledWidth() - width - 8.0f, yPos, 14.0f / (float)Minecraft.getDebugFPS());
                    if (modules.indexOf(module5) == modules.size() - 1) {
                        HUD.offset = (float)module5.getTransUtil().getPosY();
                    }
                    yPos += height;
                }
                break;
            }
            case NOVOLINE: {
                for (Module module6 : modules) {
                    int color = this.getColor(yPos, 0.85f);
                    AnimationUtil animationUtil = module6.getTransUtil();
                    String baseString = this.getModuleString(module6);
                    float baseWidth = Fonts.novolineFont.getStringWidth(baseString);
                    float baseX = (float)sr.getScaledWidth() - baseWidth - 4.0f;
                    boolean shouldDraw = Math.round(animationUtil.getPosX()) < (long)sr.getScaledWidth();
                    animationUtil.linear(module6.isEnabled() ? (double)baseX : (double)(sr.getScaledWidth() + 5), yPos, 200.0f / (float)Minecraft.getDebugFPS());
                    if (animationUtil.getPosX() == 0.0 || animationUtil.getPosX() < (double)baseX || (double)(sr.getScaledWidth() + 5) < animationUtil.getPosX()) {
                        animationUtil.setPosX(module6.isEnabled() ? (double)baseX : (double)(sr.getScaledWidth() + 5));
                    }
                    if (module6.isShouldAnimate()) {
                        animationUtil.setPosY(yPos + (float)Fonts.novolineFont.getHeight() + 2.0f);
                        module6.setShouldAnimate(false);
                    }
                    if (shouldDraw) {
                        float animatedX = (float)animationUtil.getPosX();
                        float animatedY = (float)animationUtil.getPosY();
                        if (HUD.novoBack.getValue().booleanValue()) {
                            RenderUtil.drawRect(animatedX - 2.0f, animatedY, baseWidth + 8.0f, (float)Fonts.novolineFont.getHeight() + 1.5f, new Color(0, 0, 0, (Integer)HUD.backgroundAlpha.getValue()).getRGB());
                        }
                        boolean offsetRight = false;
                        if (HUD.novoSide.getValue().booleanValue()) {
                            switch ((HUD.SidebarMode)((Object)HUD.sidebarMode.getValue())) {
                                case RIGHT: {
                                    RenderUtil.drawRect(animatedX + baseWidth + 3.0f, animatedY, 1.0, (float)Fonts.novolineFont.getHeight() + 1.5f, color);
                                    break;
                                }
                                case WRAP: {
                                    offsetRight = true;
                                    RenderUtil.drawRect(animatedX - 3.0f, animatedY, 1.0, (float)Fonts.novolineFont.getHeight() + 1.5f, color);
                                    if (module6.isEnabled() && modulesEnabled.indexOf(module6) == modulesEnabled.size() - 1) {
                                        RenderUtil.drawRect(animatedX - 3.0f, animatedY + (float)Fonts.novolineFont.getHeight() + 1.5f, baseWidth + 7.0f, 1.0, color);
                                        break;
                                    }
                                    if (!module6.isEnabled() || modulesEnabled.indexOf(module6) >= modulesEnabled.size() - 1) break;
                                    Module nextModule = modulesEnabled.get(modulesEnabled.indexOf(module6) + 1);
                                    float difference = Fonts.novolineFont.getStringWidth(this.getModuleString(module6)) - Fonts.novolineFont.getStringWidth(this.getModuleString(nextModule));
                                    RenderUtil.drawRect(animatedX - 3.0f, animatedY + (float)Fonts.novolineFont.getHeight() + 1.5f, difference, 1.0, color);
                                    break;
                                }
                                case LEFT: {
                                    offsetRight = true;
                                    RenderUtil.drawRect(animatedX - 3.0f, animatedY, 1.0, (float)Fonts.novolineFont.getHeight() + 1.5f, color);
                                }
                            }
                        }
                        Fonts.novolineFont.drawStringWithShadow(baseString, animatedX + (offsetRight ? 0.5f : 0.0f), animatedY + 1.0f, color);
                        yPos += (float)Fonts.novolineFont.getHeight() + 1.5f;
                    }
                    if (modules.indexOf(module6) != modules.size() - 1) continue;
                    HUD.offset = Math.max((float)module6.getTransUtil().getPosY(), (float)sr.getScaledHeight() / 2.0f - 50.0f);
                }
                break;
            }
            case REMIX: {
                yPos = 3.0f;
                for (Module module7 : modules) {
                    int color = this.getColor(yPos, 0.88f);
                    float xPos = sr.getScaledWidth() - Fonts.remixFont.getStringWidth(this.getModuleString(module7));
                    Color colorRemix = new Color(-13710223);
                    if (HUD.remixBack.getValue().booleanValue()) {
                        RenderUtil.drawRect(xPos - (float)(HUD.remixSide.isEnabled() ? 9 : 4), yPos - 3.0f, Fonts.remixFont.getStringWidth(this.getModuleString(module7)) + 9, (float)Fonts.remixFont.getHeight() + 4.5f, 0x67000000);
                    }
                    if (HUD.remixSide.getValue().booleanValue()) {
                        RenderUtil.drawRect(sr.getScaledWidth() - 1, yPos - 3.0f, 1.0, (float)Fonts.remixFont.getHeight() + 4.5f, color);
                    }
                    Fonts.remixFont.drawStringWithShadow(this.getModuleString(module7), xPos - (float)(HUD.remixSide.isEnabled() ? 5 : 2), yPos, color);
                    if (modules.indexOf(module7) == modules.size() - 1) {
                        HUD.offset = Math.max(yPos, (float)sr.getScaledHeight() / 2.0f - 50.0f);
                    }
                    yPos += (float)Fonts.remixFont.getHeight() + 4.5f;
                }
                break;
            }
            case JELLO: {
                yPos = 6.0f;
                for (Module module8 : modules) {
                    if (module8.isShouldAnimate()) {
                        module8.getTransUtil().setPosY(yPos);
                        module8.setShouldAnimate(false);
                    }
                    if (module8.isEnabled()) {
                        module8.getAnimationUtil2().interpolate(1.0, 0.0, 5.0f / (float)Minecraft.getDebugFPS());
                        if (MathUtils.roundDouble(module8.getAnimationUtil2().getPosX(), 1) == 1.0) {
                            module8.setShouldAnimate(false);
                        }
                        module8.getAnimationUtil3().interpolate(1.0, 1.0, 14.0f / (float)Minecraft.getDebugFPS());
                    }
                    if (!module8.isEnabled()) {
                        module8.getAnimationUtil2().interpolate(0.05f, 0.0, 30.0f / (float)Minecraft.getDebugFPS());
                        module8.getAnimationUtil3().interpolate(0.75, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
                    }
                    if (!(module8.getAnimationUtil2().getPosX() > 0.2)) continue;
                    GL11.glPushMatrix();
                    float xPos = (float)(((double)sr.getScaledWidth() - (double)((float)Fonts.jelloLight.getStringWidth(module8.getLabel()) / 2.0f) / module8.getAnimationUtil3().getPosX() - 5.0) / module8.getAnimationUtil3().getPosX());
                    float yJello = (float)((double)((float)module8.getTransUtil().getPosY()) / module8.getAnimationUtil3().getPosX());
                    GL11.glScaled((double)module8.getAnimationUtil3().getPosX(), (double)module8.getAnimationUtil3().getPosX(), (double)1.0);
                    GL11.glTranslated((double)MathUtils.roundDouble(-((double)Fonts.jelloLight.getStringWidth(module8.getLabel()) * module8.getAnimationUtil3().getPosX()) / 2.0, 1), (double)0.0, (double)0.0);
                    Fonts.jelloLight.drawString(module8.getLabel(), xPos, yJello, new Color(255, 255, 255, (int)(module8.getAnimationUtil2().getPosX() * 255.0)).getRGB());
                    GL11.glPopMatrix();
                    module8.getTransUtil().interpolate(0.0, yPos, 25.0f / (float)Minecraft.getDebugFPS());
                    if (module8.isEnabled()) {
                        yPos += (float)(Fonts.jelloLight.getHeight() + 4);
                    }
                    if (modulesEnabled.indexOf(module8) != modulesEnabled.size() - 1) continue;
                    HUD.offset = (float)module8.getTransUtil().getPosY();
                }
                break;
            }
        }
        super.render(event);
    }

    private int getColor(float yPos, float offset) {
        int color = -1;
        switch ((HUD.ColorModes)((Object)HUD.colorModes.getValue())) {
            case ASTOLFO: {
                color = RenderUtil.getGradient((double)(-(yPos / (float)Fonts.astolfoFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0, new Color(243, 111, 122), new Color(142, 95, 255), new Color(55, 230, 239)).getRGB();
                break;
            }
            case MOON: {
                color = RenderUtil.getGradientOffset(new Color(2, 163, 245), new Color(21, 70, 193), (double)(-(yPos / (float)Fonts.moon.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case CUSTOM: {
                color = (Integer)HUD.colorValue.getValue();
                break;
            }
            case RAINBOW: {
                color = RenderUtil.getRainbow(4500, (int)(-(yPos * 15.0f)), 0.5f);
                break;
            }
            case CHRISTMAS: {
                color = RenderUtil.getGradientOffset(new Color(255, 255, 255), new Color(255, 69, 69), (double)(-(yPos / (float)Fonts.moon.getHeight() / 5.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case STATICFADE: {
                color = this.color(Float.valueOf((yPos + 2.0f) * offset), HUD.colorValue.getColor().getRed(), HUD.colorValue.getColor().getGreen(), HUD.colorValue.getColor().getBlue());
            }
        }
        return color;
    }

    @Override
    public void blur() {
        ArrayList<Module> mods = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        mods.sort(Comparator.comparingDouble(module -> -Fonts.moonThemeSF.getStringWidth(this.getModuleString((Module)module))));
        mods.removeIf(module -> !module.isEnabled());
        mods.removeIf(Module::isHidden);
        switch (this.getArrayListMode()) {
            case MOON: {
                for (Module module2 : mods) {
                    AnimationUtil animationUtil = module2.getTransUtil();
                    float height = Fonts.moon.getHeight() + 2;
                    float width = Fonts.moon.getStringWidth(this.getModuleString(module2));
                    if (HUD.mcFont.getValue().booleanValue()) {
                        height = this.mc.fontRendererObj.FONT_HEIGHT + 2;
                        width = this.mc.fontRendererObj.getStringWidth(this.getModuleString(module2));
                    }
                    RenderUtil.drawRect(animationUtil.getPosX() - 1.0, animationUtil.getPosY(), width + 4.0f, height, -1);
                }
                break;
            }
        }
        super.blur();
    }

    @Override
    public void bloom() {
        ArrayList<Module> mods = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        mods.sort(Comparator.comparingDouble(module -> -Fonts.moonThemeSF.getStringWidth(this.getModuleString((Module)module))));
        mods.removeIf(module -> !module.isEnabled());
        mods.removeIf(Module::isHidden);
        switch (this.getArrayListMode()) {
            case MOON: {
                for (Module module2 : mods) {
                    AnimationUtil animationUtil = module2.getTransUtil();
                    float height = Fonts.moon.getHeight() + 2;
                    float width = Fonts.moon.getStringWidth(this.getModuleString(module2));
                    if (HUD.mcFont.getValue().booleanValue()) {
                        height = this.mc.fontRendererObj.FONT_HEIGHT + 2;
                        width = this.mc.fontRendererObj.getStringWidth(this.getModuleString(module2));
                    }
                    RenderUtil.drawRect(animationUtil.getPosX() - 1.0, animationUtil.getPosY(), width + 4.0f, height, -1);
                }
                break;
            }
        }
        super.bloom();
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(this.getArrayListMode() == HUD.ArrayListMode.FLUX ? ChatFormatting.WHITE : ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public HUD.ArrayListMode getArrayListMode() {
        return (HUD.ArrayListMode)((Object)HUD.arrayListModes.getValue());
    }
}

