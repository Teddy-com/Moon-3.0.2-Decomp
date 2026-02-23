/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.moon.gui.materialui;

import java.io.IOException;
import me.moon.Moon;
import me.moon.gui.ConfigGUI;
import me.moon.gui.materialui.plane.impl.MainPlane;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class MaterialUI
extends GuiScreen {
    private MainPlane mainPlane = null;
    private AnimationUtil util = new AnimationUtil(0.0, 0.0);

    public void initializedUI() {
        if (this.mainPlane == null) {
            this.mainPlane = new MainPlane("Moon " + Moon.INSTANCE.getVersion(), 2.0f, 2.0f, 395.0f, 315.0f);
            this.mainPlane.initializePlane();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.mc.fromMainMenu) {
            RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -15527149);
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        ClickGui clickGui = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGui");
        RenderUtil.drawBorderedRect(sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0, 0.5, -15374912, -14606047);
        Fonts.buttonFont.drawStringWithShadow("Config Menu", (float)(sr.getScaledWidth() - 51) - (float)Fonts.buttonFont.getStringWidth("Config Menu") / 2.0f, sr.getScaledHeight() - 16, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0) && Mouse.isButtonDown((int)0)) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGUI());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mainPlane.onDrawScreen(mouseX, mouseY, partialTicks);
        this.util.interpolate(((Integer)clickGui.blurRadius.getValue()).intValue(), 0.0, 5.0f / (float)Minecraft.getDebugFPS());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.mainPlane.onKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.mainPlane.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.mainPlane.onMouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.mainPlane.onGuiClosed();
        this.util.setPosX(0.0);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        super.onResize(mcIn, p_175273_2_, p_175273_3_);
        if (this.mainPlane.getPosX() + this.mainPlane.getWidth() > (float)p_175273_2_) {
            this.mainPlane.setPosX((float)p_175273_2_ - this.mainPlane.getWidth());
        }
        if (this.mainPlane.getPosY() + this.mainPlane.getHeight() > (float)p_175273_3_) {
            this.mainPlane.setPosY((float)p_175273_3_ - this.mainPlane.getHeight());
        }
    }
}

