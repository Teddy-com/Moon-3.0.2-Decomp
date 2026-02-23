/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GLContext
 */
package net.minecraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import me.moon.Moon;
import me.moon.gui.GuiChangelog;
import me.moon.gui.account.gui.GuiAltManager;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.BackgroundShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();
    private double hue = 0.0;
    private float hues = 0.0f;
    private int panoramaTimer;
    private final Object threadLock = new Object();
    private String openGLWarning1 = "";
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + (Object)((Object)EnumChatFormatting.UNDERLINE) + "here" + (Object)((Object)EnumChatFormatting.RESET) + " for more information.";
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;
    private AnimationUtil logoUtil = new AnimationUtil(0.0, 0.0);
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private BackgroundShader shadering = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");

    public GuiMainMenu() {
        this.openGLWarning2 = field_96138_a;
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    @Override
    public void updateScreen() {
        this.panoramaTimer += 2;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
    }

    public static void blur() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f - 125.0f, (float)sr.getScaledHeight() / 2.0f - 50.0f, 250.0, 140.0, -1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        DynamicTexture viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 23, 200, 20, "Singleplayer", true));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 1, 200, 20, "Multiplayer", true));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 21, 200, 20, "AltManager", true));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + 43, 200, 20, "Settings", true));
        this.buttonList.add(new GuiButton(5, this.width - 25, 5, 20, 20, "d", true));
        Object object = this.threadLock;
        synchronized (object) {
            int field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            int field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(field_92023_s, field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get((int)0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }
        this.mc.func_181537_a(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiChangelog());
                break;
            }
            case 5: {
                this.mc.shutdown();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -14606047);
        Fonts.moon.drawStringWithShadow("\u00a7fMoon " + Moon.INSTANCE.getVersion(), 3.0, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        Fonts.moon.drawStringWithShadow("Welcome back, \u00a7c" + Moon.INSTANCE.username, sr.getScaledWidth() - Fonts.moon.getStringWidth("Welcome back, " + Moon.INSTANCE.username) - 3, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        if (Moon.INSTANCE.getVersionType() == Moon.VersionType.BETA) {
            Fonts.moon.drawStringWithShadow("\u00a7fThis is a \u00a7cBETA \u00a7fplease report bugs on the discord.", (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moon.getStringWidth("This is a BETA please report bugs on the discord.") / 2.0f, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        }
        Fonts.moonBig.drawStringWithShadow("Moon " + Moon.INSTANCE.getVersion(), (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moonBig.getStringWidth("Moon " + Moon.INSTANCE.getVersion()) / 2.0f, (float)sr.getScaledHeight() / 2.0f - 43.0f, -5592406);
        GlStateManager.pushMatrix();
        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 125, sr.getScaledHeight() / 2 - 50, 250.0, 135.0, -15374912);
        MCBlurUtil.drawGuiBlur(0, 0, this.mc.displayWidth, this.mc.displayHeight, 10.0f);
        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 125, sr.getScaledHeight() / 2 - 50, 250.0, 135.0, new Color(25, 25, 25, 250).getRGB());
        Fonts.moon.drawStringWithShadow("\u00a7fMoon " + Moon.INSTANCE.getVersion(), 3.0, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        Fonts.moon.drawStringWithShadow("Welcome back, \u00a7c" + Moon.INSTANCE.username, sr.getScaledWidth() - Fonts.moon.getStringWidth("Welcome back, " + Moon.INSTANCE.username) - 3, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        if (Moon.INSTANCE.getVersionType() == Moon.VersionType.BETA) {
            Fonts.moon.drawStringWithShadow("\u00a7fThis is a \u00a7cBETA \u00a7fplease report bugs on the discord.", (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moon.getStringWidth("This is a BETA please report bugs on the discord.") / 2.0f, sr.getScaledHeight() - this.fontRendererObj.FONT_HEIGHT - 3, -1426063361);
        }
        Fonts.moonBig.drawStringWithShadow("Moon " + Moon.INSTANCE.getVersion(), (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moonBig.getStringWidth("Moon " + Moon.INSTANCE.getVersion()) / 2.0f, (float)sr.getScaledHeight() / 2.0f - 43.0f, -5592406);
        GlStateManager.popMatrix();
        if (Moon.INSTANCE.getModuleManager().getModule("ClickGUI").getKeyBind() != 0 && Keyboard.isKeyDown((int)Moon.INSTANCE.getModuleManager().getModule("ClickGUI").getKeyBind())) {
            ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
            clickGUI.callClickGui();
        }
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moonBig.getStringWidth("Moon " + Moon.INSTANCE.getVersion()) / 2.0f, (float)sr.getScaledHeight() / 2.0f - 43.0f, Fonts.moonBig.getStringWidth("Moon " + Moon.INSTANCE.getVersion()), Fonts.moonBig.getHeight())) {
            this.logoUtil.interpolate((float)Fonts.moonBig.getStringWidth("Moon " + Moon.INSTANCE.getVersion()) / 2.0f, 0.0, 0.1f);
            if (Mouse.getEventButtonState()) {
                this.mc.displayGuiScreen(new GuiChangelog());
            }
        } else {
            this.logoUtil.interpolate(0.0, 0.0, 0.1f);
        }
        RenderUtil.drawRect((double)((float)sr.getScaledWidth() / 2.0f) - this.logoUtil.getPosX(), (float)sr.getScaledHeight() / 2.0f - 29.0f, this.logoUtil.getPosX() * 2.0, 1.0, -5592406);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }
}

