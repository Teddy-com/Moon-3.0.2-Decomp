/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.location.MoonLocation;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class CopsNCrims
extends Module {
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 300, 0, 1000, 1);
    private final BooleanValue targetEsp = new BooleanValue("Target ESP", "Target ESP", true);
    public NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(70.0f), Float.valueOf(1.0f), Float.valueOf(150.0f), Float.valueOf(0.1f));
    private double counter;
    private boolean hasShot;
    private final TimerUtil time = new TimerUtil();
    private EntityLivingBase target;
    private final ArrayList<ShotLine> SHOT_LOCATION_LIST = new ArrayList();
    private final ArrayList<ShopEntity> SHOP_ENTITIES = new ArrayList();
    private int SELECTION_INDEX;
    private MoonLocation shotLocation;
    private MoonLocation playerLocation;

    public CopsNCrims() {
        super("CopsNCrims", Module.Category.COMBAT, new Color(11472384).getRGB());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.updateShop();
        if (event.isPre()) {
            this.target = null;
            ArrayList<EntityLivingBase> dynamicTargets = new ArrayList<EntityLivingBase>();
            this.mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> dynamicTargets.size() < 10 && this.isValid((EntityLivingBase)entity)).filter(entity -> this.isValidTarget((EntityLivingBase)entity)).forEach(potentialTarget -> dynamicTargets.add((EntityLivingBase)potentialTarget));
            if (!dynamicTargets.isEmpty() && this.target == null) {
                dynamicTargets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                dynamicTargets.stream().limit(3L).collect(Collectors.toList()).sort(Comparator.comparingDouble(CombatUtil::yawDist));
                dynamicTargets.sort(Comparator.comparingDouble(target -> this.mc.thePlayer.getDistanceToEntity((Entity)target)));
                this.target = (EntityLivingBase)dynamicTargets.get(0);
                if (this.target != null) {
                    if (this.counter > 5.0) {
                        this.counter = 0.0;
                    }
                    Vec3 aimPoint = this.getAimVector(this.target);
                    float[] rotations = this.getRotationsToEnt(new Vec3(aimPoint.xCoord, aimPoint.yCoord, aimPoint.zCoord), this.mc.thePlayer);
                    float yaw = rotations[0];
                    float pitch = rotations[1];
                    event.setYaw(yaw);
                    event.setPitch(pitch);
                    if (this.mc.thePlayer.inventory.getCurrentItem() != null && !this.mc.thePlayer.inventory.getCurrentItem().isItemDamaged() && (double)this.mc.thePlayer.experience >= 0.99 && !this.hasShot) {
                        event.setCancelled(true);
                        this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, yaw, pitch, this.mc.thePlayer.onGround));
                        this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getHeldItem()));
                        this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        this.shotLocation = new MoonLocation(this.mc.theWorld, aimPoint.xCoord, aimPoint.yCoord, aimPoint.zCoord);
                        this.playerLocation = new MoonLocation(this.mc.theWorld, this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);
                        ArrayList<MoonLocation> putLocations = new ArrayList<MoonLocation>();
                        putLocations.add(this.shotLocation);
                        putLocations.add(this.playerLocation);
                        this.SHOT_LOCATION_LIST.add(new ShotLine(putLocations, System.currentTimeMillis()));
                        this.hasShot = true;
                    }
                    if ((double)this.mc.thePlayer.experience <= 0.99 || this.mc.thePlayer.inventory.getCurrentItem() != null && this.mc.thePlayer.inventory.getCurrentItem().isItemDamaged() || this.time.hasReached(250L)) {
                        this.hasShot = false;
                        this.time.reset();
                    }
                }
            }
        }
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        Fonts.moonSmall.drawOutlinedString(Math.round(this.mc.thePlayer.getHealth()) + " HP", (float)sr.getScaledWidth() / 2.0f - 20.0f - (float)Fonts.moonSmall.getStringWidth(Math.round(this.mc.thePlayer.getHealth()) + " HP"), (float)sr.getScaledHeight() / 2.0f - (float)Fonts.moonSmall.getHeight() / 2.0f, -1);
        if (this.mc.thePlayer.inventory.getCurrentItem() != null) {
            boolean RELOAD_STATE = this.mc.thePlayer.inventory.getCurrentItem().isItemDamaged();
            String AMMO_COUNT = (RELOAD_STATE ? "..." : Integer.valueOf(this.mc.thePlayer.inventory.getCurrentItem().stackSize)) + " / " + (this.mc.thePlayer.experienceLevel - this.mc.thePlayer.inventory.getCurrentItem().stackSize);
            Fonts.moonSmall.drawOutlinedString(AMMO_COUNT, (float)sr.getScaledWidth() / 2.0f + 20.0f, (float)sr.getScaledHeight() / 2.0f - (float)Fonts.moonSmall.getHeight() / 2.0f, -1);
            if (RELOAD_STATE) {
                String RELOAD_ALERT = "\u00a7aReloading...";
                Fonts.moonSmall.drawOutlinedString(RELOAD_ALERT, (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moonSmall.getStringWidth(RELOAD_ALERT) / 2.0f, (float)sr.getScaledHeight() / 2.0f + 20.0f, -1);
            }
            if (this.mc.thePlayer.experienceLevel < 60) {
                String LAMMO_ALERT = "\u00a7cLow Ammo!";
                Fonts.moonSmall.drawOutlinedString(LAMMO_ALERT, (float)sr.getScaledWidth() / 2.0f - (float)Fonts.moonSmall.getStringWidth(LAMMO_ALERT) / 2.0f, (float)sr.getScaledHeight() / 2.0f - 20.0f - (float)Fonts.moonSmall.getHeight(), -1);
            }
        }
        if (this.SHOP_ENTITIES.size() > 0) {
            float width = 0.0f;
            float height = (float)(this.SHOP_ENTITIES.size() * (this.mc.fontRendererObj.FONT_HEIGHT + 2)) + 1.0f;
            for (ShopEntity shopEntity : this.SHOP_ENTITIES) {
                FontRenderer fontRenderer = this.mc.fontRendererObj;
                StringBuilder stringBuilder = new StringBuilder();
                String string = this.SHOP_ENTITIES.indexOf(shopEntity) == this.SELECTION_INDEX ? "\u00a77> " : "";
                float entityWidth = fontRenderer.getStringWidth(stringBuilder.append(string).append(shopEntity.name).append(" \u00a77- ").append(shopEntity.price).toString());
                if (!(entityWidth > width)) continue;
                width = entityWidth + 6.0f;
            }
            RenderUtil.drawBorderedRect(2.0, (float)sr.getScaledHeight() / 2.0f - height / 2.0f - (float)this.mc.fontRendererObj.FONT_HEIGHT - 3.0f, width, height + (float)this.mc.fontRendererObj.FONT_HEIGHT + 3.0f, 1.0, -14606047, -1155456735);
            RenderUtil.drawRect(2.0, (float)sr.getScaledHeight() / 2.0f - height / 2.0f - (float)this.mc.fontRendererObj.FONT_HEIGHT - 3.0f, width, this.mc.fontRendererObj.FONT_HEIGHT + 3, -14606047);
            this.mc.fontRendererObj.drawOutlinedString("\u00a7e\u00a7lRemote Buy", 5.0f, (float)sr.getScaledHeight() / 2.0f - height / 2.0f - (float)this.mc.fontRendererObj.FONT_HEIGHT - 1.0f, 0.5f, -1);
            float y = (float)sr.getScaledHeight() / 2.0f - height / 2.0f + 1.0f;
            for (ShopEntity shopEntity : this.SHOP_ENTITIES) {
                this.mc.fontRendererObj.drawOutlinedString((this.SHOP_ENTITIES.indexOf(shopEntity) == this.SELECTION_INDEX ? "\u00a77> " : "") + shopEntity.name + " \u00a77- " + shopEntity.price, 5.0f, y + 1.0f, 0.5f, -1);
                y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
            }
        }
    }

    @Handler(value=KeyInputEvent.class)
    public void onKeyInput(KeyInputEvent event) {
        if (this.SHOP_ENTITIES.size() > 0) {
            switch (event.getKey()) {
                case 208: {
                    ++this.SELECTION_INDEX;
                    break;
                }
                case 200: {
                    --this.SELECTION_INDEX;
                    break;
                }
                case 28: {
                    ShopEntity shopEntity = this.SHOP_ENTITIES.get(this.SELECTION_INDEX);
                    Vec3 pos = shopEntity.positionVector;
                    this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(pos.xCoord, pos.yCoord, pos.zCoord, true));
                    this.mc.playerController.attackEntity(this.mc.thePlayer, shopEntity.attackEntity);
                }
            }
        }
        if (this.SELECTION_INDEX >= this.SHOP_ENTITIES.size()) {
            this.SELECTION_INDEX = 0;
        }
        if (this.SELECTION_INDEX < 0) {
            this.SELECTION_INDEX = this.SHOP_ENTITIES.size() - 1;
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase) || !this.isValid((EntityLivingBase)entity) || entity == this.mc.thePlayer) continue;
            Vec3 renderLocation = this.getAimVector((EntityLivingBase)entity);
            this.drawBlock(renderLocation.xCoord - this.mc.getRenderManager().renderPosX, renderLocation.yCoord - this.mc.getRenderManager().renderPosY - 0.25, renderLocation.zCoord - this.mc.getRenderManager().renderPosZ, 0.5, 0.5, new Color(255, 255, 255));
        }
        if (this.shotLocation != null && this.playerLocation != null) {
            try {
                if (this.mc.thePlayer.ticksExisted == 0) {
                    for (ShotLine shotLine : this.SHOT_LOCATION_LIST) {
                        ArrayList<MoonLocation> locations = shotLine.getLocations();
                        Long locationMillis = shotLine.currentTime;
                        Long diff = System.currentTimeMillis() - locationMillis;
                        GL11.glPushMatrix();
                        GL11.glLineWidth((float)3.0f);
                        GL11.glDepthMask((boolean)false);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)3008);
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)Math.max((float)(5000L - diff) / 5000.0f, 1.0E-14f));
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        GL11.glBegin((int)3);
                        locations.forEach(vector -> GL11.glVertex3d((double)(vector.getX() - this.mc.getRenderManager().renderPosX), (double)(vector.getY() - this.mc.getRenderManager().renderPosY), (double)(vector.getZ() - this.mc.getRenderManager().renderPosZ)));
                        GL11.glEnd();
                        GL11.glEnable((int)2896);
                        GL11.glEnable((int)3008);
                        GL11.glDisable((int)2848);
                        GL11.glEnable((int)3553);
                        GL11.glDepthMask((boolean)true);
                        GL11.glPopMatrix();
                    }
                }
                for (ShotLine shotLine : this.SHOT_LOCATION_LIST) {
                    Long LOCATION_MILLIS = shotLine.currentTime;
                    Long MILLIS_DIFF = System.currentTimeMillis() - LOCATION_MILLIS;
                    if (MILLIS_DIFF <= 5000L) continue;
                    this.SHOT_LOCATION_LIST.remove(shotLine);
                }
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
    }

    private float[] getRotationsToEnt(Vec3 ent, EntityPlayerSP playerSP) {
        double differenceX = ent.xCoord - playerSP.posX;
        double differenceY = ent.yCoord - (playerSP.posY + (double)playerSP.height);
        double differenceZ = ent.zCoord - playerSP.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(this.target)) * 180.0 / Math.PI);
        float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        float pitchNew = MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(differenceY, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ)))));
        return new float[]{finishedYaw, pitchNew};
    }

    private void drawBlock(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), 1.0f, color.getRGB());
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public boolean isValidTarget(EntityLivingBase p) {
        return p.ticksExisted > 20 && p.getUniqueID() != this.mc.thePlayer.getUniqueID() && p.isEntityAlive() && !p.isDead && this.mc.thePlayer.canEntityBeSeen(p) && !p.isInvisible() && this.isOnEnemyTeam(p) && !Moon.INSTANCE.getFriendManager().isFriend(p.getName()) && this.mc.thePlayer.getDistanceToEntity(p) < ((Float)this.range.getValue()).floatValue();
    }

    public boolean isOnEnemyTeam(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer target = (EntityPlayer)entity;
            boolean teamchecks = false;
            ChatFormatting myCol = null;
            ChatFormatting enemyCol = null;
            if (target != null) {
                for (ChatFormatting col : ChatFormatting.values()) {
                    if (col == ChatFormatting.RESET) continue;
                    if (this.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                        myCol = col;
                    }
                    if (!target.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
                    enemyCol = col;
                }
                try {
                    if (myCol != null && enemyCol != null) {
                        teamchecks = myCol != enemyCol;
                    } else if (this.mc.thePlayer.getTeam() != null) {
                        teamchecks = !this.mc.thePlayer.isOnSameTeam(target);
                    } else if (this.mc.thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                        teamchecks = !ItemStack.areItemStacksEqual(this.mc.thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return teamchecks;
        }
        return true;
    }

    public boolean isValid(EntityLivingBase entity) {
        return this.isValidType(entity) && entity != null && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityGhast) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem || entity instanceof EntitySquid) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }

    private Vec3 getAimVector(EntityLivingBase entityLivingBase) {
        float DIST_PRED_SCALE = this.mc.thePlayer.getDistanceToEntity(entityLivingBase) / 110.0f;
        float RECOIL_PRED_SCALE = (float)((double)DIST_PRED_SCALE * this.counter);
        float PRED_SCALE = 5.0f;
        double x = entityLivingBase.posX;
        double y = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight();
        double z = entityLivingBase.posZ;
        double mx = entityLivingBase.posX - entityLivingBase.lastTickPosX;
        double mz = entityLivingBase.posZ - entityLivingBase.lastTickPosZ;
        double my = entityLivingBase.posY - entityLivingBase.lastTickPosY;
        return new Vec3(x += mx * (double)(PRED_SCALE + DIST_PRED_SCALE), y += my * (double)(PRED_SCALE / 3.0f + DIST_PRED_SCALE), z += mz * (double)(PRED_SCALE + DIST_PRED_SCALE));
    }

    private void updateShop() {
        if (this.mc.thePlayer.ticksExisted % 20 == 0) {
            this.SHOP_ENTITIES.clear();
            block0: for (Entity lEntity : this.mc.theWorld.loadedEntityList) {
                if (!(lEntity instanceof EntityArmorStand) || !lEntity.getName().endsWith("Gold") && !lEntity.getName().contains("Right click") && !lEntity.getName().contains("RIGHT-CLICK") && !lEntity.getName().contains("RIGHT CLICK")) continue;
                for (Entity tEntity : this.mc.theWorld.loadedEntityList) {
                    if (lEntity == tEntity || !((double)tEntity.getDistanceToEntity(lEntity) <= 0.5)) continue;
                    ShopEntity entity = new ShopEntity(tEntity, lEntity, lEntity.getName().contains("Right click") || lEntity.getName().contains("RIGHT-CLICK") || lEntity.getName().contains("RIGHT CLICK"));
                    this.SHOP_ENTITIES.add(entity);
                    continue block0;
                }
            }
        }
    }

    private static class ShopEntity {
        private final Vec3 positionVector;
        private final String name;
        private final String price;
        private final Entity attackEntity;
        private final boolean rightClick;

        public ShopEntity(Entity tEntity, Entity lEntity, boolean rightClick) {
            this.positionVector = tEntity.getPositionVector();
            this.name = tEntity.getName();
            this.price = lEntity.getName();
            this.attackEntity = lEntity;
            this.rightClick = rightClick;
        }
    }

    private class ShotLine {
        public ArrayList<MoonLocation> locations;
        public Long currentTime;

        public ShotLine(ArrayList<MoonLocation> locations, Long currentTime) {
            this.locations = locations;
            this.currentTime = currentTime;
        }

        public ArrayList<MoonLocation> getLocations() {
            return this.locations;
        }

        public Long getCurrentTime() {
            return this.currentTime;
        }
    }
}

