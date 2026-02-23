/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.account.gui.impl;

import java.io.IOException;
import me.moon.gui.account.gui.components.GuiPasswordField;
import me.moon.utils.shadering.BackgroundShader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiPrixGen
extends GuiScreen {
    private GuiScreen previousScreen;
    public static GuiPasswordField email;
    public static GuiPasswordField pass;
    private final String fragShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// glslsandbox uniforms\nuniform float time;\nuniform vec2 resolution;\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\n// --------[ Original ShaderToy begins here ]---------- //\n//CBS\n//Parallax scrolling fractal galaxy.\n//Inspired by JoshP's Simplicity shader: https://www.shadertoy.com/view/lslGWr\n\n// http://www.fractalforums.com/new-theories-and-research/very-simple-formula-for-fractal-patterns/\nfloat field(in vec3 p,float s) {\n\tfloat strength = 7. + .03 * log(1.e-6 + fract(sin(iTime) * 4373.11));\n\tfloat accum = s/4.;\n\tfloat prev = 0.;\n\tfloat tw = 0.;\n\tfor (int i = 0; i < 26; ++i) {\n\t\tfloat mag = dot(p, p);\n\t\tp = abs(p) / mag + vec3(-.5, -.4, -1.5);\n\t\tfloat w = exp(-float(i) / 7.);\n\t\taccum += w * exp(-strength * pow(abs(mag - prev), 2.2));\n\t\ttw += w;\n\t\tprev = mag;\n\t}\n\treturn max(0., 5. * accum / tw - .7);\n}\n\n// Less iterations for second layer\nfloat field2(in vec3 p, float s) {\n\tfloat strength = 7. + .03 * log(1.e-6 + fract(sin(iTime) * 4373.11));\n\tfloat accum = s/4.;\n\tfloat prev = 0.;\n\tfloat tw = 0.;\n\tfor (int i = 0; i < 18; ++i) {\n\t\tfloat mag = dot(p, p);\n\t\tp = abs(p) / mag + vec3(-.5, -.4, -1.5);\n\t\tfloat w = exp(-float(i) / 7.);\n\t\taccum += w * exp(-strength * pow(abs(mag - prev), 2.2));\n\t\ttw += w;\n\t\tprev = mag;\n\t}\n\treturn max(0., 5. * accum / tw - .7);\n}\n\nvec3 nrand3( vec2 co )\n{\n\tvec3 a = fract( cos( co.x*8.3e-3 + co.y )*vec3(1.3e5, 4.7e5, 2.9e5) );\n\tvec3 b = fract( sin( co.x*0.3e-3 + co.y )*vec3(8.1e5, 1.0e5, 0.1e5) );\n\tvec3 c = mix(a, b, 0.5);\n\treturn c;\n}\n\n\nvoid mainImage( out vec4 fragColor, in vec2 fragCoord ) {\n    vec2 uv = 2. * fragCoord.xy / iResolution.xy - 1.;\n\tvec2 uvs = uv * iResolution.xy / max(iResolution.x, iResolution.y);\n\tvec3 p = vec3(uvs / 4., 0) + vec3(1., -1.3, 0.);\n\tp += .2 * vec3(sin(iTime / 16.), sin(iTime / 12.),  sin(iTime / 128.));\n\t\n\tfloat freqs[4];\n\t//Sound\n\tfreqs[0] = 0.5;\n\tfreqs[1] = 0.1;\n\tfreqs[2] = 0.4;\n\tfreqs[3] = 0.5;\n\n\tfloat t = field(p,freqs[2]);\n\tfloat v = (1. - exp((abs(uv.x) - 1.) * 6.)) * (1. - exp((abs(uv.y) - 1.) * 6.));\n\t\n    //Second Layer\n\tvec3 p2 = vec3(uvs / (4.+sin(iTime*0.11)*0.2+0.2+sin(iTime*0.15)*0.3+0.4), 1.5) + vec3(2., -1.3, -1.);\n\tp2 += 0.25 * vec3(sin(iTime / 16.), sin(iTime / 12.),  sin(iTime / 128.));\n\tfloat t2 = field2(p2,freqs[3]);\n\tvec4 c2 = mix(.4, 1., v) * vec4(1.3 * t2 * t2 * t2 ,1.8  * t2 * t2 , t2* freqs[0], t2);\n\t\n\t\n\t//Let's add some stars\n\t//Thanks to http://glsl.heroku.com/e#6904.0\n\tvec2 seed = p.xy * 2.0;\t\n\tseed = floor(seed * iResolution.x);\n\tvec3 rnd = nrand3( seed );\n\tvec4 starcolor = vec4(pow(rnd.y,40.0));\n\t\n\t//Second Layer\n\tvec2 seed2 = p2.xy * 2.0;\n\tseed2 = floor(seed2 * iResolution.x);\n\tvec3 rnd2 = nrand3( seed2 );\n\tstarcolor += vec4(pow(rnd2.y,40.0));\n\t\n\tfragColor = mix(freqs[3]-.3, 1., v) * vec4(1.5*freqs[2] * t * t* t , 1.2*freqs[1] * t * t, freqs[3]*t, 1.0)+c2+starcolor;\n}\n// --------[ Original ShaderToy ends here ]---------- //\n\nvoid main(void)\n{\n    mainImage(gl_FragColor, gl_FragCoord.xy);\n}";
    private final String vertShader = "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}";
    private BackgroundShader backgroundShader = new BackgroundShader("#ifdef GL_ES\nprecision mediump float;\n#endif\n\n// glslsandbox uniforms\nuniform float time;\nuniform vec2 resolution;\n\n// shadertoy emulation\n#define iTime time\n#define iResolution resolution\n\n\n// --------[ Original ShaderToy begins here ]---------- //\n//CBS\n//Parallax scrolling fractal galaxy.\n//Inspired by JoshP's Simplicity shader: https://www.shadertoy.com/view/lslGWr\n\n// http://www.fractalforums.com/new-theories-and-research/very-simple-formula-for-fractal-patterns/\nfloat field(in vec3 p,float s) {\n\tfloat strength = 7. + .03 * log(1.e-6 + fract(sin(iTime) * 4373.11));\n\tfloat accum = s/4.;\n\tfloat prev = 0.;\n\tfloat tw = 0.;\n\tfor (int i = 0; i < 26; ++i) {\n\t\tfloat mag = dot(p, p);\n\t\tp = abs(p) / mag + vec3(-.5, -.4, -1.5);\n\t\tfloat w = exp(-float(i) / 7.);\n\t\taccum += w * exp(-strength * pow(abs(mag - prev), 2.2));\n\t\ttw += w;\n\t\tprev = mag;\n\t}\n\treturn max(0., 5. * accum / tw - .7);\n}\n\n// Less iterations for second layer\nfloat field2(in vec3 p, float s) {\n\tfloat strength = 7. + .03 * log(1.e-6 + fract(sin(iTime) * 4373.11));\n\tfloat accum = s/4.;\n\tfloat prev = 0.;\n\tfloat tw = 0.;\n\tfor (int i = 0; i < 18; ++i) {\n\t\tfloat mag = dot(p, p);\n\t\tp = abs(p) / mag + vec3(-.5, -.4, -1.5);\n\t\tfloat w = exp(-float(i) / 7.);\n\t\taccum += w * exp(-strength * pow(abs(mag - prev), 2.2));\n\t\ttw += w;\n\t\tprev = mag;\n\t}\n\treturn max(0., 5. * accum / tw - .7);\n}\n\nvec3 nrand3( vec2 co )\n{\n\tvec3 a = fract( cos( co.x*8.3e-3 + co.y )*vec3(1.3e5, 4.7e5, 2.9e5) );\n\tvec3 b = fract( sin( co.x*0.3e-3 + co.y )*vec3(8.1e5, 1.0e5, 0.1e5) );\n\tvec3 c = mix(a, b, 0.5);\n\treturn c;\n}\n\n\nvoid mainImage( out vec4 fragColor, in vec2 fragCoord ) {\n    vec2 uv = 2. * fragCoord.xy / iResolution.xy - 1.;\n\tvec2 uvs = uv * iResolution.xy / max(iResolution.x, iResolution.y);\n\tvec3 p = vec3(uvs / 4., 0) + vec3(1., -1.3, 0.);\n\tp += .2 * vec3(sin(iTime / 16.), sin(iTime / 12.),  sin(iTime / 128.));\n\t\n\tfloat freqs[4];\n\t//Sound\n\tfreqs[0] = 0.5;\n\tfreqs[1] = 0.1;\n\tfreqs[2] = 0.4;\n\tfreqs[3] = 0.5;\n\n\tfloat t = field(p,freqs[2]);\n\tfloat v = (1. - exp((abs(uv.x) - 1.) * 6.)) * (1. - exp((abs(uv.y) - 1.) * 6.));\n\t\n    //Second Layer\n\tvec3 p2 = vec3(uvs / (4.+sin(iTime*0.11)*0.2+0.2+sin(iTime*0.15)*0.3+0.4), 1.5) + vec3(2., -1.3, -1.);\n\tp2 += 0.25 * vec3(sin(iTime / 16.), sin(iTime / 12.),  sin(iTime / 128.));\n\tfloat t2 = field2(p2,freqs[3]);\n\tvec4 c2 = mix(.4, 1., v) * vec4(1.3 * t2 * t2 * t2 ,1.8  * t2 * t2 , t2* freqs[0], t2);\n\t\n\t\n\t//Let's add some stars\n\t//Thanks to http://glsl.heroku.com/e#6904.0\n\tvec2 seed = p.xy * 2.0;\t\n\tseed = floor(seed * iResolution.x);\n\tvec3 rnd = nrand3( seed );\n\tvec4 starcolor = vec4(pow(rnd.y,40.0));\n\t\n\t//Second Layer\n\tvec2 seed2 = p2.xy * 2.0;\n\tseed2 = floor(seed2 * iResolution.x);\n\tvec3 rnd2 = nrand3( seed2 );\n\tstarcolor += vec4(pow(rnd2.y,40.0));\n\t\n\tfragColor = mix(freqs[3]-.3, 1., v) * vec4(1.5*freqs[2] * t * t* t , 1.2*freqs[1] * t * t, freqs[3]*t, 1.0)+c2+starcolor;\n}\n// --------[ Original ShaderToy ends here ]---------- //\n\nvoid main(void)\n{\n    mainImage(gl_FragColor, gl_FragCoord.xy);\n}", "#version 330 core\r\n\r\nin vec3 position;\r\n\r\nvoid main()\r\n{\r\n\tgl_Position = vec4(position, 1.0);\r\n}");

    public GuiPrixGen(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.backgroundShader.draw();
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
}

