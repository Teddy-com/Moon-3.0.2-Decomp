/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import me.moon.gui.changelog.ChangeComponent;
import me.moon.gui.changelog.ChangeType;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.BackgroundShader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiChangelog
extends GuiScreen {
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private final ArrayList<ChangeComponent> changeComponents = new ArrayList();
    private BackgroundShader shadering;
    private float scrollingY = 0.0f;

    @Override
    public void initGui() {
        this.shadering = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\nuniform float time;\nuniform vec2 resolution;\n\n#define hash(a) fract(sin(a)*12345.0) \n#define noise(p) ((old_noise(p, 883.0, 971.0) + old_noise(p + 0.5, 113.0, 157.0)) * 0.5)\nfloat old_noise(vec3 x, float c1, float c2) {\n    vec3 p = floor(x);\n    vec3 f = fract(x);\n    f = f*f*(3.0-2.0*f);\n    float n = p.x + p.y*c2+ c1*p.z;\n    return mix(\n        mix(\n            mix(hash(n+0.0),hash(n+1.0),f.x),\n            mix(hash(n+c2),hash(n+c2+1.0),f.x),\n            f.y),\n        mix(\n            mix(hash(n+c1),hash(n+c1+1.0),f.x),\n            mix(hash(n+c1+c2),hash(n+c1+c2+1.0),f.x),\n            f.y),\n        f.z);\n}\nfloat fbm(vec2 n)\n\t{\n\tfloat total = 0.0, amplitude = 1.0;\n\tfor (int i = 0; i < 5; i++)\n\t\t{\n\t\ttotal += noise(vec3(n.x, n.y, 0.0) * 1.0) * amplitude;\n\t\tn += n;\n\t\tamplitude *= 0.2;\n\t\t}\n\treturn total;\n\t}\n\n\nvoid main( void ) \n\t{\n\tvec2 uv = (gl_FragCoord.xy / resolution.xy * 2. - 1.)*2.00;\n\n\n\tconst vec3 c1 = vec3(0, 0, 0);\n\tconst vec3 c2 = vec3(0.1, 0.6, 0.9);\n\tconst vec3 c3 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c4 = vec3(0.6, 0.1, 1.0);\n\tconst vec3 c5 = vec3(0.1);\n\tconst vec3 c6 = vec3(0.9);\n\tvec2 p = gl_FragCoord.xy * 8.0 / resolution.xx;\n\tfloat q = fbm(p - vec2(0.0, time * 4.4));\n\tvec2 r = vec2(fbm(p + q + time * 0.7 - p.x - p.y), fbm(p + q - vec2(0.0, time * 0.94)));\n\tvec3 c = mix(c1, c2, fbm(p + r * 0.7)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);\n\tc=pow(c * cos(1.57 * gl_FragCoord.y / resolution.y), vec3(0.9));\t\n\tgl_FragColor = vec4(c, 2);\n\t}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height - 50, 100, 20, "Back", true));
        if (!this.changeComponents.isEmpty()) {
            this.changeComponents.clear();
        }
        try {
            String line;
            URL u = new URL("http://51.178.47.169/beta/changelog.txt");
            HttpURLConnection http = (HttpURLConnection)u.openConnection();
            http.setAllowUserInteraction(true);
            http.setRequestMethod("GET");
            http.connect();
            InputStream is = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                ChangeComponent changeComponent = new ChangeComponent();
                if (line.startsWith("[+]")) {
                    changeComponent.setChangeType(ChangeType.ADDED);
                } else if (line.startsWith("[/]")) {
                    changeComponent.setChangeType(ChangeType.FIXED);
                } else if (line.startsWith("[-]")) {
                    changeComponent.setChangeType(ChangeType.REMOVED);
                } else if (line.startsWith("[!]")) {
                    changeComponent.setChangeType(ChangeType.INFORMATION);
                }
                changeComponent.setChange(line);
                this.changeComponents.add(changeComponent);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawRect(0.0, 0.0, this.mc.displayWidth, this.mc.displayHeight, -14606047);
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            wheel = wheel > 0 ? 32 : -32;
            this.scrollingY = Math.min(Math.max(this.scrollingY + (float)wheel, (float)(-(this.changeComponents.size() * (this.mc.fontRendererObj.FONT_HEIGHT + 2)) + this.height - 55)), 0.0f);
        }
        this.scrollingY = Math.min(Math.max(this.scrollingY, (float)(-(this.changeComponents.size() * (this.mc.fontRendererObj.FONT_HEIGHT + 2)) + this.height - 55)), 0.0f);
        Fonts.moonMiddle.drawCenteredStringWithShadow("Changelog", (float)this.width / 2.0f, 8.0f, -1);
        RenderUtil.drawBorderedRect(50.0, 25.0, this.width - 100, this.height - 50, 0.5, -15374912, 0);
        RenderUtil.drawGradient2(50.0, 25.0, 50 + (this.width - 100), 25 + (this.height - 50), -16512705, -16777216);
        MCBlurUtil.drawGuiBlur(50, 25, this.width - 100, this.height - 50, 15.0f);
        RenderUtil.drawBorderedRect(50.0, 25.0, this.width - 100, this.height - 50, 0.5, -15374912, 0);
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(sr, 50.0f, 27.0f, this.width - 109, this.height - 54);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        float barHeight = this.height - 56;
        float yPos = 29.0f;
        float totalHeight = this.changeComponents.size() * (this.mc.fontRendererObj.FONT_HEIGHT + 2);
        float scrollbarHeight = barHeight / totalHeight * barHeight;
        for (ChangeComponent component : this.changeComponents) {
            this.mc.fontRendererObj.drawOutlinedString(component.getFormattedText(), 54.0f, yPos + this.scrollingY, 0.5f, -1);
            yPos += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        RenderUtil.drawRect(this.width - 56, 28.0, 3.0, barHeight, -2013265920);
        RenderUtil.drawRect((float)this.width - 55.5f, 28.0f - (barHeight - scrollbarHeight) / (totalHeight - barHeight) * this.scrollingY, 2.0, scrollbarHeight, -1434419072);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
        }
        super.actionPerformed(button);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}

