/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.nostalgia;

import java.awt.Color;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.MathUtils;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;

public class NTargetHUDComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        if (KillAura.target != null) {
            int ping;
            EntityLivingBase entityLivingBase = KillAura.target;
            float ypos = (float)sr.getScaledHeight() / 2.0f + 60.0f;
            float xpos = sr.getScaledWidth() / 2 + 10;
            float width = 150 + this.mc.fontRendererObj.getStringWidth(entityLivingBase.getName());
            RenderUtil.drawBorderedRect(xpos, ypos, width, 47.0, 0.5, -5592406, -2013265920);
            boolean attacking = entityLivingBase.swingProgress > 0.0f;
            String attackingString = attacking ? "\u00a7atrue" : "\u00a74false";
            this.mc.fontRendererObj.drawStringWithShadow("Name: \u00a7b" + entityLivingBase.getName(), xpos + 3.0f, ypos + 4.0f, -1);
            NetworkPlayerInfo networkPlayerInfo = this.mc.getNetHandler().getPlayerInfo(entityLivingBase.getUniqueID());
            try {
                ping = networkPlayerInfo.getResponseTime();
            }
            catch (NullPointerException ex) {
                ping = 1;
            }
            this.mc.fontRendererObj.drawStringWithShadow("Ping: \u00a76" + ping, xpos + 3.0f, ypos + 15.0f, -1);
            this.mc.fontRendererObj.drawStringWithShadow("Direction: \u00a7e" + entityLivingBase.getHorizontalFacing(), xpos + 3.0f, ypos + 26.0f, -1);
            RenderUtil.drawRect(xpos + 3.0f, ypos + 37.0f, width - 20.0f, 4.5, Color.RED.brighter().brighter().getRGB());
            if (!Float.isNaN(entityLivingBase.getHealth())) {
                RenderUtil.drawRect(xpos + 3.0f, ypos + 37.0f, (width - 20.0f) * (entityLivingBase.getHealth() / entityLivingBase.getMaxHealth()), 4.5, Color.GREEN.brighter().brighter().getRGB());
                float f = entityLivingBase.getHealth();
                float f1 = entityLivingBase.getMaxHealth();
                float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
                String healthText = MathUtils.roundDouble(entityLivingBase.getHealth() / 2.0f, 1) + "";
                if (MathUtils.roundDouble(entityLivingBase.getHealth() / 2.0f, 1) == MathUtils.roundDouble(Math.round(entityLivingBase.getHealth()) / 2, 1)) {
                    healthText = Math.round(entityLivingBase.getHealth() / 2.0f) + "";
                }
                this.mc.fontRendererObj.drawStringWithShadow(healthText.replace(".", ","), xpos + (width - 15.0f), ypos + 37.0f, Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000);
            }
        }
        super.render(event);
    }
}

