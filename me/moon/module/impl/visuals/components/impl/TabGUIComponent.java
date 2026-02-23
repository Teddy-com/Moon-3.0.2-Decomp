/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.impl;

import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.gui.tab.TabGUI;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.module.impl.visuals.hud.impl.tabgui.jello.TabGuiJello;
import me.moon.module.impl.visuals.hud.impl.tabgui.moon.TabGuiMoon;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;

public class TabGUIComponent
extends Component {
    private TabGuiJello jelloTabGUi;
    private TabGuiMoon moonTabGui;
    private TabGUI tabGUI;

    @Override
    public void init() {
        this.jelloTabGUi = new TabGuiJello(9.0f, 50.0f, 75.0f);
        this.jelloTabGUi.init();
        this.moonTabGui = new TabGuiMoon(5.0f, 20.0f, 75.0f);
        this.moonTabGui.init();
    }

    @Override
    public void blur() {
        switch (this.getTabGUIMode()) {
            case COMPACT: {
                if (this.tabGUI == null) break;
                this.tabGUI.blur();
                break;
            }
            case JELLO: {
                RenderUtil.drawRect(this.jelloTabGUi.getPosX(), this.jelloTabGUi.getPosY() - 4.0f, this.jelloTabGUi.getWidth(), (Fonts.jelloLight.getHeight() + 6) * 5, -1);
                this.jelloTabGUi.blur();
                break;
            }
            case MOON: {
                RenderUtil.drawRect(this.moonTabGui.getPosX(), this.moonTabGui.getPosY() - 4.0f, this.moonTabGui.getWidth(), (Fonts.moonLittleBigger.getHeight() + 6) * 5, -1);
                this.moonTabGui.blur();
            }
        }
    }

    @Override
    public void bloom() {
        switch (this.getTabGUIMode()) {
            case COMPACT: {
                if (this.tabGUI == null) break;
                this.tabGUI.blur();
                break;
            }
            case JELLO: {
                RenderUtil.drawRect(this.jelloTabGUi.getPosX(), this.jelloTabGUi.getPosY() - 4.0f, this.jelloTabGUi.getWidth(), (Fonts.jelloLight.getHeight() + 6) * 5, -1);
                this.jelloTabGUi.blur();
                break;
            }
            case MOON: {
                RenderUtil.drawRect(this.moonTabGui.getPosX(), this.moonTabGui.getPosY() - 4.0f, this.moonTabGui.getWidth(), (Fonts.moonLittleBigger.getHeight() + 6) * 5, -1);
                this.moonTabGui.blur();
            }
        }
        super.bloom();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void render(Render2DEvent event) {
        if (this.moonTabGui != null && this.jelloTabGUi != null) {
            switch (this.getTabGUIMode()) {
                case COMPACT: {
                    if (this.tabGUI == null) {
                        this.tabGUI = new TabGUI(0.0f, 17.0f, 70.0f);
                        this.tabGUI.init();
                        this.tabGUI.setPosY(20.0f);
                    }
                    this.tabGUI.onDraw(event.getScaledResolution());
                    switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                        case NOVOLINE: {
                            if (this.tabGUI.getPosY() == 16.0f) break;
                            this.tabGUI.setPosY(16.0f);
                            break;
                        }
                        case ASTOLFO: {
                            if (this.tabGUI.getPosY() == 15.0f) break;
                            this.tabGUI.setPosY(15.0f);
                            break;
                        }
                        case MOON: {
                            if (this.tabGUI.getPosY() == (float)(HUD.mcFont.getValue() != false ? 17 : 15)) break;
                            this.tabGUI.setPosY(HUD.mcFont.getValue() != false ? 17 : 15);
                            break;
                        }
                        case REMIX: {
                            if (this.tabGUI.getPosY() == 18.0f) break;
                            this.tabGUI.setPosY(18.0f);
                            break;
                        }
                        case JELLO: {
                            if (this.tabGUI.getPosY() == 50.0f) break;
                            this.tabGUI.setPosY(50.0f);
                            break;
                        }
                        case XAVE: {
                            if (this.tabGUI.getPosY() == 25.0f) break;
                            this.tabGUI.setPosY(25.0f);
                            break;
                        }
                        case NONE: {
                            if (this.tabGUI.getPosY() == 2.0f) break;
                            this.tabGUI.setPosY(2.0f);
                            break;
                        }
                    }
                    break;
                }
                case MOON: {
                    this.moonTabGui.onDraw(event.getScaledResolution());
                    switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                        case NOVOLINE: 
                        case ASTOLFO: 
                        case MOON: 
                        case REMIX: {
                            if (this.moonTabGui.getPosY() == 20.0f) break;
                            this.moonTabGui.setPosY(20.0f);
                            break;
                        }
                        case JELLO: {
                            if (this.moonTabGui.getPosY() == 50.0f) break;
                            this.moonTabGui.setPosY(50.0f);
                            break;
                        }
                        case XAVE: {
                            if (this.moonTabGui.getPosY() == 25.0f) break;
                            this.moonTabGui.setPosY(25.0f);
                            break;
                        }
                        case NONE: {
                            if (this.moonTabGui.getPosY() == 15.0f) break;
                            this.moonTabGui.setPosY(15.0f);
                            break;
                        }
                    }
                    break;
                }
                case JELLO: {
                    this.jelloTabGUi.onDraw(event.getScaledResolution());
                    switch ((HUD.WatermarkMode)((Object)HUD.watermarkModes.getValue())) {
                        case NOVOLINE: 
                        case ASTOLFO: 
                        case MOON: 
                        case REMIX: {
                            if (this.jelloTabGUi.getPosY() == 20.0f) break;
                            this.jelloTabGUi.setPosY(20.0f);
                            break;
                        }
                        case JELLO: {
                            if (this.jelloTabGUi.getPosY() == 50.0f) break;
                            this.jelloTabGUi.setPosY(50.0f);
                            break;
                        }
                        case XAVE: {
                            if (this.jelloTabGUi.getPosY() == 25.0f) break;
                            this.jelloTabGUi.setPosY(25.0f);
                            break;
                        }
                        case NONE: {
                            if (this.jelloTabGUi.getPosY() == 15.0f) break;
                            this.jelloTabGUi.setPosY(15.0f);
                        }
                    }
                    break;
                }
            }
        }
        super.render(event);
    }

    @Override
    public void keyPress(KeyInputEvent event) {
        switch (this.getTabGUIMode()) {
            case MOON: {
                this.moonTabGui.onKeyPress(event.getKey());
                break;
            }
            case JELLO: {
                this.jelloTabGUi.onKeyPress(event.getKey());
                break;
            }
            case COMPACT: {
                this.tabGUI.onKeyPress(event.getKey());
            }
        }
    }

    public HUD.TabGUIMode getTabGUIMode() {
        return (HUD.TabGUIMode)((Object)HUD.tabGuiModes.getValue());
    }
}

