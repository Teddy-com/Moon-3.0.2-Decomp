/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class NetGraph
extends Module {
    private TimerUtil outGoing = new TimerUtil();
    private TimerUtil inGoing = new TimerUtil();
    private int outGoingPackets;
    private int inGoingPackets;
    private ArrayList<Integer> outGoingCount = new ArrayList();
    private ArrayList<Integer> inGoingCount = new ArrayList();

    public NetGraph() {
        super("NetGraph", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        float xPosition = 100.0f;
        float yPosition = 10.0f;
        float width = 100.0f;
        float height = 40.0f;
        int allOutGoing = 0;
        int highestCountOut = 0;
        RenderUtil.drawBorderedRect(xPosition, yPosition, width, height, 0.5, -1, 0x21000000);
        RenderUtil.drawRect(xPosition, yPosition + 4.0f, width, 0.5, -1);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)2848);
        GL11.glBegin((int)3);
        for (int i = 0; i < this.outGoingCount.size(); ++i) {
            int packetCount = this.outGoingCount.get(i);
            allOutGoing += packetCount;
            if (packetCount > highestCountOut) {
                highestCountOut = packetCount;
            }
            GL11.glVertex2f((float)(xPosition + (float)i * (width / 35.0f)), (float)(yPosition + height + 7.0f - Math.min((float)packetCount * height / 4.0f, 42.0f)));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GL11.glPopMatrix();
        RenderUtil.drawRect(xPosition, (double)(yPosition + height) - Math.min(Math.floor((float)allOutGoing / 35.0f) * (double)height / 4.0, 35.0), width, 0.5, Color.green.getRGB());
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow(Math.floor((float)allOutGoing / 35.0f) + "/avg", (xPosition + width + 2.0f) * 2.0f, (float)(((double)(yPosition + height) - Math.min(Math.floor((float)allOutGoing / 35.0f) * (double)height / 4.0, 35.0)) * 2.0), -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow("Outgoing Packets | " + MathUtils.round((float)allOutGoing / 35.0f, 1) * 20.0 + "p/s | " + MathUtils.round((float)allOutGoing / 35.0f, 1) + "/avg", xPosition * 2.0f, (yPosition - 6.0f) * 2.0f, -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow(highestCountOut + "", (xPosition - (float)this.mc.fontRendererObj.getStringWidth(highestCountOut + "") / 2.0f - 2.0f) * 2.0f, (yPosition + 3.0f) * 2.0f, -1);
        GL11.glPopMatrix();
        float xPositionIn = xPosition + width + 35.0f;
        int allInGoing = 0;
        int highestCount = 0;
        RenderUtil.drawBorderedRect(xPositionIn, yPosition, width, height, 0.5, -1, 0x21000000);
        RenderUtil.drawRect(xPositionIn, yPosition + 4.0f, width, 0.5, -1);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)2848);
        GL11.glBegin((int)3);
        for (int i = 0; i < this.inGoingCount.size(); ++i) {
            int packetCount = this.inGoingCount.get(i);
            allInGoing += packetCount;
            if (packetCount > highestCount) {
                highestCount = packetCount;
            }
            GL11.glVertex2f((float)(xPositionIn + (float)i * (width / 35.0f)), (float)(yPosition + height - Math.min((float)packetCount * height / 70.0f, 35.0f)));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GL11.glPopMatrix();
        float substraction = (float)Math.min(Math.floor((float)allInGoing / 35.0f) * (double)height / 70.0, 35.0);
        RenderUtil.drawRect(xPositionIn, yPosition + height - substraction, width, 0.5, Color.green.getRGB());
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow(Math.floor((float)allInGoing / 35.0f) + "/avg", (xPositionIn + width + 2.0f) * 2.0f, (yPosition + height - substraction) * 2.0f, -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow("Ingoing Packets | " + MathUtils.round((float)allInGoing / 35.0f, 1) * 20.0 + "p/s | " + MathUtils.round((float)allInGoing / 35.0f, 1) + "/avg", xPositionIn * 2.0f, (yPosition - 6.0f) * 2.0f, -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
        this.mc.fontRendererObj.drawStringWithShadow(highestCount + "", (xPositionIn - (float)this.mc.fontRendererObj.getStringWidth(highestCount + "") / 2.0f - 2.0f) * 2.0f, (yPosition + 3.0f) * 2.0f, -1);
        GL11.glPopMatrix();
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            ++this.outGoingPackets;
            if (this.outGoing.hasReached(50L)) {
                if (this.outGoingCount.size() > 35) {
                    this.outGoingCount.remove(0);
                }
                this.outGoingCount.add(this.outGoingPackets);
                this.outGoingPackets = 0;
                this.outGoing.reset();
            }
        } else {
            ++this.inGoingPackets;
            if (this.inGoing.hasReached(50L)) {
                if (this.inGoingCount.size() > 35) {
                    this.inGoingCount.remove(0);
                }
                this.inGoingCount.add(this.inGoingPackets);
                this.inGoingPackets = 0;
                this.inGoing.reset();
            }
        }
    }
}

