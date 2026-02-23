/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.ghost;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.input.ClickMouseEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.location.MoonLocation;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class BackTracing
extends Module {
    public static final NumberValue<Long> size = new NumberValue<Long>("Size", 25L, 1L, 50L, 1L);
    private final List<MoonLocation> recentLoc;
    private Entity lastTarget;
    private final ArrayList<Vector3d> locations = new ArrayList();

    public BackTracing() {
        super("BackTracing", Module.Category.GHOST, new Color(10789534).getRGB());
        this.setDescription("Increase your reach");
        this.recentLoc = new ArrayList<MoonLocation>();
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S14PacketEntity.S15PacketEntityRelMove && this.lastTarget != null) {
                // empty if block
            }
        } else if (event.getPacket() instanceof C02PacketUseEntity) {
            if (((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.INTERACT_AT || ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            } else {
                Entity target = ((C02PacketUseEntity)event.getPacket()).getEntityFromWorld(this.mc.theWorld);
                if (this.lastTarget != target) {
                    this.lastTarget = target;
                    this.recentLoc.clear();
                    this.locations.clear();
                }
            }
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender(Render3DEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        if (!this.locations.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glLineWidth((float)3.0f);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glColor3d((double)55.0, (double)218.0, (double)240.0);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glBegin((int)3);
            this.locations.forEach(vector -> GL11.glVertex3d((double)(vector.x - this.mc.getRenderManager().renderPosX), (double)(vector.y - this.mc.getRenderManager().renderPosY), (double)(vector.z - this.mc.getRenderManager().renderPosZ)));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && this.lastTarget != null && this.mc.theWorld != null) {
            MoonLocation moonLocation = new MoonLocation(this.lastTarget.getEntityWorld(), this.lastTarget.posX, this.lastTarget.posY, this.lastTarget.posZ, 0.0f, 0.0f);
            this.recentLoc.add(moonLocation);
            this.locations.add(new Vector3d(moonLocation.getX(), moonLocation.getY(), moonLocation.getZ()));
            if ((long)this.recentLoc.size() >= (Long)size.getValue()) {
                this.recentLoc.remove(0);
                this.locations.remove(0);
            }
        }
    }

    @Handler(value=ClickMouseEvent.class)
    public void onUpdate(ClickMouseEvent event) {
        if (this.mc.pointedEntity == null) {
            MoonLocation attackerLoc = new MoonLocation(this.mc.thePlayer.getEntityWorld(), this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, 0.0f, 0.0f);
            if (this.recentLoc != null && !this.mc.thePlayer.isBlocking()) {
                Vec3 vec = new Vec3((double)0.4f - MathUtils.getRandomInRange(0.01, 0.01), 0.63241 - MathUtils.getRandomInRange(0.01, 0.05), 0.321421 - MathUtils.getRandomInRange(0.01, 0.05));
                for (int i = this.recentLoc.size() - 1; i >= 0; --i) {
                    MoonLocation backTraced = this.recentLoc.get(i);
                    double distance = Math.hypot(backTraced.getX() - this.mc.thePlayer.posX, backTraced.getZ() - this.mc.thePlayer.posZ);
                    if (!(distance <= 3.4)) continue;
                    this.mc.objectMouseOver = new MovingObjectPosition(this.lastTarget, vec);
                    this.mc.pointedEntity = this.lastTarget;
                    break;
                }
            }
        }
    }
}

