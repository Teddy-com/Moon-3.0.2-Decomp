/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.moon.module.impl.visuals;

import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.event.impl.render.Render3DEventPost;
import me.moon.module.Module;
import me.moon.utils.game.OutlineUtils;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.shadering.ShaderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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

public class OutlineESP
extends Module {
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    public final BooleanValue shader = new BooleanValue("Shader", false);
    private final BooleanValue teamColor = new BooleanValue("Team Color", false, (Value)this.shader, "true");
    private final ColorValue colorValue = new ColorValue("Outline Color", -1);
    private ShaderUtil outlineShader = new ShaderUtil("outline_shader.fsh", "vertex_shader.vsh");
    private Framebuffer entityFramebuffer;
    private int lastWidth;
    private int lastHeight;

    public OutlineESP() {
        super("OutlineESP", Module.Category.VISUALS, -1);
    }

    public ColorValue getColorValue() {
        return this.colorValue;
    }

    @Handler(value=Render3DEvent.class)
    public void render3d(Render3DEvent event) {
        if (this.shader.isEnabled()) {
            return;
        }
        float red = (float)this.colorValue.getColor().getRed() / 255.0f;
        float green = (float)this.colorValue.getColor().getGreen() / 255.0f;
        float blue = (float)this.colorValue.getColor().getBlue() / 255.0f;
        OutlineUtils.renderOne();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        this.drawEntitys();
        OutlineUtils.renderTwo();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        this.drawEntitys();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        OutlineUtils.renderThree();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        OutlineUtils.renderFour();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        this.drawEntitys();
        if (!this.teamColor.getValue().booleanValue()) {
            GL11.glColor3f((float)red, (float)green, (float)blue);
        }
        OutlineUtils.renderFive();
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        if (this.shader.getValue().booleanValue()) {
            this.mc.gameSettings.ofFastRender = false;
            int program = this.outlineShader.getProgramId();
            GlStateManager.enableBlend();
            this.mc.getFramebuffer().bindFramebuffer(true);
            this.outlineShader.startShader();
            GL20.glUniform1i((int)GL20.glGetUniformLocation((int)program, (CharSequence)"texture"), (int)0);
            GL20.glUniform2f((int)GL20.glGetUniformLocation((int)program, (CharSequence)"texelSize"), (float)(1.0f / (float)Minecraft.getMinecraft().displayWidth), (float)(1.0f / (float)Minecraft.getMinecraft().displayHeight));
            GL20.glUniform3f((int)GL20.glGetUniformLocation((int)program, (CharSequence)"color"), (float)((float)this.colorValue.getColor().getRed() / 255.0f), (float)((float)this.colorValue.getColor().getGreen() / 255.0f), (float)((float)this.colorValue.getColor().getBlue() / 255.0f));
            this.outlineShader.drawQuads(this.entityFramebuffer);
            this.outlineShader.stopShader();
        }
    }

    @Override
    public void onEnable() {
        this.outlineShader = new ShaderUtil("outline_shader.fsh", "vertex_shader.vsh");
    }

    @Handler(value=Render3DEventPost.class)
    public void onRender3DPost(Render3DEventPost eventPost) {
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(eventPost.getPartialTicks(), 0);
        ScaledResolution sr = new ScaledResolution(this.mc);
        int widthScaled = sr.getScaledWidth();
        int heightScaled = sr.getScaledHeight();
        if (this.lastWidth != widthScaled || this.lastHeight != heightScaled || this.entityFramebuffer == null) {
            this.entityFramebuffer = this.outlineShader.createBuffer(this.entityFramebuffer);
            this.lastWidth = widthScaled;
            this.lastHeight = heightScaled;
        }
        this.entityFramebuffer.framebufferClear();
        this.entityFramebuffer.bindFramebuffer(true);
        this.drawEntitys();
        this.entityFramebuffer.unbindFramebuffer();
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(eventPost.getPartialTicks(), 0);
    }

    public void drawEntitys() {
        GlStateManager.disableLighting();
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase target;
            if (entity instanceof EntityLivingBase && this.isValid(target = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(target)) {
                RendererLivingEntity.renderNametags = false;
                boolean prev = this.mc.gameSettings.field_181151_V;
                this.mc.gameSettings.field_181151_V = false;
                if (!this.shader.getValue().booleanValue()) {
                    if (this.teamColor.getValue().booleanValue()) {
                        this.mc.getRenderManager().setRenderOutlines(true);
                    }
                    RenderManager.renderFire = false;
                    RenderManager.renderLayers = false;
                }
                RenderManager.renderEnchant = false;
                this.mc.getRenderManager().renderEntitySimple((Entity)entity, this.mc.timer.renderPartialTicks);
                RenderManager.renderEnchant = true;
                if (!this.shader.getValue().booleanValue()) {
                    RenderManager.renderFire = true;
                    if (this.teamColor.getValue().booleanValue()) {
                        this.mc.getRenderManager().setRenderOutlines(false);
                    }
                    RenderManager.renderLayers = true;
                }
                this.mc.gameSettings.field_181151_V = prev;
                RendererLivingEntity.renderNametags = true;
            }
        });
        GlStateManager.enableLighting();
    }

    public boolean isValid(EntityLivingBase entity) {
        return this.mc.thePlayer != entity && this.isValidType(entity) && entity != null && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }
}

