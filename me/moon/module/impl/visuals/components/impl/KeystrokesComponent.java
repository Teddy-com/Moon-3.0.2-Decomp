/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.impl;

import java.util.ArrayList;
import java.util.Iterator;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.module.impl.visuals.components.keystrokes.Keystroke;
import net.minecraft.client.gui.ScaledResolution;

public class KeystrokesComponent
extends Component {
    private ArrayList<Keystroke> keystrokes = new ArrayList();

    @Override
    public void bloom() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        switch (this.getMode()) {
            case JELLO: {
                this.keystrokes.forEach(Keystroke::bloom);
            }
        }
        super.bloom();
    }

    @Override
    public void blur() {
        switch (this.getMode()) {
            case JELLO: {
                this.keystrokes.forEach(Keystroke::blur);
            }
        }
        super.blur();
    }

    @Override
    public void update() {
        switch (this.getMode()) {
            case JELLO: {
                this.keystrokes.forEach(Keystroke::update);
            }
        }
        super.update();
    }

    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        if (this.keystrokes.isEmpty()) {
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindForward.getKeyCode(), 35.0f, 135.0f, 28.0f, 28.0f));
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindBack.getKeyCode(), 35.0f, 165.0f, 28.0f, 28.0f));
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindLeft.getKeyCode(), 5.0f, 165.0f, 28.0f, 28.0f));
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindRight.getKeyCode(), 65.0f, 165.0f, 28.0f, 28.0f));
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindAttack.getKeyCode(), 5.0f, 195.0f, 43.0f, 28.0f));
            this.keystrokes.add(new Keystroke(this.mc.gameSettings.keyBindUseItem.getKeyCode(), 50.0f, 195.0f, 43.0f, 28.0f));
        }
        switch (this.getMode()) {
            case JELLO: {
                this.keystrokes.forEach(Keystroke::render);
            }
        }
        block3 : switch (this.getTabGUIMode()) {
            case JELLO: 
            case MOON: {
                switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                    case JELLO: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(5.0f);
                        }
                        break block3;
                    }
                    case NONE: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(-25.0f);
                        }
                        break block3;
                    }
                    default: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(-15.0f);
                        }
                        break block3;
                    }
                }
            }
            case COMPACT: {
                switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                    case JELLO: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(15.0f);
                        }
                        break block3;
                    }
                    case NONE: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(-30.0f);
                        }
                        break block3;
                    }
                    default: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(-5.0f);
                        }
                        break block3;
                    }
                }
            }
            case NONE: {
                block16 : switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                    case JELLO: {
                        for (Keystroke keystroke : this.keystrokes) {
                            keystroke.setOffsetY(-85.0f);
                        }
                        break block3;
                    }
                    default: {
                        Iterator<Keystroke> iterator = this.keystrokes.iterator();
                        while (iterator.hasNext()) {
                            Keystroke keystroke = iterator.next();
                            keystroke.setOffsetY(-110.0f);
                            break block16;
                        }
                        break block3;
                    }
                }
                {
                    continue;
                    break;
                }
            }
        }
        super.render(event);
    }

    @Override
    public void keyPress(KeyInputEvent event) {
        switch (this.getMode()) {
            case JELLO: {
                this.keystrokes.stream().filter(keystroke -> keystroke.getKey() == event.getKey()).forEach(Keystroke::onKey);
            }
        }
        super.keyPress(event);
    }

    public HUD.KeystrokesMode getMode() {
        return (HUD.KeystrokesMode)((Object)HUD.keystrokesMode.getValue());
    }

    public HUD.TabGUIMode getTabGUIMode() {
        return (HUD.TabGUIMode)((Object)HUD.tabGuiModes.getValue());
    }
}

