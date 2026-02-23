/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.autism;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.Moon;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AutismTheme
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Color logoColor = RenderUtil.getGradientOffset(HUD.colorValue.getColor(), new Color(0, 0, 0), (double)(-(2.0f / (float)Fonts.autismFont.getHeight() / 18.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/client/genesis/genesis.png"));
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture2(3.5f, 3.5f, 0.0f, 81.0f, 81.0f, 81.0f, 81.0f, 81.0f);
        GL11.glColor4f((float)((float)logoColor.getRed() / 255.0f), (float)((float)logoColor.getGreen() / 255.0f), (float)((float)logoColor.getBlue() / 255.0f), (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture2(4.0f, 4.0f, 0.0f, 80.0f, 80.0f, 80.0f, 80.0f, 80.0f);
        GlStateManager.popMatrix();
        ArrayList<Module> modules = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        modules.sort(Comparator.comparingDouble(module -> -Fonts.autismFont.getStringWidth(this.getModuleString((Module)module))));
        modules.removeIf(Module::isHidden);
        modules.removeIf(module -> !module.isEnabled());
        float posY = 2.0f;
        for (Module module2 : modules) {
            String renderName = this.getModuleString(module2);
            int color = RenderUtil.getGradientOffset(HUD.colorValue.getColor(), new Color(0, 0, 0), (double)(-(posY / (float)Fonts.autismFont.getHeight() / 18.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
            float width = Fonts.autismFont2.getStringWidth(renderName) - 1;
            float height = this.mc.fontRendererObj.FONT_HEIGHT;
            float posX = (float)sr.getScaledWidth() - width - 2.0f;
            Fonts.autismFont.drawString(renderName, posX, posY, color);
            posY += height + 3.0f;
            if (modules.indexOf(module2) != modules.size() - 1) continue;
            HUD.offset = posY;
        }
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }
}

