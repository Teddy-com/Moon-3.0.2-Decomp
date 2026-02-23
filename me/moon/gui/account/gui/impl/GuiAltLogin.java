/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.account.gui.impl;

import java.io.IOException;
import me.moon.gui.account.gui.components.GuiPasswordField;
import me.moon.gui.account.gui.impl.GuiTextBox;
import me.moon.gui.account.gui.thread.AccountLoginThread;
import me.moon.gui.account.system.Account;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.BackgroundShader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiAltLogin
extends GuiScreen {
    private final GuiScreen parent;
    private GuiTextBox email;
    private GuiPasswordField password;
    private AccountLoginThread loginThread;
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private BackgroundShader backgroundShader = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");

    public GuiAltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2, 200, 20, "Login", true));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 24, 200, 20, "Back", true));
        this.email = new GuiTextBox(0, this.fontRendererObj, this.width / 2 - 100, this.height / 2 - 48, 200, 20);
        this.email.setMaxStringLength(Integer.MAX_VALUE);
        this.email.setFocused(true);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        this.email.textboxKeyTyped(character, keyCode);
        switch (keyCode) {
            case 15: {
                this.email.setFocused(!this.email.isFocused());
                this.password.setFocused(!this.password.isFocused());
                break;
            }
            case 28: {
                this.actionPerformed((GuiButton)this.buttonList.get(0));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.email.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -14606047);
        Fonts.sectioNormal.drawCenteredStringWithShadow("Direct Login", this.width / 2, 5.0f, -1);
        this.email.drawTextBox();
        if (this.email.getText().isEmpty() && !this.email.isFocused()) {
            Fonts.sectioNormal.drawStringWithShadow("Email:Pass", this.width / 2 - 96, (double)(this.height / 2) - 40.5, -7829368);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        this.email.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (this.email.getText().isEmpty() || !this.email.getText().contains(":")) break;
                String[] split = this.email.getText().split(":");
                Account account = new Account(split[0], split[1]);
                this.loginThread = new AccountLoginThread(account.getEmail(), account.getPassword());
                this.loginThread.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }
}

