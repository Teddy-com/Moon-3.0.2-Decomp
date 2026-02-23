/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.nostalgia;

import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.gui.nostalgia.tab.TabGUI;
import me.moon.module.impl.visuals.components.Component;

public class NTabGuiComponent
extends Component {
    private TabGUI tabGUI = new TabGUI(4.0f, 20.0f, 92.0f);
    private boolean wasInitialized = false;

    @Override
    public void render(Render2DEvent event) {
        if (!this.wasInitialized) {
            this.tabGUI.init();
            this.wasInitialized = true;
        }
        this.tabGUI.onDraw(event.getScaledResolution());
        super.render(event);
    }

    @Override
    public void keyPress(KeyInputEvent event) {
        this.tabGUI.onKeyPress(event.getKey());
        super.keyPress(event);
    }

    @Override
    public void init() {
        super.init();
    }
}

