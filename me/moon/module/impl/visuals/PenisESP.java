/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.Sphere
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class PenisESP
extends Module {
    private final NumberValue<Float> selfLength = new NumberValue<Float>("SelfLength", Float.valueOf(0.8f), Float.valueOf(0.1f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    private final NumberValue<Float> friendLength = new NumberValue<Float>("FriendLength", Float.valueOf(0.8f), Float.valueOf(0.1f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    private final NumberValue<Float> enemyLength = new NumberValue<Float>("EnemyLength", Float.valueOf(0.4f), Float.valueOf(0.1f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    private final BooleanValue uncircumcised = new BooleanValue("Uncircumcised", false);

    public PenisESP() {
        super("PenisESP", Module.Category.VISUALS, new Color(92, 52, 46, 255).getRGB());
    }

    @Handler(value=Render3DEvent.class)
    public void render3d(Render3DEvent event) {
        for (Entity ent : this.mc.theWorld.loadedEntityList) {
            if (!(ent instanceof EntityPlayer) || ent.isInvisible()) continue;
            EntityPlayer player = (EntityPlayer)ent;
            double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)this.mc.timer.renderPartialTicks;
            double x = n - this.mc.getRenderManager().renderPosX;
            double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)this.mc.timer.renderPartialTicks;
            double y = n2 - this.mc.getRenderManager().renderPosY;
            double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks;
            double z = n3 - this.mc.getRenderManager().renderPosZ;
            GL11.glPushMatrix();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable((int)2896);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)2848);
            GL11.glDepthMask((boolean)false);
            GL11.glLineWidth((float)1.0f);
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glRotatef((float)(-player.rotationYaw), (float)0.0f, (float)player.height, (float)0.0f);
            GL11.glTranslated((double)(-x), (double)(-y), (double)(-z));
            GL11.glTranslated((double)x, (double)(y + (double)(player.height / 2.0f) - (double)0.225f), (double)z);
            GL11.glColor4f((float)0.5882353f, (float)0.29411766f, (float)0.0f, (float)1.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)0.075f);
            Cylinder shaft = new Cylinder();
            shaft.setDrawStyle(100012);
            shaft.draw(0.1f, 0.11f, player == this.mc.thePlayer ? ((Float)this.selfLength.getValue()).floatValue() : (Moon.INSTANCE.getFriendManager().isFriend(player.getName()) ? ((Float)this.friendLength.getValue()).floatValue() : ((Float)this.enemyLength.getValue()).floatValue()), 25, 20);
            GL11.glColor4f((float)0.6082353f, (float)0.31411764f, (float)0.0f, (float)1.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)0.025);
            GL11.glTranslated((double)-0.09, (double)0.0, (double)0.0);
            Sphere right = new Sphere();
            right.setDrawStyle(100012);
            right.draw(0.14f, 10, 20);
            GL11.glTranslated((double)0.16, (double)0.0, (double)0.0);
            Sphere left = new Sphere();
            left.setDrawStyle(100012);
            left.draw(0.14f, 10, 20);
            GL11.glColor4f((float)0.3882353f, (float)0.15411764f, (float)0.0f, (float)1.0f);
            GL11.glTranslated((double)-0.07, (double)0.0, (double)((double)(player == this.mc.thePlayer ? (Float)this.selfLength.getValue() : (Moon.INSTANCE.getFriendManager().isFriend(player.getName()) ? (Float)this.friendLength.getValue() : (Float)this.enemyLength.getValue())).floatValue() - (this.uncircumcised.isEnabled() ? 0.15 : 0.0)));
            Sphere tip = new Sphere();
            tip.setDrawStyle(100012);
            tip.draw(0.13f, 15, 20);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)2848);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

