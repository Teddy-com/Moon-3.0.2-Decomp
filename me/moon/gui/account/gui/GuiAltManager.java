/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.account.gui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import me.moon.Moon;
import me.moon.gui.account.gui.components.GuiAccountList;
import me.moon.gui.account.gui.impl.GuiAddAlt;
import me.moon.gui.account.gui.impl.GuiAltLogin;
import me.moon.gui.account.gui.impl.GuiAlteningLogin;
import me.moon.gui.account.gui.impl.GuiEditAlt;
import me.moon.gui.account.gui.thread.AccountLoginThread;
import me.moon.gui.account.system.Account;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.BackgroundShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.realms.Realms;
import net.minecraft.util.Session;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager
extends GuiScreen {
    public static final GuiAltManager INSTANCE = new GuiAltManager();
    public static Account currentAccount;
    public static AccountLoginThread loginThread;
    private GuiAccountList accountList;
    private int scrollY;
    private AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private BackgroundShader backgroundShader = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 23, 100, 20, "Import", true));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 157, this.height - 23, 50, 20, "Direct", true));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 207, this.height - 23, 50, 20, "Add", true));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 50, this.height - 23, 100, 20, "TheAltening", true));
        this.buttonList.add(new GuiButton(9, this.width / 2 + 54, this.height - 23, 100, 20, "Clipboard", true));
    }

    public void blur() {
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -1);
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            wheel = wheel > 0 ? 32 : -32;
            this.scrollY = Math.min(Math.max(this.scrollY + wheel, -(Moon.INSTANCE.getAccountManager().getAccounts().size() * 33) + this.height - 100), 0);
        }
        this.scrollY = Math.min(Math.max(this.scrollY, -(Moon.INSTANCE.getAccountManager().getAccounts().size() * 33) + this.height - 100), 0);
        this.transUtil.interpolate(0.0, this.scrollY, 16.0f / (float)Minecraft.getDebugFPS());
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -14606047);
        MCBlurUtil.drawGuiBlur(0, 0, this.width, this.height, 10.0f);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        RenderUtil.drawBorderedRect(50.0, 25.0, this.width - 100, this.height - 50, 0.5, -15374912, 0);
        RenderUtil.drawGradient2(50.0, 25.0, 50 + (this.width - 100), 25 + (this.height - 50), -16512705, -16777216);
        double yPos3 = 50.0 + this.transUtil.getPosY();
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(sr, 0.0f, 50.0f, sr.getScaledWidth(), sr.getScaledHeight() - 102);
        for (Account account : Moon.INSTANCE.getAccountManager().getAccounts()) {
            float baseX = 60.0f;
            boolean isHoveredAccount = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + 1.0f, yPos3, this.width - 183, 28.0);
            boolean isHoveredEdit = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + (float)this.width - 180.0f, yPos3, 28.0, 28.0);
            boolean isHoveredRemove = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + (float)this.width - 150.0f, yPos3, 28.0, 28.0);
            if (MouseUtil.mouseWithinBounds((int)baseX, (int)yPos3, 0.0, 40.0, sr.getScaledWidth(), sr.getScaledHeight() - 82) || MouseUtil.mouseWithinBounds((int)baseX, (int)yPos3 + 27, 0.0, 40.0, sr.getScaledWidth(), sr.getScaledHeight() - 82)) {
                RenderUtil.drawRect(baseX + 1.0f, yPos3, this.width - 183, 28.0, isHoveredAccount ? new Color(-16444847).brighter().getRGB() : new Color(-16444847).getRGB());
            }
            yPos3 += 33.0;
        }
        GL11.glDisable((int)3089);
        MCBlurUtil.drawGuiBlur(50, 25, this.width - 100, this.height - 50, 15.0f);
        RenderUtil.drawBorderedRect(50.0, 25.0, this.width - 100, this.height - 50, 0.5, -15374912, 0);
        Fonts.moonBig.drawCenteredStringWithShadow("Accounts", (float)sr.getScaledWidth() / 2.0f, 4.0f, -1);
        double yPos = 50.0 + this.transUtil.getPosY();
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(sr, 0.0f, 50.0f, sr.getScaledWidth(), sr.getScaledHeight() - 102);
        for (Account account : Moon.INSTANCE.getAccountManager().getAccounts()) {
            float baseX = 60.0f;
            boolean isHoveredAccount = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + 1.0f, yPos, this.width - 183, 28.0);
            boolean isHoveredEdit = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + (float)this.width - 180.0f, yPos, 28.0, 28.0);
            boolean isHoveredRemove = MouseUtil.mouseWithinBounds(p_drawScreen_1_, p_drawScreen_2_, baseX + (float)this.width - 150.0f, yPos, 28.0, 28.0);
            if (MouseUtil.mouseWithinBounds((int)baseX, (int)yPos + 27, 0.0, 40.0, sr.getScaledWidth(), sr.getScaledHeight() - 82) || MouseUtil.mouseWithinBounds((int)baseX, (int)yPos, 0.0, 40.0, sr.getScaledWidth(), sr.getScaledHeight() - 82)) {
                RenderUtil.drawRect(baseX + 1.0f, yPos, this.width - 183, 28.0, isHoveredAccount ? new Color(-16444847).brighter().getRGB() : new Color(-16444847).getRGB());
                RenderUtil.drawRect(baseX + (float)this.width - 180.0f, yPos, 28.0, 28.0, isHoveredEdit ? new Color(-15374912).brighter().getRGB() : -15374912);
                Fonts.iconFont.drawStringWithShadow("e", baseX + (float)this.width - 165.5f - (float)Fonts.iconFont.getStringWidth("e") / 2.0f, yPos + 14.0 - (double)((float)Fonts.iconFont.getHeight() / 2.0f), -1);
                RenderUtil.drawRect(baseX + (float)this.width - 150.0f, yPos, 28.0, 28.0, isHoveredRemove ? new Color(-2937041).brighter().getRGB() : -2937041);
                Fonts.iconFont.drawStringWithShadow("d", baseX + (float)this.width - 135.5f - (float)Fonts.iconFont.getStringWidth("d") / 2.0f, yPos + 14.0 - (double)((float)Fonts.iconFont.getHeight() / 2.0f), -1);
                float xPosText = baseX + 30.0f;
                Fonts.moon.drawStringWithShadow(account.getName(), xPosText, yPos + 3.0, -1);
                Fonts.moon.drawStringWithShadow(account.getPassword().replaceAll(".", "*"), xPosText, yPos + (double)Fonts.moon.getHeight() + 6.0, -1);
                Fonts.moon.drawStringWithShadow("Account Type: " + (account.getEmail().contains("@alt.com") ? "TheAltening" : "Mojang"), xPosText, yPos + (double)(Fonts.moon.getHeight() * 2) + 6.0, -1);
                RenderUtil.drawRect(baseX + 2.0f, (float)(yPos + 1.0), 26.0, 26.0, -15374912);
                this.drawFace(account.getName(), baseX + 3.0f, (float)(yPos + 2.0), 24, 24);
            }
            yPos += 33.0;
        }
        GL11.glDisable((int)3089);
        Moon.INSTANCE.getAccountManagerNotification().onDraw(sr);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc);
        double yPos = 50 + this.scrollY;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!MouseUtil.mouseWithinBounds(mouseX, mouseY, 0.0, 50.0, sr.getScaledWidth(), sr.getScaledHeight() - 102)) {
            return;
        }
        for (Account account : Moon.INSTANCE.getAccountManager().getAccounts()) {
            float baseX = 60.0f;
            boolean isHoveredAccount = MouseUtil.mouseWithinBounds(mouseX, mouseY, baseX + 1.0f, yPos, this.width - 183, 28.0);
            boolean isHoveredEdit = MouseUtil.mouseWithinBounds(mouseX, mouseY, baseX + (float)this.width - 180.0f, yPos, 28.0, 28.0);
            boolean isHoveredRemove = MouseUtil.mouseWithinBounds(mouseX, mouseY, baseX + (float)this.width - 150.0f, yPos, 28.0, 28.0);
            if (isHoveredRemove) {
                Moon.INSTANCE.getAccountManagerNotification().addNotification("\u00a7cRemoved Account named: " + account.getName(), 2000);
                Moon.INSTANCE.getAccountManager().getAccounts().remove(account);
                Moon.INSTANCE.getAccountManager().save();
                return;
            }
            if (isHoveredAccount) {
                this.login(account);
            }
            if (isHoveredEdit) {
                this.mc.displayGuiScreen(new GuiEditAlt(this, account));
            }
            yPos += 33.0;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (loginThread != null && loginThread.getStatus().contains("Logging in")) {
                    return;
                }
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
            case 3: {
                if (loginThread != null) {
                    loginThread.interrupt();
                    loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                if (loginThread != null) {
                    loginThread.interrupt();
                    loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiAlteningLogin(this));
                break;
            }
            case 7: {
                ArrayList<Account> registry = Moon.INSTANCE.getAccountManager().getAccounts();
                if (registry.isEmpty()) {
                    return;
                }
                Account randomAlt = registry.get(new Random().nextInt(Moon.INSTANCE.getAccountManager().getAccounts().size()));
                if (randomAlt.isBanned()) {
                    return;
                }
                this.login(randomAlt);
                break;
            }
            case 5: {
                if (this.mc.isFullScreen()) {
                    this.mc.toggleFullscreen();
                }
                new Thread(() -> {
                    System.out.println("import");
                    JFrame frame = new JFrame("Import alts");
                    frame.setDefaultCloseOperation(2);
                    frame.setAlwaysOnTop(true);
                    if (this.mc.isFullScreen()) {
                        this.mc.toggleFullscreen();
                    }
                    JFileChooser chooser = new JFileChooser(Realms.getGameDirectoryPath());
                    chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setDialogTitle("Select alt list");
                    frame.add(chooser);
                    if (chooser.showOpenDialog(null) != 0) {
                        return;
                    }
                    frame.dispatchEvent(new WindowEvent(frame, 201));
                    try {
                        for (String line : Files.readAllLines(Paths.get(chooser.getSelectedFile().getPath(), new String[0]))) {
                            if (!line.contains(":")) continue;
                            String[] split = line.split(":", 2);
                            Account account = new Account(split[0], split[1]);
                            if (Moon.INSTANCE.getAccountManager().getAccounts().contains(account)) continue;
                            Moon.INSTANCE.getAccountManager().getAccounts().add(account);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Moon.INSTANCE.getAccountManager().save();
                }).start();
                break;
            }
            case 8: {
                if (Moon.INSTANCE.getAccountManager().getLastAlt() == null) {
                    return;
                }
                this.login(Moon.INSTANCE.getAccountManager().getLastAlt());
                break;
            }
            case 9: {
                try {
                    Object data = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!(data instanceof String) || !((String)data).contains(":")) break;
                    String[] split = ((String)data).split(":", 2);
                    this.login(new Account(split[0], split[1]));
                    break;
                }
                catch (Exception e) {
                    System.out.println("Nothing copied to clipboard");
                }
            }
        }
    }

    public void login(Account account) {
        if (account.getEmail().contains("@alt.com")) {
            Moon.INSTANCE.switchToTheAltening();
        } else {
            Moon.INSTANCE.switchToMojang();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || account.getEmail().contains("@alt.com") || Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected && GuiDisconnected.niggaButton) {
            Moon.INSTANCE.switchToTheAltening();
        } else {
            Moon.INSTANCE.switchToMojang();
        }
        YggdrasilAuthenticationService yService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        UserAuthentication userAuth = yService.createUserAuthentication(Agent.MINECRAFT);
        if (userAuth == null) {
            Moon.INSTANCE.getAccountManagerNotification().addNotification("\u00a74Unknown Error!", 2000);
            return;
        }
        try {
            String email = account.getEmail();
            userAuth.setUsername(email);
            userAuth.setPassword(account.getPassword());
            userAuth.logIn();
            Session session = new Session(userAuth.getSelectedProfile().getName(), userAuth.getSelectedProfile().getId().toString(), userAuth.getAuthenticatedToken(), email.contains("@") ? "mojang" : "legacy");
            Minecraft.getMinecraft().setSession(session);
            account.setName(session.getUsername());
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected)) {
                Moon.INSTANCE.getAccountManager().setLastAlt(account);
            }
            Moon.INSTANCE.getAccountManager().save();
            currentAccount = account;
            Moon.INSTANCE.getAccountManagerNotification().addNotification("\u00a7aSuccessfully logged in as: " + account.getName(), 2000);
        }
        catch (AuthenticationException exception) {
            Moon.INSTANCE.getAccountManagerNotification().addNotification("\u00a7cLogin Failed!", 2000);
        }
        catch (NullPointerException exception) {
            Moon.INSTANCE.getAccountManagerNotification().addNotification("\u00a74Unknown Error!", 2000);
        }
    }

    private void drawFace(String name, float x, float y, int w, int h) {
        try {
            AbstractClientPlayer.getDownloadImageSkin2(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawModalRectWithCustomSizedTexture2(x, y, 24.0f, 24.0f, w, h, 192.0f, 192.0f);
            Gui.drawModalRectWithCustomSizedTexture2(x, y, 120.0f, 24.0f, w, h, 192.0f, 192.0f);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

