/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.other;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import me.moon.utils.render.GLUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class MurderMystery
extends Module {
    private final ArrayList<EntityPlayer> murderers = new ArrayList();
    private final ArrayList<EntityPlayer> gudpeople = new ArrayList();
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

    public MurderMystery() {
        super("MurderMystery", Module.Category.OTHER, new Color(62, 131, 227, 255).getRGB());
        this.setDescription("Shows all murderers and good guys.");
    }

    @Override
    public void onDisable() {
        this.murderers.clear();
        this.gudpeople.clear();
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        double z;
        double y;
        double x;
        for (EntityPlayer murderer : this.murderers) {
            x = murderer.lastTickPosX + (murderer.posX - murderer.lastTickPosX) * (double)event.getPartialTicks();
            y = murderer.lastTickPosY + (murderer.posY - murderer.lastTickPosY) * (double)event.getPartialTicks();
            z = murderer.lastTickPosZ + (murderer.posZ - murderer.lastTickPosZ) * (double)event.getPartialTicks();
            this.drawEntityESP(x - this.mc.getRenderManager().renderPosX, y - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ, (double)murderer.height - 0.1, (double)murderer.width - 0.12, new Color(14890790));
        }
        for (EntityPlayer good : this.gudpeople) {
            x = good.lastTickPosX + (good.posX - good.lastTickPosX) * (double)event.getPartialTicks();
            y = good.lastTickPosY + (good.posY - good.lastTickPosY) * (double)event.getPartialTicks();
            z = good.lastTickPosZ + (good.posZ - good.lastTickPosZ) * (double)event.getPartialTicks();
            this.drawEntityESP(x - this.mc.getRenderManager().renderPosX, y - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ, (double)good.height - 0.1, (double)good.width - 0.12, new Color(0x3E83E3));
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.getCurrentServerData() != null && this.mc.theWorld != null) {
            for (Object entities : this.mc.theWorld.loadedEntityList) {
                if (!(entities instanceof EntityPlayer)) continue;
                EntityPlayer entity = (EntityPlayer)entities;
                if (this.murderers.contains(entity) && !entity.isEntityAlive()) {
                    this.murderers.remove(entity);
                }
                if (this.gudpeople.contains(entity) && !entity.isEntityAlive()) {
                    this.gudpeople.remove(entity);
                }
                if (entity == this.mc.thePlayer || entity.isDead) continue;
                if (!(this.murderers.contains(entity) || entity.getHeldItem() == null || entity.getHeldItem().getItem() instanceof ItemMap || entity.getHeldItem().getItem() instanceof ItemBow || entity.getHeldItem().getItem() instanceof ItemBed || entity.getHeldItem().getItem() == Items.gold_ingot || entity.getHeldItem().getItem() == Items.arrow || entity.getHeldItem().getItem() == Items.dye || entity.getHeldItem().getItem() instanceof ItemPotion || entity.getHeldItem().getItem() instanceof ItemBlock)) {
                    Printer.print((Object)((Object)EnumChatFormatting.RED) + entity.getName() + " might be a murderer watch out!");
                    this.murderers.add(entity);
                }
                if (this.gudpeople.contains(entity) || this.murderers.contains(entity) || entity.getHeldItem() == null || !(entity.getHeldItem().getItem() instanceof ItemBow)) continue;
                Printer.print((Object)((Object)EnumChatFormatting.BLUE) + entity.getName() + " has a bow.");
                this.gudpeople.add(entity);
            }
        }
        if (this.mc.thePlayer.isDead) {
            this.gudpeople.clear();
            this.murderers.clear();
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat)event.getPacket();
            for (String str : this.nigga) {
                if (!packet.getChatComponent().getUnformattedText().contains(str)) continue;
                this.gudpeople.clear();
                this.murderers.clear();
            }
        }
    }

    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask((boolean)true);
        RenderUtil.BB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), 1.0f, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

