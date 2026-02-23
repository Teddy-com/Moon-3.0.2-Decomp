/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.module.impl.visuals.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.moon.Moon;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;

public class RBuildComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        float negativity = 0.0f;
        if (HUD.informationMode.getValue() == HUD.InformationMode.XAVE) {
            negativity += 21.0f;
        }
        if (this.mc.ingameGUI.getChatGUI().getChatOpen()) {
            negativity += 14.0f;
        }
        switch (this.getReleaseBuildModes()) {
            case MOON: {
                if (HUD.mcFont.getValue().booleanValue()) {
                    this.mc.fontRendererObj.drawStringWithShadow("\u00a77Build: \u00a7f" + Moon.INSTANCE.getVersion() + "-" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77| UID:\u00a7f " + Moon.INSTANCE.uid, sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("\u00a77Build: \u00a7f" + Moon.INSTANCE.getVersion() + "-" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77| UID:\u00a7f " + Moon.INSTANCE.uid) - 2, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 1, -1);
                    break;
                }
                Fonts.moonThemeSF.drawStringWithShadow("\u00a77Build: \u00a7f" + Moon.INSTANCE.getVersion() + "-" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77| UID:\u00a7f " + Moon.INSTANCE.uid, sr.getScaledWidth() - Fonts.moonThemeSF.getStringWidth("\u00a77Build: \u00a7f" + Moon.INSTANCE.getVersion() + "-" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77| UID:\u00a7f " + Moon.INSTANCE.uid) - 2, sr.getScaledHeight() - Fonts.moonThemeSF.getHeight() - 2, -1);
                break;
            }
            case ASTOLFO: {
                Fonts.moonThemeSF.drawStringWithShadow("\u00a77" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77 - \u00a7f" + Moon.INSTANCE.getVersion() + " \u00a77- " + Moon.INSTANCE.username, sr.getScaledWidth() - Fonts.moonThemeSF.getStringWidth("\u00a77" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77 - \u00a7f" + Moon.INSTANCE.getVersion() + " \u00a77- " + Moon.INSTANCE.username) - 2, sr.getScaledHeight() - Fonts.moonThemeSF.getHeight() - 2, -1);
                break;
            }
            case REMIX: {
                String bottomRight = ChatFormatting.GRAY + Moon.INSTANCE.uid + " - \u00a7f" + (this.mc.getCurrentServerData() != null ? this.mc.getCurrentServerData().gameVersion : "Offline");
                Fonts.remixFont.drawStringWithShadow(bottomRight, sr.getScaledWidth() - Fonts.remixFont.getStringWidth(bottomRight) - 3, (float)(sr.getScaledHeight() - Fonts.remixFont.getHeight() - 4) - negativity, -1);
                break;
            }
            case NOVOLINE: {
                float basePosition = sr.getScaledHeight() - Fonts.novolineFont.getHeight() - 2;
                String buildNovoline = "\u00a77Build - \u00a7f" + Moon.INSTANCE.getVersion() + "-" + Moon.INSTANCE.getVersionType().getDisplay() + " \u00a77| UID - \u00a7f" + Moon.INSTANCE.uid;
                Fonts.novolineFont.drawStringWithShadow(buildNovoline, sr.getScaledWidth() - 2 - Fonts.novolineFont.getStringWidth(buildNovoline), basePosition - negativity, -1);
            }
        }
        super.render(event);
    }

    public HUD.ReleaseBuildMode getReleaseBuildModes() {
        return (HUD.ReleaseBuildMode)((Object)HUD.releaseBuildModes.getValue());
    }
}

