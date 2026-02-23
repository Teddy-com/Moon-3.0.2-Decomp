/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed;

import java.util.ArrayList;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.module.impl.movement.speed.modes.SpeedBhop;
import me.moon.module.impl.movement.speed.modes.SpeedFaithful;
import me.moon.module.impl.movement.speed.modes.SpeedHive;
import me.moon.module.impl.movement.speed.modes.SpeedMineplex;
import me.moon.module.impl.movement.speed.modes.SpeedNCP;
import me.moon.module.impl.movement.speed.modes.SpeedRedesky;
import me.moon.module.impl.movement.speed.modes.SpeedSpartan;
import me.moon.module.impl.movement.speed.modes.SpeedStrafe;
import me.moon.module.impl.movement.speed.modes.SpeedVanilla;
import me.moon.module.impl.movement.speed.modes.SpeedVerus;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdog;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdogCustom;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdogFast;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdogGround;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdogLowhop;
import me.moon.module.impl.movement.speed.modes.SpeedWatchdogNew;

public class SpeedManager {
    public ArrayList<SpeedMode> speedModes = new ArrayList();

    public void init() {
        this.speedModes.add(new SpeedWatchdog());
        this.speedModes.add(new SpeedWatchdogLowhop());
        this.speedModes.add(new SpeedWatchdogGround());
        this.speedModes.add(new SpeedWatchdogCustom());
        this.speedModes.add(new SpeedWatchdogFast());
        this.speedModes.add(new SpeedWatchdogNew());
        this.speedModes.add(new SpeedMineplex());
        this.speedModes.add(new SpeedNCP());
        this.speedModes.add(new SpeedFaithful());
        this.speedModes.add(new SpeedVanilla());
        this.speedModes.add(new SpeedBhop());
        this.speedModes.add(new SpeedHive());
        this.speedModes.add(new SpeedStrafe());
        this.speedModes.add(new SpeedRedesky());
        this.speedModes.add(new SpeedVerus());
        this.speedModes.add(new SpeedSpartan());
    }

    public void onEnable() {
        for (SpeedMode speedMode : this.speedModes) {
            if (Speed.mode.getValue() != speedMode.mode) continue;
            speedMode.onEnable();
        }
    }

    public void onDisable() {
        for (SpeedMode speedMode : this.speedModes) {
            if (Speed.mode.getValue() != speedMode.mode) continue;
            speedMode.onDisable();
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        for (SpeedMode speedMode : this.speedModes) {
            if (Speed.mode.getValue() != speedMode.mode) continue;
            speedMode.onUpdate(event);
        }
    }

    @Handler(value=MotionEvent.class)
    public void onMove(MotionEvent event) {
        for (SpeedMode speedMode : this.speedModes) {
            if (Speed.mode.getValue() != speedMode.mode) continue;
            speedMode.onMotion(event);
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        for (SpeedMode speedMode : this.speedModes) {
            if (Speed.mode.getValue() != speedMode.mode) continue;
            speedMode.onPacket(event);
        }
    }
}

