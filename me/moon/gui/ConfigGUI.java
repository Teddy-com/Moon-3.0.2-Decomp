/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.config.Config;
import me.moon.gui.GuiLoginField;
import me.moon.gui.config.ConfigObject;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ConfigGUI
extends GuiScreen {
    private int x;
    private int y;
    private int currentScrolledY;
    private AnimationUtil animation = new AnimationUtil(0.0, 0.0);
    private int xDiff = 300;
    private int scrollvalue = 0;
    private boolean isOverLoad;
    private boolean isOverDelete;
    private boolean isOverSave;
    private int yDiff = 100;
    private GuiTextField textField;
    private GuiLoginField searchTextField;
    private Config hoveredConfig;
    private Config loadedConfig;
    private boolean wasMouseReleased;
    private String currentMenu = "Offline";
    private String searchedString;
    private boolean isSearching;
    public static ArrayList<ConfigObject> configObjects = new ArrayList();

    @Override
    public void initGui() {
        Moon.INSTANCE.getConfigManager().getConfigs().clear();
        Moon.INSTANCE.getConfigManager().load();
        this.buttonList.clear();
        this.x = this.width / 2;
        this.y = this.height / 2;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.textField = new GuiTextField(1336, this.mc.fontRendererObj, this.x - this.xDiff + 117, this.y - this.yDiff - 23, 100, 16);
        this.searchTextField = new GuiLoginField(1337, this.mc.fontRendererObj, this.width / 2 - 50, this.height / 2 - 106, 100, 20);
        this.searchedString = "Waiting...";
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();
        if (var1 != 0) {
            if (var1 > 1) {
                var1 = 1;
            }
            if (var1 < -1) {
                var1 = -1;
            }
            if (!ConfigGUI.isShiftKeyDown()) {
                var1 *= this.mc.fontRendererObj.FONT_HEIGHT + 6;
            }
            if (this.currentScrolledY <= 0) {
                this.currentScrolledY += this.currentScrolledY == 0 ? -1 : var1 / 2;
            }
            if (this.currentScrolledY >= 0) {
                this.currentScrolledY = -1;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.textField.textboxKeyTyped(typedChar, keyCode);
        this.searchTextField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    private void drawBackground(int mouseX, int mouseY) {
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("HUD");
        double xPos = (float)this.width / 2.0f - 150.0f;
        double yPos = (float)this.height / 2.0f - 105.0f;
        if (!this.mc.gameSettings.ofFastRender) {
            MCBlurUtil.drawBLURRRR(0, 0, this.width, this.height, 12.0f);
        }
        RenderUtil.drawRect(xPos, yPos, 300.0, 205.0, -15374912);
        RenderUtil.drawGradient2(xPos + 0.5, yPos + 0.5, xPos + 299.5, yPos + 204.5, -16512705, -16777216);
    }

    public static void bloom() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof ConfigGUI)) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float xPos = (float)sr.getScaledWidth() / 2.0f - 150.0f;
        float yPos = (float)sr.getScaledHeight() / 2.0f - 105.0f;
        RenderUtil.drawRect(xPos, yPos, 300.0, 204.0, -1);
    }

    private void drawContents(int mouseX, int mouseY) {
        this.currentMenu = "Offline";
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            wheel = wheel > 0 ? 32 : -32;
            this.scrollvalue = Math.min(Math.max(this.scrollvalue + wheel, -(Moon.INSTANCE.getConfigManager().getConfigs().size() * 15) + 185), 0);
        }
        this.scrollvalue = Math.min(Math.max(this.scrollvalue + wheel, -(Moon.INSTANCE.getConfigManager().getConfigs().size() * 15) + 185), 0);
        double xPos = (float)this.width / 2.0f - 148.0f;
        double yPos = (float)this.height / 2.0f - 85.0f + (float)this.scrollvalue;
        ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(sr, (float)xPos - 1.0f, (float)this.height / 2.0f - 85.0f, 298.0f, 185.0f);
        if (this.currentMenu.equals("Offline")) {
            for (Config config : Moon.INSTANCE.getConfigManager().getConfigs()) {
                if (!config.getName().contains(this.searchTextField.getText())) continue;
                boolean isInMenu = MouseUtil.mouseWithinBounds(mouseX, mouseY, (float)xPos - 1.0f, (float)this.height / 2.0f - 80.0f, 298.0, 185.0);
                boolean hoveredConfig = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos, yPos, 267.0, 14.0) && isInMenu;
                boolean hoveredSave = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos + 268.0, yPos, 14.0, 14.0) && isInMenu;
                boolean hoveredRemove = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos + 283.0, yPos, 14.0, 14.0) && isInMenu;
                RenderUtil.drawRect(xPos - 1.0, yPos, 1.0, 14.0, -15374912);
                RenderUtil.drawRect(xPos, yPos, 267.0, 14.0, hoveredConfig ? new Color(25, 25, 25).brighter().getRGB() : new Color(25, 25, 25).getRGB());
                RenderUtil.drawRect(xPos + 268.0, yPos, 14.0, 14.0, hoveredSave ? new Color(3, 204, 0).brighter().getRGB() : new Color(3, 204, 0).getRGB());
                RenderUtil.drawRect(xPos + 283.0, yPos, 14.0, 14.0, hoveredRemove ? new Color(-56798).brighter().brighter().getRGB() : -56798);
                Fonts.sectioNormal.drawStringWithShadow(config.getName(), xPos + 2.0, yPos + 4.5, -1);
                Fonts.iconFont4.drawCenteredStringWithShadow("a", (float)xPos + 275.0f, (float)yPos + 5.0f, -1);
                Fonts.iconFont3.drawCenteredStringWithShadow("d", (float)xPos + 290.0f, (float)yPos + 4.5f, -1);
                yPos += 15.0;
            }
        }
        GL11.glDisable((int)3089);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.searchTextField.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            double xPos = (float)this.width / 2.0f - 150.0f;
            double yPos = (float)this.height / 2.0f - 105.0f;
            boolean offlineHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos + 2.0, yPos + 2.0, 50.0, 12.0);
            boolean onlineHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos + 54.0, yPos + 2.0, 50.0, 12.0);
            if (offlineHovered) {
                this.currentMenu = "Offline";
            }
            if (onlineHovered) {
                this.currentMenu = "Online";
                this.searchedString = "\u00a7aReloaded Online Configs...";
            }
            if (this.currentMenu.equals("Offline")) {
                double xPos1 = (float)this.width / 2.0f - 148.0f;
                double yPos1 = (float)this.height / 2.0f - 85.0f + (float)this.scrollvalue;
                ScaledResolution sr = new ScaledResolution(this.mc);
                if (!MouseUtil.mouseWithinBounds(mouseX, mouseY, (float)xPos - 1.0f, (float)this.height / 2.0f - 85.0f, 298.0, 185.0)) {
                    return;
                }
                for (Config config : Moon.INSTANCE.getConfigManager().getConfigs()) {
                    if (!config.getName().contains(this.searchTextField.getText())) continue;
                    boolean hoveredConfig = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos1, yPos1, 267.0, 14.0);
                    boolean hoveredSave = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos1 + 268.0, yPos1, 14.0, 14.0);
                    boolean hoveredRemove = MouseUtil.mouseWithinBounds(mouseX, mouseY, xPos1 + 283.0, yPos1, 14.0, 14.0);
                    if (hoveredConfig) {
                        Moon.INSTANCE.getConfigManager().loadConfig(config.getName(), false);
                        Moon.INSTANCE.getConfigManager().getConfigs().clear();
                        Moon.INSTANCE.getConfigManager().load();
                        this.searchedString = "\u00a7aLoaded Config called " + config.getName() + "...";
                        return;
                    }
                    if (hoveredSave) {
                        Moon.INSTANCE.getConfigManager().saveConfig(config.getName(), true);
                        Moon.INSTANCE.getConfigManager().getConfigs().clear();
                        Moon.INSTANCE.getConfigManager().load();
                        this.searchedString = "\u00a79Saved Config called " + config.getName() + "...";
                        return;
                    }
                    if (hoveredRemove) {
                        this.searchedString = "\u00a7cDeleted Config called " + config.getName() + "...";
                        Moon.INSTANCE.getConfigManager().deleteConfig(config.getName());
                        Moon.INSTANCE.getConfigManager().getConfigs().clear();
                        Moon.INSTANCE.getConfigManager().load();
                        return;
                    }
                    yPos1 += 15.0;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean showOnlySearch() {
        return this.isSearching && this.searchedString != null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.animation.interpolate(0.0, this.currentScrolledY, 20 / Minecraft.getDebugFPS());
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.drawBackground(mouseX, mouseY);
        this.drawContents(mouseX, mouseY);
        this.searchTextField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
        this.searchTextField.updateCursorCounter();
        super.updateScreen();
    }

    public static boolean isMouseHover(double mouseX, double mouseY, double minX, double minY, double maxX, double maxY) {
        return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
    }
}

