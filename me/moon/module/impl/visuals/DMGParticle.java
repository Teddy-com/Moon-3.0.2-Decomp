/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.util.ArrayList;
import me.moon.event.Handler;
import me.moon.event.impl.game.EntityDamageEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.dmgparticle.Particle;
import net.minecraft.entity.EntityLivingBase;

public class DMGParticle
extends Module {
    public ArrayList<Particle> particles = new ArrayList();

    public DMGParticle() {
        super("DMGParticle", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        if (this.particles.size() != 0) {
            for (int i = this.particles.size() - 1; i >= 0; --i) {
                Particle particle = this.particles.get(i);
                if (System.currentTimeMillis() - particle.creationDate >= 3000L) {
                    this.particles.remove(i);
                    continue;
                }
                particle.drawParticle();
            }
        }
    }

    @Handler(value=EntityDamageEvent.class)
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        EntityLivingBase entityLivingBase = entityDamageEvent.getEntity();
        this.particles.add(new Particle(entityLivingBase, entityLivingBase.getHealth(), entityLivingBase.getMaxHealth(), System.currentTimeMillis(), entityDamageEvent.getDamageAmount()));
    }
}

