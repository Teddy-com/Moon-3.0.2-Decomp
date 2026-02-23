/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.editscreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.module.impl.visuals.hud.editscreen.buttons.AddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.ArmorHUDAddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.ArraylistButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.PotionHUDAddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.RadarAddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.ScoreboardAddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.StringAddButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.TabGuiButton;
import me.moon.module.impl.visuals.hud.editscreen.buttons.impl.TargetHUDAddButton;
import me.moon.module.impl.visuals.hud.settingsscreen.SettingsHud;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiHudEditScreen
extends GuiScreen {
    private static boolean isDragging = false;
    private Component draggingHudComp = null;
    private double lastX;
    private double lastY;
    private boolean isAdding;
    private float widthScissor = 0.0f;
    private float heightScissor = 0.0f;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);
    private ArrayList<AddButton> buttons = new ArrayList();

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int y = sr.getScaledHeight() - 263;
        this.buttons.add(new StringAddButton(y));
        this.buttons.add(new ArraylistButton(y += 22));
        this.buttons.add(new ScoreboardAddButton(y += 22));
        this.buttons.add(new PotionHUDAddButton(y += 22));
        this.buttons.add(new ArmorHUDAddButton(y += 22));
        this.buttons.add(new TargetHUDAddButton(y += 22));
        this.buttons.add(new RadarAddButton(y += 22));
        this.buttons.add(new TabGuiButton(y += 22));
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        isDragging = false;
        this.draggingHudComp = null;
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (this.animationUtil.getPosX() == 0.0) {
            this.animationUtil.setPosX(sr.getScaledWidth() + 10);
        }
        if (this.isAdding) {
            this.animationUtil.interpolate(sr.getScaledWidth() - 150, 50.0, 13.0f / (float)Minecraft.getDebugFPS());
        } else {
            this.animationUtil.interpolate(sr.getScaledWidth() + 10, GuiHudEditScreen.isDragging() ? 25.0 : 5.0, 13.0f / (float)Minecraft.getDebugFPS());
        }
        if (this.animationUtil.getPosY() == 0.0) {
            this.animationUtil.setPosY(1.0);
        }
        if (!this.mc.gameSettings.ofFastRender) {
            MCBlurUtil.drawBLURRRR(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (float)this.animationUtil.getPosY());
        }
        RenderUtil.drawRoundedRect(this.animationUtil.getPosX(), sr.getScaledHeight() - 265, 100.0, 200.0, 1.0, -14606047);
        this.buttons.forEach(button -> {
            button.onDraw(mouseX, mouseY, sr);
            button.setPosX((float)(this.animationUtil.getPosX() + 2.0));
        });
        RenderUtil.drawCircle(sr.getScaledWidth() - 50, sr.getScaledHeight() - 50, 25.0f, -14606047);
        this.mc.fontRendererObj.drawStringWithShadow("+", (float)(sr.getScaledWidth() - 50) + 13.0f - (float)this.mc.fontRendererObj.getStringWidth("+") / 2.0f, (float)(sr.getScaledHeight() - 50) + 13.0f - (float)this.mc.fontRendererObj.FONT_HEIGHT / 2.0f, -1);
        Moon.INSTANCE.getComponentManager().getComponentMap().values().forEach(component -> {
            component.onCompRender(sr);
            float nameHeight = (float)this.mc.fontRendererObj.FONT_HEIGHT + 2.0f;
            float nameWidth = this.mc.fontRendererObj.getStringWidth(component.getComponentName().replaceAll("[1-9]", ""));
            RenderUtil.drawRect(component.getX() - 1.0, component.getY() - 1.0, nameWidth + 1.0f, nameHeight, 0x67000000);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(component.getComponentName().replaceAll("[1-9]", ""), (float)component.getX(), (float)component.getY(), component.isEnabled ? -1 : -5592406);
            if (this.draggingHudComp == component && isDragging) {
                component.setX((double)mouseX + this.lastX);
                component.setY((double)mouseY + this.lastY);
                Moon.INSTANCE.getComponentManager().saveComps();
            }
        });
        Moon.INSTANCE.getCustomHudNotification().onDraw(sr);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Moon.INSTANCE.getComponentManager().getComponentMap().values().forEach(component -> {
            float nameHeight = (float)this.mc.fontRendererObj.FONT_HEIGHT + 2.0f;
            float nameWidth = this.mc.fontRendererObj.getStringWidth(component.getComponentName());
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, component.getX(), component.getY() - 1.0, nameWidth, nameHeight)) {
                if (mouseButton == 0) {
                    isDragging = true;
                    this.draggingHudComp = component;
                    this.lastX = component.getX() - (double)mouseX;
                    this.lastY = component.getY() - (double)mouseY;
                    Moon.INSTANCE.getComponentManager().saveComps();
                }
                if (mouseButton == 1) {
                    SettingsHud hud = new SettingsHud((Component)component);
                    hud.setDirectory(new File(Moon.INSTANCE.getComponentManager().getDirectory(), "menu"));
                    if (!hud.getDirectory().exists()) {
                        hud.getDirectory().mkdirs();
                    }
                    this.mc.displayGuiScreen(hud);
                }
                if (mouseButton == 2) {
                    component.isEnabled = !component.isEnabled;
                }
            }
        });
        for (AddButton button : this.buttons) {
            if (!MouseUtil.mouseWithinBounds(mouseX, mouseY, this.animationUtil.getPosX(), button.getPosY(), 96.0, 20.0)) continue;
            button.onRun();
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, sr.getScaledWidth() - 50, sr.getScaledHeight() - 50, 25.0, 25.0)) {
            this.isAdding = !this.isAdding;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            isDragging = false;
            this.draggingHudComp = null;
        }
    }

    public static boolean isDragging() {
        return isDragging;
    }
}

