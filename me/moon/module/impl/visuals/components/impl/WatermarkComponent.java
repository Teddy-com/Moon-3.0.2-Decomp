/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WatermarkComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        String clientName = ((String)HUD.clientName.getValue()).replaceAll("&", "\u00a7");
        switch (this.getWatermarkMode()) {
            case MOON: {
                if (HUD.mcFont.getValue().booleanValue()) {
                    this.mc.fontRendererObj.drawStringWithShadow(clientName, 2.0f, 4.0f, this.getColor());
                    break;
                }
                Fonts.moon.drawStringWithShadow(clientName, 2.0, 4.0, this.getColor());
                break;
            }
            case FLUX: {
                Fonts.fluxURW.drawStringWithShadow(clientName, 3.0, 3.0, RenderUtil.getRainbow(3000, 10, 0.5f));
                break;
            }
            case JELLO: {
                GL11.glPushMatrix();
                GL11.glEnable((int)3042);
                GL11.glDisable((int)3008);
                RenderUtil.drawImage(new ResourceLocation("textures/client/arraylistshadow.png"), 9.0f + (float)Fonts.jelloRegularBig.getStringWidth((String)HUD.clientName.getValue()) / 2.0f - 52.5f, 3.0f, Fonts.jelloRegularBig.getStringWidth((String)HUD.clientName.getValue()), 40);
                GL11.glPopMatrix();
                Fonts.jelloRegularBig.drawString((String)HUD.clientName.getValue(), 9.0f, 8.0f, -1428300323);
                Fonts.jelloRegular.drawString("3.0", 9.5f, 32.0f, -1428300323);
                GL11.glEnable((int)3008);
                break;
            }
            case REMIX: {
                Fonts.remixFont.drawStringWithShadow(clientName, 4.0, 6.0, this.getColor());
                break;
            }
            case ASTOLFO: {
                Fonts.astolfoFont.drawStringWithShadow(clientName, 2.0, 4.0, this.getColor());
                break;
            }
            case NOVOLINE: {
                LocalDateTime date = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                Fonts.novolineFont.drawStringWithShadow(clientName + " \u00a77(\u00a7f" + formatter.format(date) + "\u00a77)", 2.0, 3.0, this.getColor());
                break;
            }
            case XAVE: {
                MCBlurUtil.drawBLURRRR(0, 0, Fonts.fontESP.getStringWidth(clientName) + 2, Fonts.fontESP.getHeight() + 7, 20.0f);
                RenderUtil.drawRect(0.0, 0.0, Fonts.fontESP.getStringWidth(clientName) + 2, Fonts.fontESP.getHeight() + 7, 0x67000000);
                Fonts.fontESP.drawStringWithShadow(clientName, 2.0, 4.0, -1);
            }
        }
        super.render(event);
    }

    @Override
    public void blur() {
        String clientName = ((String)HUD.clientName.getValue()).replaceAll("&", "\u00a7");
        switch (this.getWatermarkMode()) {
            case XAVE: {
                RenderUtil.drawRect(0.0, 0.0, Fonts.fontESP.getStringWidth(clientName) + 2, Fonts.fontESP.getHeight() + 7, 0x67000000);
            }
        }
    }

    public HUD.WatermarkMode getWatermarkMode() {
        return (HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue());
    }
}

