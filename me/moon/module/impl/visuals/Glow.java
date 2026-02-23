/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import java.nio.FloatBuffer;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEventPost;
import me.moon.module.Module;
import me.moon.module.impl.visuals.ChestESP;
import me.moon.module.impl.visuals.ESP;
import me.moon.utils.render.BloomUtils;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.ShaderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class Glow
extends Module {
    public final EnumValue<mode> modes = new EnumValue<mode>("Shader Mode", mode.FADING);
    public final NumberValue<Float> radius = new NumberValue<Float>("Shader Radius", Float.valueOf(5.5f), Float.valueOf(1.0f), Float.valueOf(50.0f), Float.valueOf(1.0f));
    public final NumberValue<Float> fading = new NumberValue<Float>("Shader Fading", Float.valueOf(180.0f), Float.valueOf(1.0f), Float.valueOf(1000.0f), Float.valueOf(1.0f), this.modes, "Fading");
    public final NumberValue<Float> alpha = new NumberValue<Float>("Glow Alpha", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(3.0f), Float.valueOf(0.1f), this.modes, "Gaussian");
    public final NumberValue<Integer> sigma = new NumberValue<Integer>("Blur Strength", 4, 1, 60, 1, this.modes, "Gaussian");
    public final BooleanValue overrideTexture = new BooleanValue("Override Texture", true, (Value)this.modes, "Gaussian");
    public final BooleanValue blurTwice = new BooleanValue("Blur Twice", false, (Value)this.modes, "Gaussian");
    public final BooleanValue gammaCorrection = new BooleanValue("Gamma Correction", false, (Value)this.modes, "Gaussian");
    public final ColorValue colorShader = new ColorValue("Glow Color", -9109467);
    private Framebuffer entityFramebuffer;
    private Framebuffer blurFramebuffer;
    private Framebuffer outlineFramebuffer;
    private Float radiusLast;
    private int sigmaLast;
    private static int lastScale;
    private static int lastWidth;
    private static int lastHeight;
    private ShaderUtil fadingShader = new ShaderUtil("fading_outline.fsh", "vertex_shader.vsh");
    private ShaderUtil outlineShaderUtil = new ShaderUtil("entity_outline.fsh", "vertex_shader.vsh");
    private ShaderUtil blurShaderUtil = new ShaderUtil("gaussian_outline.fsh", "vertex_shader.vsh");

    public Glow() {
        super("Glow", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2d(Render2DEvent event) {
        this.mc.gameSettings.ofFastRender = false;
        this.setSuffix(((mode)((Object)this.modes.getValue())).getName());
        ESP espModule = (ESP)Moon.INSTANCE.getModuleManager().getModule("ESP");
        ScaledResolution scale = event.getScaledResolution();
        switch ((mode)((Object)this.modes.getValue())) {
            case FADING: {
                Minecraft.getMinecraft().entityRenderer.disableLightmap();
                RenderHelper.disableStandardItemLighting();
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glAlphaFunc((int)516, (float)0.0f);
                Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
                this.mc.getFramebuffer().bindFramebuffer(true);
                this.fadingShader.startShader();
                this.fadingUniformUpdate(this.fadingShader.getProgramId(), this.colorShader.getColor());
                this.fadingShader.drawQuads(this.entityFramebuffer);
                this.fadingShader.stopShader();
                GL11.glAlphaFunc((int)516, (float)0.1f);
                break;
            }
            case Gaussian: {
                if (this.blurFramebuffer != null && this.outlineFramebuffer != null) {
                    this.blurFramebuffer.framebufferClear();
                    this.outlineFramebuffer.framebufferClear();
                }
                Color espColor = this.colorShader.getColor();
                GlStateManager.enableBlend();
                GlStateManager.alphaFunc(516, 0.0f);
                this.outlineFramebuffer.bindFramebuffer(true);
                this.outlineShaderUtil.startShader();
                this.outlineUniformUpdate(this.outlineShaderUtil.getProgramId(), espColor, 1.0f, 0.0f);
                this.outlineShaderUtil.drawQuads(this.entityFramebuffer);
                this.outlineUniformUpdate(this.outlineShaderUtil.getProgramId(), espColor, 0.0f, 1.0f);
                this.outlineShaderUtil.drawQuads(this.entityFramebuffer);
                this.outlineShaderUtil.stopShader();
                this.outlineFramebuffer.unbindFramebuffer();
                GlStateManager.enableBlend();
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GlStateManager.disableAlpha();
                if (!this.blurTwice.getValue().booleanValue()) {
                    this.blurFramebuffer.bindFramebuffer(true);
                    this.blurShaderUtil.startShader();
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 1.0f, 0.0f);
                    this.blurShaderUtil.drawQuads(this.outlineFramebuffer);
                    this.blurShaderUtil.stopShader();
                    this.blurFramebuffer.unbindFramebuffer();
                    this.blurShaderUtil.startShader();
                    this.mc.getFramebuffer().bindFramebuffer(true);
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 0.0f, 1.0f);
                    GL13.glActiveTexture((int)34004);
                    GL11.glBindTexture((int)3553, (int)this.entityFramebuffer.framebufferTexture);
                    GL13.glActiveTexture((int)33984);
                    this.blurShaderUtil.drawQuads(this.blurFramebuffer);
                    this.blurShaderUtil.stopShader();
                } else {
                    this.blurFramebuffer.bindFramebuffer(true);
                    this.blurShaderUtil.startShader();
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 1.0f, 0.0f);
                    this.blurShaderUtil.drawQuads(this.outlineFramebuffer);
                    this.blurShaderUtil.stopShader();
                    this.blurFramebuffer.unbindFramebuffer();
                    this.blurFramebuffer.bindFramebuffer(true);
                    this.blurShaderUtil.startShader();
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 0.0f, 1.0f);
                    this.blurShaderUtil.drawQuads(this.blurFramebuffer);
                    this.blurShaderUtil.stopShader();
                    this.blurFramebuffer.unbindFramebuffer();
                    this.blurFramebuffer.bindFramebuffer(true);
                    this.blurShaderUtil.startShader();
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 1.0f, 0.0f);
                    this.blurShaderUtil.drawQuads(this.blurFramebuffer);
                    this.blurShaderUtil.stopShader();
                    this.blurFramebuffer.unbindFramebuffer();
                    this.blurShaderUtil.startShader();
                    this.mc.getFramebuffer().bindFramebuffer(true);
                    this.blurUniformUpdate(this.blurShaderUtil.getProgramId(), espColor, 0.0f, 1.0f);
                    GL13.glActiveTexture((int)34004);
                    GL11.glBindTexture((int)3553, (int)this.entityFramebuffer.framebufferTexture);
                    GL13.glActiveTexture((int)33984);
                    this.blurShaderUtil.drawQuads(this.blurFramebuffer);
                    this.blurShaderUtil.stopShader();
                }
                GlStateManager.resetColor();
                GlStateManager.alphaFunc(516, 0.1f);
            }
        }
        if (Moon.INSTANCE.getModuleManager().getModule("Bloom").isEnabled()) {
            BloomUtils.callBloom();
        }
    }

    @Handler(value=Render3DEventPost.class)
    public void onRender3d(Render3DEventPost event) {
        ScaledResolution scale = new ScaledResolution(this.mc);
        int factor = scale.getScaleFactor();
        int widthScaled = scale.getScaledWidth();
        int heightScaled = scale.getScaledHeight();
        switch ((mode)((Object)this.modes.getValue())) {
            case FADING: 
            case Gaussian: {
                Minecraft.getMinecraft().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                if (this.entityFramebuffer != null) {
                    this.entityFramebuffer.framebufferClear();
                }
                if (lastScale != factor || lastWidth != widthScaled || lastHeight != heightScaled || this.entityFramebuffer == null || this.blurFramebuffer == null || this.outlineFramebuffer == null) {
                    this.blurFramebuffer = this.outlineShaderUtil.createBuffer(this.blurFramebuffer);
                    this.outlineFramebuffer = this.outlineShaderUtil.createBuffer(this.outlineFramebuffer);
                    this.entityFramebuffer = this.outlineShaderUtil.createBuffer(this.entityFramebuffer);
                    lastScale = factor;
                    lastWidth = widthScaled;
                    lastHeight = heightScaled;
                }
                this.entityFramebuffer.framebufferClear();
                this.entityFramebuffer.bindFramebuffer(true);
                this.renderEntities();
                this.entityFramebuffer.unbindFramebuffer();
                RenderHelper.disableStandardItemLighting();
                Minecraft.getMinecraft().entityRenderer.disableLightmap();
                Minecraft.getMinecraft().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            }
        }
        lastScale = factor;
        lastWidth = widthScaled;
        lastHeight = heightScaled;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.outlineShaderUtil = new ShaderUtil("entity_outline.fsh", "vertex_shader.vsh");
        this.fadingShader = new ShaderUtil("fading_outline.fsh", "vertex_shader.vsh");
        this.blurShaderUtil = new ShaderUtil("gaussian_outline.fsh", "vertex_shader.vsh");
        this.radiusLast = Float.valueOf(0.0f);
        this.sigmaLast = 0;
    }

    public Framebuffer createBuffer(Framebuffer framebuffer) {
        if (framebuffer != null) {
            framebuffer.deleteFramebuffer();
        }
        framebuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        return framebuffer;
    }

    public void outlineUniformUpdate(int programID, Color color, float leftDir, float rightDir) {
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture"), (int)0);
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texelSize"), (float)(1.0f / (float)this.mc.displayWidth), (float)(1.0f / (float)this.mc.displayHeight));
        GL20.glUniform3f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"color"), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f));
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"radius"), (float)((Float)this.radius.getValue()).floatValue());
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"direction"), (float)leftDir, (float)rightDir);
    }

    public void renderEntities() {
        ESP espModule = (ESP)Moon.INSTANCE.getModuleManager().getModule("ESP");
        ChestESP chestESPModule = (ChestESP)Moon.INSTANCE.getModuleManager().getModule("ChestESP");
        RendererLivingEntity.renderNametags = false;
        if (espModule.shader.getValue().booleanValue()) {
            boolean before = this.mc.gameSettings.field_181151_V;
            this.mc.gameSettings.field_181151_V = false;
            this.mc.theWorld.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLivingBase && entity != this.mc.thePlayer && espModule.isValid((EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(entity)) {
                    this.mc.getRenderManager().renderEntitySimple((Entity)entity, this.mc.timer.renderPartialTicks);
                }
            });
            this.mc.gameSettings.field_181151_V = before;
        }
        if (chestESPModule.mode.getValue() == ChestESP.Mode.SHADER) {
            this.mc.theWorld.loadedTileEntityList.stream().filter(tileEntity -> tileEntity instanceof TileEntityChest).forEach(tileEntity -> {
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)tileEntity, this.mc.timer.renderPartialTicks, -1);
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            });
        }
        RendererLivingEntity.renderNametags = true;
    }

    public void fadingUniformUpdate(int programID, Color color) {
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture"), (int)0);
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texelSize"), (float)(1.0f / (float)Minecraft.getMinecraft().displayWidth), (float)(1.0f / (float)Minecraft.getMinecraft().displayHeight));
        GL20.glUniform3f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"color"), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f));
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"radius"), (float)((Float)this.radius.getValue()).floatValue());
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"fade"), (float)((Float)this.fading.getValue()).floatValue());
    }

    public void blurUniformUpdate(int programID, Color color, float leftDir, float rightDir) {
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture"), (int)0);
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture2"), (int)20);
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texelSize"), (float)(1.0f / (float)Minecraft.getMinecraft().displayWidth), (float)(1.0f / (float)Minecraft.getMinecraft().displayHeight));
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"direction"), (float)leftDir, (float)rightDir);
        GL20.glUniform3f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"color"), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f));
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"alpha"), (float)((Float)this.alpha.getValue()).floatValue());
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"sigma"), (int)((Integer)this.sigma.getValue() * 2));
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"overrideTexture"), (int)(this.overrideTexture.getValue() != false ? 1 : 0));
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"gammaCorrection"), (int)(this.gammaCorrection.getValue() != false ? 1 : 0));
        if (this.radiusLast != this.radius.getValue() || this.sigmaLast != (Integer)this.sigma.getValue()) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer((int)256);
            float blurRadius = (Integer)this.sigma.getValue() * 2;
            int i = 0;
            while ((float)i <= blurRadius) {
                buffer.put(this.getGaussianOffset(i, ((Integer)this.sigma.getValue()).intValue()));
                ++i;
            }
            buffer.rewind();
            GL20.glUniform1((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"weights"), (FloatBuffer)buffer);
            this.radiusLast = (Float)this.radius.getValue();
            this.sigmaLast = (Integer)this.sigma.getValue();
        }
    }

    private float getGaussianOffset(float x, float sigma) {
        float pow = x / sigma;
        return (float)(1.0 / ((double)Math.abs(sigma) * 2.50662827463) * Math.exp(-0.5 * (double)pow * (double)pow));
    }

    private static enum mode {
        FADING("Fading"),
        Gaussian("Gaussian");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

