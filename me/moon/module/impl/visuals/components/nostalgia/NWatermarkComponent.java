/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.nostalgia;

import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class NWatermarkComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        int color = RenderUtil.getRainbow(4500, -30, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2, 1.2, 1.0);
        this.mc.fontRendererObj.drawStringWithShadow("M\u00a77oon \u00a78-", 4.0f, 4.0f, color);
        GlStateManager.popMatrix();
        this.mc.fontRendererObj.drawStringWithShadow(" \u00a77FPS \u00a78[\u00a77" + Minecraft.getDebugFPS() + "\u00a78]", 46.0f, 7.0f, -1);
    }
}

