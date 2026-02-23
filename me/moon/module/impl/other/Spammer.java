/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Spammer
extends Module {
    private final TimerUtil timer = new TimerUtil();
    private int index = 0;
    private List<String> StringList;
    private final Random random = new Random();
    private final NumberValue<Double> delay = new NumberValue<Double>("Delay", 1.0, 0.1, 10.0, 0.1);

    public Spammer() {
        super("Spammer", Module.Category.OTHER, new Color(16748980).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.theWorld == null) {
            return;
        }
        if (this.timer.sleep((int)((Double)this.delay.getValue() * 1000.0))) {
            if (this.index < this.StringList.size()) {
                ArrayList<NetworkPlayerInfo> niggas;
                String msg = this.StringList.get(this.index).replace("%RANDOMPLAYER%", (niggas = new ArrayList<NetworkPlayerInfo>(this.mc.thePlayer.sendQueue.getPlayerInfoMap())).size() < 2 ? "" : niggas.get(this.random.nextInt(niggas.size())).getGameProfile().getName()).replace("%INVISIBLE%", "\u061d").replace("%RANDOMNUMBER%", String.valueOf(MathUtils.getRandomInRange(10000232, 992323999)));
                this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C01PacketChatMessage(msg));
                ++this.index;
            } else {
                this.index = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.StringList.clear();
        this.StringList = null;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            List<String> lines;
            File file = new File(Moon.INSTANCE.getDir(), "spam.txt");
            if (file.exists() && (lines = Files.readAllLines(file.toPath())).size() > 0) {
                this.StringList = lines;
                return;
            }
            file.createNewFile();
            this.StringList = new ArrayList<String>();
            this.StringList.add("Moon > Hypixel %RANDOMNUMBER%");
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.index = 0;
    }
}

