/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector4d
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.font.Fonts;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.FontValue;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Waypoints
extends Module {
    public BooleanValue font = new BooleanValue("Font", "Enable Custom Font", true);
    public FontValue fontValue = new FontValue("WaypointFont", new MCFontRenderer(new Font("Arial", 0, 16), true, true));

    public Waypoints() {
        super("Waypoints", Module.Category.VISUALS, new Color(8773345).getRGB());
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        if (!this.mc.isSingleplayer() && this.mc.getCurrentServerData() != null) {
            Moon.INSTANCE.getWaypointManager().getWaypoints().forEach(waypoint -> {
                if (this.mc.getCurrentServerData().serverIP.equals(waypoint.getServer()) && this.mc.thePlayer.dimension == waypoint.getDimension() || !this.isOnScreen(new Vec3(waypoint.getX(), waypoint.getY(), waypoint.getZ()))) {
                    double posX = waypoint.getX();
                    double posY = waypoint.getY();
                    double posZ = waypoint.getZ();
                    AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX - this.mc.getRenderManager().renderPosX, posY - this.mc.getRenderManager().renderPosY, posZ - this.mc.getRenderManager().renderPosZ);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(posX + bb.minX - bb.maxX + 0.5, posY, posZ + bb.minZ - bb.maxZ + 0.5), new Vector3d(posX + bb.maxX - bb.minX - 0.5, posY, posZ + bb.minZ - bb.maxZ + 0.5), new Vector3d(posX + bb.minX - bb.maxX + 0.5, posY, posZ + bb.maxZ - bb.minZ - 0.5), new Vector3d(posX + bb.maxX - bb.minX - 0.5, posY, posZ + bb.maxZ - bb.minZ - 0.5), new Vector3d(posX + bb.minX - bb.maxX + 0.5, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 0.5), new Vector3d(posX + bb.maxX - bb.minX - 0.5, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 0.5), new Vector3d(posX + bb.minX - bb.maxX + 0.5, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 0.5), new Vector3d(posX + bb.maxX - bb.minX - 0.5, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 0.5));
                    this.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - this.mc.getRenderManager().viewerPosX, vector.y - this.mc.getRenderManager().viewerPosY, vector.z - this.mc.getRenderManager().viewerPosZ);
                        if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                    this.mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        float x = (float)position.x;
                        float x2 = (float)position.z;
                        float y = (float)position.y;
                        String nametext = "(" + Math.round(this.mc.thePlayer.getDistance(waypoint.getX(), waypoint.getY(), waypoint.getZ())) + "m) " + waypoint.getLabel();
                        RenderUtil.drawRect((double)(x + (x2 - x) / 2.0f - (this.font.isEnabled() ? (float)Fonts.moon.getStringWidth(nametext) : (float)this.mc.fontRendererObj.getStringWidth(nametext) / 2.0f) / 2.0f) - 2.5, y - (float)(this.font.isEnabled() ? Fonts.moon.getHeight() + 6 : this.mc.fontRendererObj.FONT_HEIGHT), (this.font.isEnabled() ? Fonts.moon.getStringWidth(nametext) : this.mc.fontRendererObj.getStringWidth(nametext) / 2) + 5, this.font.isEnabled() ? Fonts.moon.getHeight() + 6 : this.mc.fontRendererObj.FONT_HEIGHT, new Color(0, 0, 0, 120).getRGB());
                        if (this.font.isEnabled()) {
                            Fonts.moon.drawStringWithShadow(nametext, x + (x2 - x) / 2.0f - (float)Fonts.moon.getStringWidth(nametext) / 2.0f, y - (float)Fonts.moon.getHeight() - 2.0f, -1);
                        } else {
                            GL11.glPushMatrix();
                            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                            this.mc.fontRendererObj.drawStringWithShadow(nametext, (x + (x2 - x) / 2.0f - (float)this.mc.fontRendererObj.getStringWidth(nametext) / 4.0f) * 2.0f, (y - (float)this.mc.fontRendererObj.FONT_HEIGHT / 2.0f - 2.0f) * 2.0f, -1);
                            GL11.glPopMatrix();
                            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        }
                        GL11.glPopMatrix();
                    }
                }
            });
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isOnScreen(Vec3 pos) {
        if (!(pos.xCoord > -1.0)) return false;
        if (!(pos.zCoord < 1.0)) return false;
        double d = pos.xCoord;
        int n = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d / (double)n >= 0.0)) return false;
        double d2 = pos.xCoord;
        int n2 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d2 / (double)n2 <= (double)Display.getWidth())) return false;
        double d3 = pos.yCoord;
        int n3 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d3 / (double)n3 >= 0.0)) return false;
        double d4 = pos.yCoord;
        int n4 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d4 / (double)n4 <= (double)Display.getHeight())) return false;
        return true;
    }
}

