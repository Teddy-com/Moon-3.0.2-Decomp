/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.io.File;
import me.moon.Moon;
import me.moon.debug.DebugManager;
import me.moon.event.Handler;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.ResizeEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.components.ComponentManager;
import me.moon.module.impl.visuals.hud.editscreen.GuiHudEditScreen;
import me.moon.utils.blur.BlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUD
extends Module {
    public ComponentManager hudComps = new ComponentManager();
    public static final StringValue clientName = new StringValue("Client Name", "Moon 3.0");
    public static final EnumValue<hudModes> modes = new EnumValue<hudModes>("HUD", hudModes.CUSTOM);
    public static final EnumValue<ColorModes> colorModes = new EnumValue<ColorModes>("Color", ColorModes.MOON);
    public static final EnumValue<WatermarkMode> watermarkModes = new EnumValue<WatermarkMode>("Watermark", WatermarkMode.MOON);
    public static final EnumValue<ArrayListMode> arrayListModes = new EnumValue<ArrayListMode>("ArrayList", ArrayListMode.MOON);
    public static final BooleanValue novoSide = new BooleanValue("Novo Sidebar", false, (Value)arrayListModes, "Novoline");
    public static final BooleanValue novoBack = new BooleanValue("Novo Background", false, (Value)arrayListModes, "Novoline");
    public static final BooleanValue astolfoSide = new BooleanValue("Astolfo Sidebar", false, (Value)arrayListModes, "Astolfo");
    public static final BooleanValue astolfoBack = new BooleanValue("Astolfo Background", false, (Value)arrayListModes, "Astolfo");
    public static final BooleanValue remixSide = new BooleanValue("Remix Sidebar", false, (Value)arrayListModes, "Remix");
    public static final BooleanValue remixBack = new BooleanValue("Remix Background", false, (Value)arrayListModes, "Remix");
    public static final BooleanValue moonSide = new BooleanValue("Moon Sidebar", false, (Value)arrayListModes, "Moon");
    public static final BooleanValue moonBackground = new BooleanValue("Moon Background", false, (Value)arrayListModes, "Moon");
    public static final BooleanValue mcFont = new BooleanValue("Minecraft Font", false);
    public static final NumberValue<Integer> backgroundAlpha = new NumberValue<Integer>("Background Alpha", 60, 1, 255, 1);
    public static final EnumValue<SidebarMode> sidebarMode = new EnumValue<SidebarMode>("Sidebar", SidebarMode.RIGHT);
    public static final EnumValue<ReleaseBuildMode> releaseBuildModes = new EnumValue<ReleaseBuildMode>("Release Build", ReleaseBuildMode.MOON);
    public static final EnumValue<PotionHUDMode> potionHUDMode = new EnumValue<PotionHUDMode>("PotionHUD", PotionHUDMode.MOON);
    public static final EnumValue<TargetHUDMode> targetHUDMode = new EnumValue<TargetHUDMode>("TargetHUD", TargetHUDMode.MOON);
    public static final EnumValue<TabGUIMode> tabGuiModes = new EnumValue<TabGUIMode>("Tab GUI", TabGUIMode.MOON);
    public static final BooleanValue fontForTabGUI = new BooleanValue("TabGUI Font", "TabGUI Font", true);
    public static final EnumValue<InformationMode> informationMode = new EnumValue<InformationMode>("Information", InformationMode.MOON);
    public static final EnumValue<KeystrokesMode> keystrokesMode = new EnumValue<KeystrokesMode>("Keystrokes", KeystrokesMode.NONE);
    public static final ColorValue colorValue = new ColorValue("Color", -1);
    public static final BooleanValue betterChat = new BooleanValue("Better Chat", false);
    public static final BooleanValue betterScoreboard = new BooleanValue("Better Scoreboard", false);
    public static final BooleanValue transparentChat = new BooleanValue("Transparent Chat", false);
    public static final BooleanValue transparentScoreboard = new BooleanValue("Transparent Scoreboard", false);
    public static final BooleanValue animatedChat = new BooleanValue("Animated Chat", false);
    public static float offset = 0.0f;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);
    private final ResourceLocation INVENTORY_RESOURCE = new ResourceLocation("textures/gui/container/inventory.png");
    private Minecraft mc = Minecraft.getMinecraft();
    private MCFontRenderer fr = Fonts.jelloCompassNormal;
    private MCFontRenderer fr1 = Fonts.jelloCompassBig;
    private MCFontRenderer fr2 = Fonts.JelloCompassSmall;
    private int colorMarker;
    private int centerX;
    private int colorDirection;
    public int tintMarker;
    public int tintDirection;
    private float offsetAll;
    public int details;

    public HUD() {
        super("HUD", Module.Category.VISUALS, -1);
        System.out.println("Initialized HUD");
    }

    @Override
    public void onEnable() {
        Moon.INSTANCE.getComponentManager().setDirectory(new File(Moon.INSTANCE.getDir(), "components"));
        Moon.INSTANCE.getComponentManager().initComponents();
        Moon.INSTANCE.getComponentManager().loadComps();
        this.hudComps = new ComponentManager();
        BlurUtil.init();
        for (Module value : Moon.INSTANCE.getModuleManager().getModuleMap().values()) {
            value.offset = 0.0f;
        }
        super.onEnable();
    }

    public void blur() {
        if (this.mc.gameSettings.showDebugInfo || modes.getValue() == hudModes.CUSTOM || modes.getValue() == hudModes.NONE) {
            return;
        }
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        this.hudComps.blur();
    }

    public void onBloom() {
        if (this.mc.gameSettings.showDebugInfo || modes.getValue() == hudModes.CUSTOM || modes.getValue() == hudModes.NONE) {
            return;
        }
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        this.hudComps.bloom();
    }

    @Handler(value=ResizeEvent.class)
    public void onWindowResize(ResizeEvent event) {
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        if (!event.isPre()) {
            ScaledResolution sr = new ScaledResolution(this.mc);
            for (Module module : Moon.INSTANCE.getModuleManager().getModuleMap().values()) {
                module.offset = 0.0f;
                module.getTransUtil().setPosX(sr.getScaledWidth());
            }
        }
    }

    @Handler(value=KeyInputEvent.class)
    public void onKeyEvent(KeyInputEvent event) {
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        if (event.getKey() == 210) {
            this.mc.displayGuiScreen(new GuiHudEditScreen());
        }
        this.hudComps.onKeyPress(event);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        if (arrayListModes.getValue() == ArrayListMode.ASTOLFO || arrayListModes.getValue() == ArrayListMode.NOVOLINE) {
            sidebarMode.setParentValueObject(arrayListModes);
            sidebarMode.setParentValue(arrayListModes.getValueAsString());
        }
        BooleanValue shouldShow = new BooleanValue("Should Show McFont", arrayListModes.getValue() == ArrayListMode.MOON || watermarkModes.getValue() == WatermarkMode.MOON || informationMode.getValue() == InformationMode.MOON || potionHUDMode.getValue() == PotionHUDMode.MOON || releaseBuildModes.getValue() == ReleaseBuildMode.MOON);
        mcFont.setParentValueObject(shouldShow);
        mcFont.setParentValue("true");
        if (this.mc.gameSettings.showDebugInfo) {
            Fonts.moonBig.drawOutlinedString("Moon 3.0", (float)event.getScaledResolution().getScaledWidth() / 2.0f - (float)Fonts.moonBig.getStringWidth("Moon 3.0") / 2.0f, 5.0, -15374912);
            return;
        }
        DebugManager.renderDebugs();
        if (Moon.INSTANCE.getModuleManager().getModule("Secret") != null && Moon.INSTANCE.getModuleManager().getModule("Secret").isEnabled()) {
            return;
        }
        if (modes.getValue() != hudModes.NORMAL) {
            return;
        }
        ScaledResolution sr = event.getScaledResolution();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (informationMode.getValue() == InformationMode.JELLO) {
            this.drawCompass(event.getScaledResolution().getScaledWidth() / 2);
        }
        this.hudComps.renderComponents(event);
        Moon.INSTANCE.getNotificationManager().renderNotifications();
        GlStateManager.disableAlpha();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        this.hudComps.update();
    }

    public void drawCompass(int screenWidth) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        GL11.glPopMatrix();
        float compassY = 40.0f;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float direction = this.normalize(this.mc.thePlayer.rotationYaw);
        this.offsetAll = 500.0f * direction / 360.0f;
        this.centerX = screenWidth;
        this.colorMarker = this.tintMarker != 0 ? Color.HSBtoRGB((float)this.tintMarker / 50.0f, 1.0f, 1.0f) : -1;
        this.colorDirection = this.tintDirection != 0 ? Color.HSBtoRGB((float)this.tintDirection / 50.0f, 1.0f, 1.0f) : -1;
        this.drawDirection("S", 0, 1.5);
        this.drawDirection("W", 90, 1.5);
        this.drawDirection("N", 180, 1.5);
        this.drawDirection("E", 270, 1.5);
        this.drawDirection("SW", 45, 1.0);
        this.drawDirection("NW", 135, 1.0);
        this.drawDirection("NE", 225, 1.0);
        this.drawDirection("SE", 315, 1.0);
        this.drawDirection("15", 15, 0.75);
        this.drawDirection("30", 30, 0.75);
        this.drawDirection("60", 60, 0.75);
        this.drawDirection("75", 75, 0.75);
        this.drawDirection("105", 105, 0.75);
        this.drawDirection("120", 120, 0.75);
        this.drawDirection("150", 150, 0.75);
        this.drawDirection("165", 165, 0.75);
        this.drawDirection("195", 195, 0.75);
        this.drawDirection("210", 210, 0.75);
        this.drawDirection("240", 240, 0.75);
        this.drawDirection("255", 255, 0.75);
        this.drawDirection("285", 285, 0.75);
        this.drawDirection("300", 300, 0.75);
        this.drawDirection("330", 330, 0.75);
        this.drawDirection("345", 345, 0.75);
    }

    private void drawDirection(String dir, int degrees, double scale) {
        float compassY = 40.0f;
        float offset = (float)(500 * degrees) / 360.0f - this.offsetAll;
        if (offset > 250.0f) {
            offset -= 500.0f;
        }
        if (offset < -250.0f) {
            offset += 500.0f;
        }
        double opacity = MathHelper.clamp_float((float)(1.0 - (double)(Math.abs(offset) / 35.0f) / 5.0), 0.0f, 1.0f);
        double opacity2 = MathHelper.clamp_float((float)(0.8 - (double)(Math.abs(offset) / 40.0f) / 5.0), 0.0f, 1.0f);
        if (opacity > 0.1) {
            int color = new Color(238, 238, 238, (int)(opacity * 255.0)).getRGB();
            int color2 = new Color(238, 238, 238, (int)(opacity2 * 255.0)).getRGB();
            if (scale == 0.75) {
                float posX = (float)this.centerX + offset - (float)this.fr2.getStringWidth(dir);
                int posY = (int)(compassY + 11.0f - (float)this.fr2.getHeight());
                RenderUtil.drawRect(posX - 1.0f + (float)this.fr2.getStringWidth(dir) / 2.0f, posY - 3, 1.0, 5.0, color2);
                this.fr2.drawString(dir, posX, (float)((double)posY + 5.0), color);
            } else if (scale == 1.5) {
                float posX = (float)this.centerX + offset - (float)this.fr1.getStringWidth(dir);
                switch (dir) {
                    case "N": 
                    case "E": 
                    case "S": {
                        posX -= 2.0f;
                        break;
                    }
                    case "W": {
                        posX += 1.0f;
                    }
                }
                int posY = (int)(compassY + 11.0f - (float)this.fr1.getHeight());
                this.fr1.drawString(dir, posX, (float)((double)posY - 0.5), color);
            } else {
                float posX = (float)this.centerX + offset - (float)this.fr.getStringWidth(dir);
                switch (dir) {
                    case "SW": {
                        posX += 4.0f;
                        break;
                    }
                    case "NW": {
                        posX += 3.0f;
                        break;
                    }
                    case "SE": {
                        posX -= 1.0f;
                    }
                }
                int posY = (int)(compassY + 9.0f - (float)this.fr.getHeight());
                this.fr.drawString(dir, posX, (float)((double)posY - 1.5), color);
            }
        }
    }

    public float normalize(float direction) {
        if (direction > 360.0f) {
            direction %= 360.0f;
        }
        while (direction < 0.0f) {
            direction += 360.0f;
        }
        return direction;
    }

    public void onBloomNC() {
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static int color(Float offset, int red, int green, int blue) {
        double timer = (float)(System.currentTimeMillis() % 1700L) / 850.0f;
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, green, blue, hsb);
        float brightness = (float)((double)hsb[2] * Math.abs(((double)offset.floatValue() + timer) % 1.0 - (double)0.55f) + (double)0.45f);
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
    }

    public static int getColorHUD() {
        int color = -1;
        switch ((ColorModes)((Object)colorModes.getValue())) {
            case ASTOLFO: {
                color = RenderUtil.getGradient((double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0, new Color(243, 111, 122), new Color(142, 95, 255), new Color(55, 230, 239)).getRGB();
                break;
            }
            case MOON: {
                color = RenderUtil.getGradientOffset(new Color(2, 163, 246), new Color(21, 70, 193), (double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case RAINBOW: {
                color = RenderUtil.getRainbow(4500, (int)((double)((int)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f))) + (double)Math.abs(System.currentTimeMillis()) / 1000.0), 0.5f);
                break;
            }
            case STATICFADE: {
                color = HUD.color(Float.valueOf((float)((double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0)), colorValue.getColor().getRed(), colorValue.getColor().getGreen(), colorValue.getColor().getBlue());
                break;
            }
            case CHRISTMAS: {
                color = RenderUtil.getGradientOffset(new Color(255, 69, 69), new Color(255, 255, 255), (double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case CUSTOM: {
                color = (Integer)colorValue.getValue();
            }
        }
        return color;
    }

    public static enum SidebarMode {
        RIGHT("Right"),
        LEFT("Left"),
        WRAP("Wrap");

        private final String name;

        private SidebarMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum ColorModes {
        WHITE("White"),
        MOON("Moon"),
        STATICFADE("Static Fade"),
        ASTOLFO("Astolfo"),
        CUSTOM("Custom"),
        RAINBOW("Rainbow"),
        CHRISTMAS("Christmas");

        private final String name;

        private ColorModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum KeystrokesMode {
        JELLO("Jello"),
        NONE("None");

        private final String name;

        private KeystrokesMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum TabGUIMode {
        MOON("Moon"),
        JELLO("Jello"),
        COMPACT("Compact"),
        NONE("None");

        private final String name;

        private TabGUIMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum PotionHUDMode {
        MOON("Moon"),
        ASTOLFO("Astolfo"),
        NOVOLINE("Novoline"),
        XAVE("Xave"),
        FLUX("Flux"),
        NONE("None");

        private final String name;

        private PotionHUDMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum ReleaseBuildMode {
        MOON("Moon"),
        ASTOLFO("Astolfo"),
        REMIX("Remix"),
        NOVOLINE("Novoline"),
        NONE("None");

        private final String name;

        private ReleaseBuildMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum TargetHUDMode {
        MOON("Moon"),
        ASTOLFO("Astolfo"),
        REMIX("Remix"),
        NOVOLINE("Novoline"),
        ASTOLFOOLD("Astolfo Old"),
        ASTOLFOO("AstolfoO"),
        NOVOLINEO("NovolineO"),
        NONE("None");

        private final String name;

        private TargetHUDMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum WatermarkMode {
        JELLO("Jello"),
        MOON("Moon"),
        ASTOLFO("Astolfo"),
        REMIX("Remix"),
        NOVOLINE("Novoline"),
        XAVE("Xave"),
        FLUX("Flux"),
        NONE("None");

        private final String name;

        private WatermarkMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum InformationMode {
        JELLO("Jello"),
        MOON("Moon"),
        ASTOLFO("Astolfo"),
        REMIX("Remix"),
        NOVOLINE("Novoline"),
        XAVE("Xave"),
        FLUX("Flux"),
        NONE("None");

        private final String name;

        private InformationMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum ArrayListMode {
        JELLO("Jello"),
        MOON("Moon"),
        REMIX("Remix"),
        NOVOLINE("Novoline"),
        XAVE("Xave"),
        FLUX("Flux"),
        ASTOLFO("Astolfo"),
        NONE("None");

        private final String name;

        private ArrayListMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum hudModes {
        CUSTOM("Custom"),
        NORMAL("Normal"),
        NONE("None");

        private final String name;

        private hudModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

