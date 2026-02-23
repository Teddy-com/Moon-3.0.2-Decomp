/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.account.gui.impl;

import java.io.IOException;
import java.util.Objects;
import me.moon.Moon;
import me.moon.gui.account.gui.components.GuiPasswordField;
import me.moon.gui.account.gui.thread.AccountLoginThread;
import me.moon.gui.account.system.Account;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.BackgroundShader;
import me.moon.utils.thealtening.TheAltening;
import me.moon.utils.thealtening.domain.AlteningAlt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiAlteningLogin
extends GuiScreen {
    public static AccountLoginThread thread;
    public static GuiPasswordField token;
    public static GuiPasswordField key;
    private final GuiScreen previousScreen;
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private BackgroundShader backgroundShader = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");

    public GuiAlteningLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case -1: {
                if (key.getText().isEmpty() || token.getText().isEmpty()) {
                    return;
                }
                Moon.INSTANCE.getAccountManager().setAlteningKey(key.getText());
                Moon.INSTANCE.getAccountManager().setLastAlteningAlt(token.getText());
                thread = new AccountLoginThread(token.getText().replaceAll(" ", ""), "moon");
                thread.start();
                Moon.INSTANCE.getAccountManager().save();
                break;
            }
            case 0: {
                if (key.getText().isEmpty()) {
                    return;
                }
                try {
                    TheAltening theAltening = new TheAltening(key.getText());
                    AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                    token.setText(Objects.requireNonNull(account).getToken());
                    Moon.INSTANCE.getAccountManager().save();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (token.getText().isEmpty()) break;
                Moon.INSTANCE.getAccountManager().setAlteningKey(key.getText());
                Moon.INSTANCE.getAccountManager().setLastAlteningAlt(token.getText());
                thread = new AccountLoginThread(token.getText().replaceAll(" ", ""), "moon");
                thread.start();
                Moon.INSTANCE.getAccountManager().save();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 3: {
                if (key.getText().isEmpty()) {
                    return;
                }
                try {
                    TheAltening theAltening = new TheAltening(key.getText());
                    AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                    token.setText(Objects.requireNonNull(account).getToken());
                    Moon.INSTANCE.getAccountManager().save();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (token.getText().isEmpty()) break;
                Account account = new Account(token.getText(), "moonbestclientbuynowhehe");
                if (Moon.INSTANCE.getAccountManager().getAccounts().contains(account)) break;
                Moon.INSTANCE.getAccountManager().getAccounts().add(account);
                Moon.INSTANCE.getAccountManager().save();
            }
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -14606047);
        token.drawTextBox();
        key.drawTextBox();
        Fonts.sectioNormal.drawCenteredStringWithShadow("The Altening Login", this.width / 2, 5.0f, -1);
        Fonts.sectioNormal.drawCenteredStringWithShadow(this.mc.getSession().getUsername(), this.width / 2, 15.0f, -7829368);
        if (token.getText().isEmpty() && !token.isFocused()) {
            Fonts.sectioNormal.drawStringWithShadow("Token", this.width / 2 - 94, (double)(this.height / 4) + 49.5, -7829368);
        }
        if (key.getText().isEmpty() && !key.isFocused()) {
            Fonts.sectioNormal.drawStringWithShadow("API-Key", this.width / 2 - 94, (double)(this.height / 4) + 79.5, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(-1, this.width / 2 - 100, this.height / 4 + 124, 200, 20, "Login", true));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 148, 200, 20, "Generate + Login", true));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 196, 200, 20, "Back", true));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 100, 200, 20, "Multiplayer", true));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 172, 200, 20, "Generate + Add", true));
        token = new GuiPasswordField(this.height / 4 + 24, this.mc.fontRendererObj, this.width / 2 - 98, this.height / 4 + 42, 196, 20);
        token.setMaxStringLength(Integer.MAX_VALUE);
        key = new GuiPasswordField(this.height / 4 + 22, this.mc.fontRendererObj, this.width / 2 - 98, this.height / 4 + 72, 196, 20);
        key.setMaxStringLength(Integer.MAX_VALUE);
        if (Moon.INSTANCE.getAccountManager().getAlteningKey() != null) {
            key.setText(Moon.INSTANCE.getAccountManager().getAlteningKey());
        }
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t' && token.isFocused()) {
            token.setFocused(token.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        if (character == '\t' && GuiAlteningLogin.key.isFocused()) {
            GuiAlteningLogin.key.setFocused(GuiAlteningLogin.key.isFocused());
        }
        token.textboxKeyTyped(character, key);
        GuiAlteningLogin.key.textboxKeyTyped(character, key);
    }

    @Override
    public void updateScreen() {
        token.updateCursorCounter();
        key.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        token.mouseClicked(x, y, button);
        key.mouseClicked(x, y, button);
    }
}

