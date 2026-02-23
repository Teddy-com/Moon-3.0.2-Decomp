/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.moon.gui.moderngui;

import java.io.IOException;
import java.util.ArrayList;
import me.moon.gui.ConfigGUI;
import me.moon.gui.moderngui.frame.Frame;
import me.moon.gui.moderngui.frame.impl.CategoryFrame;
import me.moon.module.Module;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class ModernClickGui
extends GuiScreen {
    private final ArrayList<Frame> frames = new ArrayList();
    public static CategoryFrame extendedFrame = null;
    private float alpha = 0.0f;

    public void init() {
        int x = 2;
        int y = 2;
        for (Module.Category moduleCategory : Module.Category.values()) {
            this.getFrames().add(new CategoryFrame(moduleCategory, (float)x, (float)y, 110.0f, 24.0f));
            if (x + 235 >= new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                x = 2;
                y += 20;
                continue;
            }
            x += 115;
        }
        this.getFrames().forEach(Frame::init);
    }

    public void onBloom() {
        this.getFrames().forEach(Frame::bloom);
    }

    public void onBlur() {
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -1);
        this.getFrames().forEach(Frame::blur);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.mc.fromMainMenu) {
            RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -15527149);
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.drawBorderedRect(sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0, 0.5, -15374912, -14606047);
        Fonts.buttonFont.drawStringWithShadow("Config Menu", (float)(sr.getScaledWidth() - 51) - (float)Fonts.buttonFont.getStringWidth("Config Menu") / 2.0f, sr.getScaledHeight() - 16, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0) && Mouse.isButtonDown((int)0)) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGUI());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.getFrames().forEach(frame -> frame.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void keyTyped(char character, int keyCode) throws IOException {
        super.keyTyped(character, keyCode);
        if (extendedFrame != null) {
            extendedFrame.keyTyped(character, keyCode);
            return;
        }
        this.getFrames().forEach(frame -> frame.keyTyped(character, keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.getFrames().forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.getFrames().forEach(frame -> frame.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<Frame> getFrames() {
        return this.frames;
    }
}

