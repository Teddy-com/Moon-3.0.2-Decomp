/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.MouseEvent;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import net.minecraft.entity.player.EntityPlayer;

public class MCF
extends Module {
    public MCF() {
        super("MCF", Module.Category.PLAYER, new Color(200, 200, 0).getRGB());
    }

    @Handler(value=MouseEvent.class)
    public void onMouse(MouseEvent event) {
        if (event.getButton() == 2 && this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)this.mc.objectMouseOver.entityHit;
            String name = player.getName();
            if (Moon.INSTANCE.getFriendManager().isFriend(name)) {
                Moon.INSTANCE.getFriendManager().removeFriend(name);
                Printer.print("Removed " + name + " as a friend!");
            } else {
                Moon.INSTANCE.getFriendManager().addFriend(name);
                Printer.print("Added " + name + " as a friend!");
            }
        }
    }
}

