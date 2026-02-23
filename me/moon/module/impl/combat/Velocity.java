/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.MoveUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    private final EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.NORMAL);
    private final EnumValue<intaveMode> intaveModes = new EnumValue<intaveMode>("Intave Modes", intaveMode.QSG, this.mode, "Intave");
    private final NumberValue<Integer> horizontalModifier = new NumberValue<Integer>("Horizontal", 0, 0, 100, 1, this.mode, "Normal");
    private final NumberValue<Integer> verticalModifier = new NumberValue<Integer>("Vertical", 0, 0, 100, 1, this.mode, "Normal");
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 1250, 50, 5000, 50, this.mode, "Delayed");
    private final BooleanValue pingDelay = new BooleanValue("Delayed ping", true, (Value)this.mode, "Delayed");
    private final NumberValue<Integer> stackSize = new NumberValue<Integer>("Stack Size", 5, 2, 20, 1, this.mode, "Stack");
    private double stackedVelocity;
    private boolean isValid;

    public Velocity() {
        super("Velocity", Module.Category.COMBAT, new Color(0x7E7E7E).getRGB());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        int vertical = (Integer)this.verticalModifier.getValue();
        int horizontal = (Integer)this.horizontalModifier.getValue();
        if (this.mc.thePlayer == null) return;
        if (this.mc.theWorld == null) {
            return;
        }
        switch ((Mode)((Object)this.mode.getValue())) {
            case NORMAL: {
                Packet<INetHandlerPlayClient> packet;
                if (event.isSending()) return;
                if (event.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)(packet = (S12PacketEntityVelocity)event.getPacket())).getEntityID() == this.mc.thePlayer.getEntityId()) {
                    if (vertical != 0 || horizontal != 0) {
                        ((S12PacketEntityVelocity)packet).setMotionX(horizontal * ((S12PacketEntityVelocity)packet).getMotionX() / 100);
                        ((S12PacketEntityVelocity)packet).setMotionY(vertical * ((S12PacketEntityVelocity)packet).getMotionY() / 100);
                        ((S12PacketEntityVelocity)packet).setMotionZ(horizontal * ((S12PacketEntityVelocity)packet).getMotionZ() / 100);
                    } else {
                        event.setCancelled(true);
                    }
                }
                if (!(event.getPacket() instanceof S27PacketExplosion)) return;
                packet = (S27PacketExplosion)event.getPacket();
                event.setCancelled(true);
                return;
            }
            case AAC: {
                return;
            }
            case REVERSE: {
                if (event.isSending()) return;
                if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                    return;
                }
                packet.setMotionX(packet.getMotionX() * -1);
                packet.setMotionZ(packet.getMotionZ() * -1);
                return;
            }
            case DELAYED: {
                if (!event.isSending()) {
                    if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                    event.setCancelled(true);
                    new Thread(() -> {
                        try {
                            Thread.sleep(((Integer)this.delay.getValue()).intValue());
                            this.mc.getNetHandler().handleEntityVelocity(packet);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    return;
                }
                if (!this.pingDelay.isEnabled()) return;
                if (!(event.getPacket() instanceof C00PacketKeepAlive)) {
                    if (!(event.getPacket() instanceof C0FPacketConfirmTransaction)) return;
                }
                event.setCancelled(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(((Integer)this.delay.getValue()).intValue());
                        this.mc.thePlayer.sendQueue.addToSendQueue(event.getPacket());
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                return;
            }
            case MINIHOP: {
                if (event.isSending()) return;
                if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                    return;
                }
                event.setCancelled(true);
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.015625, this.mc.thePlayer.posZ);
                return;
            }
            case STACK: {
                if (event.isSending()) return;
                if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                    return;
                }
                this.stackedVelocity += 1.0;
                if (this.stackedVelocity < (double)((Integer)this.stackSize.getValue()).intValue()) {
                    event.setCancelled(true);
                    return;
                }
                this.stackedVelocity = 0.0;
                return;
            }
            case INTAVE: {
                if (event.isSending()) return;
                switch ((intaveMode)((Object)this.intaveModes.getValue())) {
                    case BEDWARS: {
                        if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                        if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                            return;
                        }
                        double velocity = (double)packet.getMotionY() / 8000.0;
                        if (velocity > 0.0) {
                            if (this.mc.thePlayer.onGround) {
                                double jumpValue = 3360.0;
                                int velocityModifier = this.mc.thePlayer.isMoving() ? 0 : 15;
                                packet.setMotionX(velocityModifier * packet.getMotionX() / 100);
                                packet.setMotionY((int)jumpValue);
                                packet.setMotionZ(velocityModifier * packet.getMotionZ() / 100);
                                return;
                            }
                            Printer.print("Velocity reduced " + this.mc.thePlayer.ticksExisted);
                            this.isValid = true;
                            return;
                        }
                        this.isValid = false;
                        return;
                    }
                    case QSG: {
                        if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                        if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                            return;
                        }
                        double velocity = (double)packet.getMotionY() / 8000.0;
                        if (velocity > 0.0) {
                            if (!this.mc.thePlayer.onGround) return;
                            this.mc.thePlayer.jump();
                            return;
                        }
                        this.isValid = false;
                        return;
                    }
                    case DEV: {
                        if (event.isSending()) return;
                        if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                        if (packet.getEntityID() != this.mc.thePlayer.getEntityId()) {
                            return;
                        }
                        double velocity = (double)packet.getMotionY() / 8000.0;
                        if (!(velocity > 0.0)) return;
                        if (!this.mc.thePlayer.onGround) return;
                        double jumpValue = 3360.0;
                        packet.setMotionY((int)jumpValue);
                        this.isValid = true;
                    }
                }
                return;
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((Mode)((Object)this.mode.getValue())).getName());
        switch ((Mode)((Object)this.mode.getValue())) {
            case INTAVE: {
                if (!event.isPre()) break;
                switch ((intaveMode)((Object)this.intaveModes.getValue())) {
                    case BEDWARS: {
                        if (this.mc.thePlayer.hurtResistantTime != 18 || this.mc.thePlayer.onGround || !this.isValid) break;
                        MoveUtil.setSpeed(Math.min(0.1, MoveUtil.getSpeed()));
                        break;
                    }
                    case QSG: {
                        break;
                    }
                    case DEV: {
                        if (this.mc.thePlayer.hurtResistantTime != 19 || this.mc.thePlayer.onGround || !this.isValid) break;
                        this.mc.thePlayer.motionY = 0.0;
                        double x = -this.mc.thePlayer.motionX;
                        double z = -this.mc.thePlayer.motionZ;
                        this.mc.thePlayer.setVelocity(x * 0.5, 0.0, z * 0.5);
                        this.isValid = false;
                    }
                }
                break;
            }
            case STACK: {
                if (!event.isPre()) break;
                break;
            }
            case MINIHOP: {
                break;
            }
            case SWITCH: {
                if (!event.isPre() || this.mc.thePlayer.hurtResistantTime < 13 || this.mc.thePlayer.ticksExisted % 2 != 0 || this.mc.thePlayer.onGround) break;
                double x = -this.mc.thePlayer.motionX;
                double z = -this.mc.thePlayer.motionZ;
                this.mc.thePlayer.setVelocity(x, this.mc.thePlayer.motionY, z);
                break;
            }
            case NORMAL: {
                break;
            }
            case AAC: {
                if (!event.isPre() || this.mc.thePlayer.hurtResistantTime <= 13 || this.mc.thePlayer.hurtResistantTime >= 20 || this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.hurtResistantTime == 19) {
                    this.mc.thePlayer.motionX *= 0.85;
                    this.mc.thePlayer.motionZ *= 0.85;
                    break;
                }
                double tick = Math.max(0, 16 - this.mc.thePlayer.hurtResistantTime);
                double speed = Math.min(0.05 + tick * 0.01, 0.3125);
                MoveUtil.setSpeed(speed);
            }
        }
    }

    private static enum intaveMode {
        BEDWARS("BedWars"),
        QSG("QSG"),
        DEV("Dev");

        private final String name;

        private intaveMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum Mode {
        NORMAL("Normal"),
        AAC("AAC"),
        INTAVE("Intave"),
        REVERSE("Reverse"),
        SWITCH("Switch"),
        MINIHOP("MiniHop"),
        DELAYED("Delayed"),
        STACK("Stack");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

