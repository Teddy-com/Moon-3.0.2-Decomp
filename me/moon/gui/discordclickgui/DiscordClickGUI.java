/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.moon.gui.discordclickgui;

import java.io.IOException;
import java.util.ArrayList;
import me.moon.gui.ConfigGUI;
import me.moon.gui.discordclickgui.frame.Frame;
import me.moon.gui.discordclickgui.frame.frames.DiscordFrame;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class DiscordClickGUI
extends GuiScreen {
    private ArrayList<Frame> frames = new ArrayList();

    public void init() {
        this.frames.add(new DiscordFrame("Moon", 2.0f, 2.0f, 288.0f, 290.0f));
        this.frames.forEach(Frame::init);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.mc.fromMainMenu) {
            RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -15527149);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawBorderedRect(sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0, 0.5, -15374912, -14606047);
        Fonts.buttonFont.drawStringWithShadow("Config Menu", (float)(sr.getScaledWidth() - 51) - (float)Fonts.buttonFont.getStringWidth("Config Menu") / 2.0f, sr.getScaledHeight() - 16, -1);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, sr.getScaledWidth() - 102, sr.getScaledHeight() - 22, 100.0, 20.0) && Mouse.isButtonDown((int)0)) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGUI());
        }
        this.frames.forEach(frame -> frame.drawScreen(mouseX, mouseY, sr));
    }

    public void onBloom() {
        this.frames.forEach(Frame::onBloom);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.frames.forEach(frame -> frame.keyTyped(typedChar, keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.frames.forEach(frame -> frame.setDragging(false));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

