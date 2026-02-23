/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components;

import java.util.ArrayList;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.module.impl.visuals.components.autism.AutismTheme;
import me.moon.module.impl.visuals.components.impl.ArrayListComponent;
import me.moon.module.impl.visuals.components.impl.InformationComponent;
import me.moon.module.impl.visuals.components.impl.KeystrokesComponent;
import me.moon.module.impl.visuals.components.impl.PotionHUDComponent;
import me.moon.module.impl.visuals.components.impl.RBuildComponent;
import me.moon.module.impl.visuals.components.impl.TabGUIComponent;
import me.moon.module.impl.visuals.components.impl.TargetHUDComponent;
import me.moon.module.impl.visuals.components.impl.WatermarkComponent;
import me.moon.module.impl.visuals.components.moonvirtue.MVArraylistComponent;
import me.moon.module.impl.visuals.components.moonvirtue.MVWatermarkComponent;
import me.moon.module.impl.visuals.components.nostalgia.NArmorComponent;
import me.moon.module.impl.visuals.components.nostalgia.NArraylistComponent;
import me.moon.module.impl.visuals.components.nostalgia.NPotionComponent;
import me.moon.module.impl.visuals.components.nostalgia.NTabGuiComponent;
import me.moon.module.impl.visuals.components.nostalgia.NTargetHUDComponent;
import me.moon.module.impl.visuals.components.nostalgia.NWatermarkComponent;

public class ComponentManager {
    public ArrayList<Component> hudComponents = new ArrayList();
    public ArrayList<Component> nostalgiaComponents = new ArrayList();
    public ArrayList<Component> moonVirtueComponents = new ArrayList();
    public AutismTheme autismTheme = new AutismTheme();

    public ComponentManager() {
        this.init();
    }

    public void init() {
        this.hudComponents.add(new WatermarkComponent());
        this.hudComponents.add(new ArrayListComponent());
        this.hudComponents.add(new PotionHUDComponent());
        this.hudComponents.add(new RBuildComponent());
        this.hudComponents.add(new TabGUIComponent());
        this.hudComponents.add(new TargetHUDComponent());
        this.hudComponents.add(new InformationComponent());
        this.hudComponents.add(new KeystrokesComponent());
        this.nostalgiaComponents.add(new NArraylistComponent());
        this.nostalgiaComponents.add(new NWatermarkComponent());
        this.nostalgiaComponents.add(new NTabGuiComponent());
        this.nostalgiaComponents.add(new NPotionComponent());
        this.nostalgiaComponents.add(new NArmorComponent());
        this.nostalgiaComponents.add(new NTargetHUDComponent());
        this.moonVirtueComponents.add(new MVArraylistComponent());
        this.moonVirtueComponents.add(new MVWatermarkComponent());
        this.hudComponents.forEach(Component::init);
        this.nostalgiaComponents.forEach(Component::init);
        this.moonVirtueComponents.forEach(Component::init);
    }

    public void blur() {
        if (ComponentManager.isMoonVirtueTheme()) {
            return;
        }
        if (!ComponentManager.isNostalgiaTheme() && !ComponentManager.isGenesisTheme()) {
            this.hudComponents.forEach(Component::blur);
        }
    }

    public void bloom() {
        if (ComponentManager.isMoonVirtueTheme()) {
            return;
        }
        if (!ComponentManager.isNostalgiaTheme() && !ComponentManager.isGenesisTheme()) {
            this.hudComponents.forEach(Component::bloom);
        }
    }

    public void renderComponents(Render2DEvent event) {
        if (ComponentManager.isGenesisTheme()) {
            this.autismTheme.render(event);
            return;
        }
        if (ComponentManager.isMoonVirtueTheme()) {
            this.moonVirtueComponents.forEach(component -> component.render(event));
            return;
        }
        if (ComponentManager.isNostalgiaTheme()) {
            this.nostalgiaComponents.forEach(component -> component.render(event));
        } else {
            this.hudComponents.forEach(component -> component.render(event));
        }
    }

    public void onKeyPress(KeyInputEvent keyInputEvent) {
        if (ComponentManager.isGenesisTheme()) {
            return;
        }
        if (ComponentManager.isMoonVirtueTheme()) {
            return;
        }
        if (ComponentManager.isNostalgiaTheme()) {
            this.nostalgiaComponents.forEach(component -> component.keyPress(keyInputEvent));
        } else {
            this.hudComponents.forEach(component -> component.keyPress(keyInputEvent));
        }
    }

    public void update() {
        if (ComponentManager.isGenesisTheme()) {
            return;
        }
        if (ComponentManager.isMoonVirtueTheme()) {
            return;
        }
        if (ComponentManager.isNostalgiaTheme()) {
            this.nostalgiaComponents.forEach(Component::update);
        } else {
            this.hudComponents.forEach(Component::update);
        }
    }

    public static boolean isGenesisTheme() {
        return ((String)HUD.clientName.getValue()).equals("Genesis Best");
    }

    public static boolean isNostalgiaTheme() {
        return ((String)HUD.clientName.getValue()).equals("Nostalgia Components");
    }

    public static boolean isMoonVirtueTheme() {
        return ((String)HUD.clientName.getValue()).equals("Moon Virtue");
    }
}

