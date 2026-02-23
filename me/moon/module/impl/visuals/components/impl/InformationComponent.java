/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.Animation;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class InformationComponent
extends Component {
    private Animation abraxasAnimation = new Animation();
    private Animation abraxasAnimation2 = new Animation();
    private Minecraft mc = Minecraft.getMinecraft();
    private int colorMarker;
    private int centerX;
    private int colorDirection;
    public int tintMarker;
    public int tintDirection;
    private float offsetAll;
    public int details;

    @Override
    public void render(Render2DEvent event) {
        int yPos = 20;
        ScaledResolution sr = event.getScaledResolution();
        double motionX = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        double motionZ = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        switch (this.getInformationMode()) {
            case FLUX: {
                if (this.mc.thePlayer == null) break;
                for (int i = 1; i <= 5; ++i) {
                    boolean height;
                    ItemStack ia = this.mc.thePlayer.getEquipmentInSlot(5 - i);
                    if (ia == null) continue;
                    GlStateManager.pushMatrix();
                    RenderHelper.enableStandardItemLighting();
                    boolean width = height = false;
                    float rwidth = (float)sr.getScaledWidth() / 2.0f + 7.0f + (float)(17 * (i - 1));
                    this.mc.getRenderItem().renderItemIntoGUI(ia, rwidth, sr.getScaledHeight() - 57);
                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, ia, rwidth, sr.getScaledHeight() - 57);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popMatrix();
                }
                break;
            }
            case JELLO: {
                break;
            }
            case MOON: {
                String coordinateString = Math.round(this.mc.thePlayer.posX) + " " + Math.round(this.mc.thePlayer.posY) + " " + Math.round(this.mc.thePlayer.posZ);
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                if (HUD.mcFont.getValue().booleanValue()) {
                    this.mc.fontRendererObj.drawStringWithShadow(coordinateString, 2.0f, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 2, -1);
                    break;
                }
                Fonts.moon.drawStringWithShadow(coordinateString, 2.0, sr.getScaledHeight() - Fonts.moon.getHeight() - 2, -1);
                break;
            }
            case REMIX: {
                InformationComponent informationComponent = this;
                String bottomLeft = "\u00a7fXYZ:  \u00a77" + Math.round(this.mc.thePlayer.posX) + ", " + Math.round(this.mc.thePlayer.posY) + ", " + Math.round(this.mc.thePlayer.posZ) + "  \u00a7fFPS:  \u00a77" + informationComponent.mc.getDebugFPS();
                Fonts.remixFont.drawStringWithShadow(bottomLeft, 2.0, sr.getScaledHeight() - Fonts.remixFont.getHeight() - 4 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 13 : 0), -1);
                break;
            }
            case NOVOLINE: {
                InformationComponent informationComponent = this;
                String fpsNovoline = "FPS: \u00a7f" + informationComponent.mc.getDebugFPS();
                String bpsNovoline = "Speed: \u00a7f" + MathUtils.roundDouble(Math.hypot(motionX, motionZ) * 20.0 * (double)this.mc.timer.timerSpeed, 2) + " b/s";
                String xyzNovoline = "XYZ: \u00a7f" + Math.round(this.mc.thePlayer.posX) + " " + Math.round(this.mc.thePlayer.posY) + " " + Math.round(this.mc.thePlayer.posZ);
                float basePosition = sr.getScaledHeight() - Fonts.novolineFont.getHeight() - 2;
                Fonts.novolineFont.drawStringWithShadow(xyzNovoline, 2.0, basePosition, this.getColor());
                Fonts.novolineFont.drawStringWithShadow(bpsNovoline, 2.0, basePosition - 10.0f, this.getColor());
                Fonts.novolineFont.drawStringWithShadow(fpsNovoline, 2.0, basePosition - 19.0f, this.getColor());
                break;
            }
            case ASTOLFO: {
                String tps = "\u00a7fPing: \u00a77" + (this.mc.getCurrentServerData() == null ? 0L : this.mc.getCurrentServerData().pingToServer) + "ms";
                InformationComponent informationComponent = this;
                String fps = "FPS: " + informationComponent.mc.getDebugFPS();
                String bps = MathUtils.roundDouble(Math.hypot(motionX, motionZ) * (double)this.mc.timer.timerSpeed * 20.0, 2) + " blocks/sec";
                String coords = Math.round(this.mc.thePlayer.posX) + " " + Math.round(this.mc.thePlayer.posY) + " " + Math.round(this.mc.thePlayer.posZ);
                Fonts.astolfoFont.drawStringWithShadow(coords, 1.0, sr.getScaledHeight() - Fonts.astolfoFont.getHeight() - 2 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0), -1);
                if (!this.mc.ingameGUI.getChatGUI().getChatOpen()) {
                    Fonts.astolfoFont.drawStringWithShadow(fps, 1.0, sr.getScaledHeight() - Fonts.astolfoFont.getHeight() * 3 - 6 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0), -1);
                }
                Fonts.astolfoFont.drawStringWithShadow(bps, 1.0, sr.getScaledHeight() - Fonts.astolfoFont.getHeight() * 2 - 4 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0), -1);
                break;
            }
            case XAVE: {
                Fonts.fontESP.drawStringWithShadow("M", 2.0, event.getScaledResolution().getScaledHeight() - Fonts.fontESP.getHeight() - 3 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), -1);
                InformationComponent informationComponent = this;
                Fonts.hudNormal.drawStringWithShadow("\u00a77FPS: \u00a7f" + informationComponent.mc.getDebugFPS() + " \u00a77Ping: \u00a7f1", 20.0, (float)(event.getScaledResolution().getScaledHeight() - Fonts.hudNormal.getHeight()) - 11.5f - (float)(this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), -1);
                Fonts.hudNormal.drawStringWithShadow("\u00a77X\u00a77: \u00a7f" + Math.round(this.mc.thePlayer.posX) + " \u00a77Y: \u00a7f" + Math.round(this.mc.thePlayer.posY) + " \u00a77Z: \u00a7f" + Math.round(this.mc.thePlayer.posZ), 20.0, (float)(event.getScaledResolution().getScaledHeight() - Fonts.hudNormal.getHeight()) - 1.5f - (float)(this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), -1);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yy");
                Calendar calendar = Calendar.getInstance();
                Fonts.hudNormal.drawStringWithShadow(format.format(calendar.getTime()), (double)(event.getScaledResolution().getScaledWidth() - Fonts.hudNormal.getStringWidth(format.format(calendar.getTime()))) - 3.5 - (double)((float)Fonts.hudNormal.getStringWidth(format2.format(calendar.getTime())) / 4.0f), event.getScaledResolution().getScaledHeight() - Fonts.hudNormal.getHeight() - 12 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), -1);
                Fonts.hudNormal.drawStringWithShadow(format2.format(calendar.getTime()), event.getScaledResolution().getScaledWidth() - Fonts.hudNormal.getStringWidth(format2.format(calendar.getTime())) - 6, event.getScaledResolution().getScaledHeight() - Fonts.hudNormal.getHeight() - 2 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), -1);
                RenderUtil.drawRect(event.getScaledResolution().getScaledWidth() - 3, event.getScaledResolution().getScaledHeight() - 21 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 15 : 0), 3.0, 22.0, -2046820353);
            }
        }
        super.render(event);
    }

    public HUD.InformationMode getInformationMode() {
        return (HUD.InformationMode)((Object)HUD.informationMode.getValue());
    }
}

