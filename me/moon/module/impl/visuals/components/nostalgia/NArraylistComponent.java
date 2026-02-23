/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.module.impl.visuals.components.nostalgia;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.Moon;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NArraylistComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        ArrayList<Module> modules = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        modules.sort(Comparator.comparingDouble(module -> -this.mc.fontRendererObj.getStringWidth(this.getModuleString((Module)module))));
        modules.removeIf(Module::isHidden);
        float yPos = 2.0f;
        for (Module module2 : modules) {
            if (Float.isNaN(module2.offset)) {
                module2.offset = 0.0f;
            }
            if (!module2.isEnabled() && module2.offset == 0.0f) continue;
            int color = RenderUtil.getRainbow(4500, (int)(-(yPos * 15.0f)), 0.33f);
            String moduleString = this.getModuleString(module2);
            RenderUtil.drawRect(module2.isEnabled() ? (double)((float)sr.getScaledWidth() - (float)this.mc.fontRendererObj.getStringWidth(moduleString) - 7.5f + module2.offset) : (double)((float)(sr.getScaledWidth() + 7) + module2.offset), yPos - 1.0f, this.mc.fontRendererObj.getStringWidth(moduleString) + 9, 11.0, -2013265920);
            RenderUtil.drawRect((float)(sr.getScaledWidth() - 2) + (module2.isEnabled() ? module2.offset : -module2.offset), (int)yPos - 1, 2.0, 11.0, color);
            this.mc.fontRendererObj.drawStringWithShadow(moduleString, module2.isEnabled() ? (float)sr.getScaledWidth() - (float)this.mc.fontRendererObj.getStringWidth(moduleString) + module2.offset - 5.0f : (float)sr.getScaledWidth() + module2.offset + 4.0f, yPos + 0.5f, color);
            yPos += module2.isEnabled() ? 11.0f - NArraylistComponent.map(module2.offset, 0.0f, this.mc.fontRendererObj.getStringWidth(moduleString), 0.0f, 11.0f) : -NArraylistComponent.map(module2.offset, 0.0f, this.mc.fontRendererObj.getStringWidth(moduleString), 0.0f, 11.0f);
            float difference = module2.offset < 0.0f ? 0.0f - module2.offset : module2.offset - 0.0f;
            float xd = Minecraft.getDebugFPS();
            xd = difference / xd * 100.0f;
            if (module2.offset > 0.0f) {
                module2.offset -= xd / 15.0f;
            }
            if (module2.offset < 0.0f) {
                module2.offset += xd / 15.0f;
            }
            if (modules.indexOf(module2) != modules.size() - 1) continue;
            HUD.offset = yPos;
        }
        super.render(event);
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
}

