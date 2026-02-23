/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.moonvirtue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.components.Component;
import net.minecraft.client.Minecraft;

public class MVWatermarkComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm a");
        Calendar calendar = Calendar.getInstance();
        this.mc.fontRendererObj.drawStringWithShadow("Moon \u00a77(" + time.format(calendar.getTime()) + ")", 2.0f, 2.0f, -1);
        this.mc.fontRendererObj.drawStringWithShadow("\u00a77FPS: " + Minecraft.getDebugFPS() + " - MS: " + (this.mc.getCurrentServerData() == null ? 0L : this.mc.getCurrentServerData().pingToServer), 2.0f, 3 + this.mc.fontRendererObj.FONT_HEIGHT, -1);
        super.render(event);
    }
}

