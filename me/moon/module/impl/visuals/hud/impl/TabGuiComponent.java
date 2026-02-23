/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl;

import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.hud.Component;
import net.minecraft.client.gui.ScaledResolution;

public class TabGuiComponent
extends Component {
    int height = 0;

    public TabGuiComponent(String name) {
        super(name, 1.0, 1.0);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        int width = 100;
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
        float offsetY = (float)this.getY();
        for (Module.Category category : Module.Category.values()) {
            offsetY += 20.0f;
            this.height += 20;
        }
    }
}

