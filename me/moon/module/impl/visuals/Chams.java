/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.moon.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEventPost;
import me.moon.event.impl.render.RenderEntityModelEvent;
import me.moon.module.Module;
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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Chams
extends Module {
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    public BooleanValue shader = new BooleanValue("Shader", false);
    public NumberValue<Float> shaderAlpha = new NumberValue<Float>("Shader Alpha", Float.valueOf(0.5f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.1f));
    private final BooleanValue teamColor = new BooleanValue("Team Color", false, (Value)this.shader, "true");
    public NumberValue<Float> mixingPercentage = new NumberValue<Float>("Mixing Percentage", Float.valueOf(0.5f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.1f), this.shader, "true");
    public ColorValue shaderColor = new ColorValue("Shader Color", new Color(0xFF0000).getRGB(), (Value)this.shader, "true");
    public EnumValue<Modes> chamsModes = new EnumValue<Modes>("Chams Modes", Modes.BRIGHT, (Value)this.shader, "false");
    public ColorValue visible = new ColorValue("Visible", new Color(0xFF0000).getRGB(), (Value)this.shader, "false");
    public ColorValue hidden = new ColorValue("Hidden", new Color(0xFF00FF).getRGB(), (Value)this.shader, "false");
    public BooleanValue blur = new BooleanValue("Blur", false);
    private Framebuffer entityFBO;
    private int lastWidth;
    private int lastHeight;
    private ShaderUtil shaderUtil = new ShaderUtil("chams_shader.fsh", "vertex_shader.vsh");

    public Chams() {
        super("Chams", Module.Category.VISUALS, new Color(10833407).getRGB());
        this.setDescription("Wallhacks");
    }

    public boolean isValid(EntityLivingBase entity) {
        return this.isValidType(entity) && entity instanceof EntityLivingBase && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }

    @Override
    public void onEnable() {
        this.shaderUtil = new ShaderUtil("chams_shader.fsh", "vertex_shader.vsh");
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    @Handler(value=Render3DEventPost.class)
    public void onRenderPost(Render3DEventPost eventPost) {
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(eventPost.getPartialTicks(), 0);
        ScaledResolution sr = new ScaledResolution(this.mc);
        int widthScaled = sr.getScaledWidth();
        int heightScaled = sr.getScaledHeight();
        if (this.lastWidth != widthScaled || this.lastHeight != heightScaled || this.entityFBO == null) {
            this.entityFBO = this.shaderUtil.createBuffer(this.entityFBO);
            this.lastWidth = widthScaled;
            this.lastHeight = heightScaled;
        }
        this.entityFBO.framebufferClear();
        this.entityFBO.bindFramebuffer(true);
        this.renderEntities();
        this.entityFBO.unbindFramebuffer();
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(eventPost.getPartialTicks(), 0);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        if (this.shader.getValue().booleanValue()) {
            GlStateManager.disableLighting();
            GlStateManager.alphaFunc(516, 0.0f);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            this.mc.getFramebuffer().bindFramebuffer(true);
            this.shaderUtil.startShader();
            GL20.glUniform1i((int)GL20.glGetUniformLocation((int)this.shaderUtil.getProgramId(), (CharSequence)"texture"), (int)0);
            GL20.glUniform1f((int)GL20.glGetUniformLocation((int)this.shaderUtil.getProgramId(), (CharSequence)"alpha"), (float)((Float)this.shaderAlpha.getValue()).floatValue());
            GL20.glUniform3f((int)GL20.glGetUniformLocation((int)this.shaderUtil.getProgramId(), (CharSequence)"color"), (float)((float)this.shaderColor.getColor().getRed() / 255.0f), (float)((float)this.shaderColor.getColor().getGreen() / 255.0f), (float)((float)this.shaderColor.getColor().getBlue() / 255.0f));
            GL20.glUniform1f((int)GL20.glGetUniformLocation((int)this.shaderUtil.getProgramId(), (CharSequence)"mixingPercentage"), (float)(this.teamColor.isEnabled() ? 1.0f : ((Float)this.mixingPercentage.getValue()).floatValue()));
            this.shaderUtil.drawQuads(this.entityFBO);
            this.shaderUtil.stopShader();
            GlStateManager.alphaFunc(516, 0.1f);
        }
    }

    @Handler(value=RenderEntityModelEvent.class)
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (this.shader.getValue().booleanValue() || !this.isValid(event.getEntityLivingBase()) || this.blur.getValue().booleanValue()) {
            return;
        }
        if (event.isPre()) {
            switch ((Modes)((Object)this.chamsModes.getValue())) {
                case FLAT: {
                    GlStateManager.pushMatrix();
                    GL11.glEnable((int)10754);
                    GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GlStateManager.color((float)this.hidden.getColor().getRed() / 255.0f, (float)this.hidden.getColor().getGreen() / 255.0f, (float)this.hidden.getColor().getBlue() / 255.0f, (float)this.hidden.getColor().getAlpha() / 255.0f);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.alphaFunc(516, 0.0f);
                    break;
                }
                case BRIGHT: {
                    GlStateManager.pushMatrix();
                    GL11.glEnable((int)10754);
                    GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                    GL11.glDisable((int)2896);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    RenderUtil.hexColor((Integer)this.hidden.getValue());
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.alphaFunc(516, 0.0f);
                }
            }
        } else {
            switch ((Modes)((Object)this.chamsModes.getValue())) {
                case FLAT: {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GlStateManager.color((float)this.visible.getColor().getRed() / 255.0f, (float)this.visible.getColor().getGreen() / 255.0f, (float)this.visible.getColor().getBlue() / 255.0f, (float)this.visible.getColor().getAlpha() / 255.0f);
                    event.getModel().render(event.getEntityLivingBase(), event.getP_77036_2_(), event.getP_77036_3_(), event.getP_77036_4_(), event.getP_77036_5_(), event.getP_77036_6_(), event.getP_77036_7_());
                    GL11.glEnable((int)3553);
                    GlStateManager.disableBlend();
                    GlStateManager.alphaFunc(516, 0.1f);
                    GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
                    GL11.glDisable((int)10754);
                    GlStateManager.popMatrix();
                    break;
                }
                case BRIGHT: {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    RenderUtil.hexColor((Integer)this.visible.getValue());
                    event.getModel().render(event.getEntityLivingBase(), event.getP_77036_2_(), event.getP_77036_3_(), event.getP_77036_4_(), event.getP_77036_5_(), event.getP_77036_6_(), event.getP_77036_7_());
                    GL11.glEnable((int)2896);
                    GL11.glEnable((int)3553);
                    GlStateManager.disableBlend();
                    GlStateManager.alphaFunc(516, 0.1f);
                    GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
                    GL11.glDisable((int)10754);
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public void blur() {
        if (!this.isEnabled()) {
            return;
        }
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase ent;
            if (entity instanceof EntityLivingBase && entity != this.mc.thePlayer && this.blur.getValue().booleanValue() && this.isValid(ent = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(ent)) {
                GL11.glPushMatrix();
                this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 0);
                RendererLivingEntity.renderNametags = false;
                RenderManager.renderFire = false;
                this.mc.getRenderManager().renderEntitySimple((Entity)entity, this.mc.timer.renderPartialTicks);
                RenderManager.renderFire = true;
                RendererLivingEntity.renderNametags = true;
                GlStateManager.resetColor();
                GlStateManager.bindTexture(0);
                this.mc.entityRenderer.setupOverlayRendering();
                GL11.glPopMatrix();
            }
        });
    }

    public void renderEntities() {
        GlStateManager.disableLighting();
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase target;
            if (entity instanceof EntityLivingBase && this.isValid(target = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(target) && entity != this.mc.thePlayer) {
                RendererLivingEntity.renderNametags = false;
                boolean prev = this.mc.gameSettings.field_181151_V;
                this.mc.gameSettings.field_181151_V = false;
                RenderManager.renderEnchant = false;
                if (this.teamColor.getValue().booleanValue()) {
                    this.mc.getRenderManager().setRenderOutlines(true);
                }
                this.mc.getRenderManager().renderEntitySimple((Entity)entity, this.mc.timer.renderPartialTicks);
                if (this.teamColor.getValue().booleanValue()) {
                    this.mc.getRenderManager().setRenderOutlines(false);
                }
                RenderManager.renderEnchant = true;
                this.mc.gameSettings.field_181151_V = prev;
                RendererLivingEntity.renderNametags = true;
            }
        });
        GlStateManager.enableLighting();
    }

    public static enum Modes {
        FLAT,
        TEXTURE,
        BRIGHT;

    }
}

