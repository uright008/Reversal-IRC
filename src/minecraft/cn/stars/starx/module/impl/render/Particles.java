/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.AttackEvent;
import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.EvictingList;
import cn.stars.starx.util.render.HitParticleUtils;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

import java.util.List;

@ModuleInfo(name = "Particles", description = "Renders particles when you attack someone", category = Category.RENDER)
public final class Particles extends Module {

    private final NumberValue amount = new NumberValue("Amount", this, 10, 1, 20, 1);
    private final BoolValue physics = new BoolValue("Physics", this, true);
    private final NumberValue aliveTime = new NumberValue("Alive Time", this, 1, 1, 10, 1);

    private final List<HitParticleUtils> particles = new EvictingList<>(100);
    private final TimeUtil timer = new TimeUtil();
    private EntityLivingBase target;

    @Override
    public void onDisable() {
        particles.clear();
    }

    @Override
    public void onAttack(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            for (int i = 0; i < amount.getValue(); i++)
                particles.add(new HitParticleUtils(new Vec3(target.posX + (Math.random() - 0.5) * 0.5, target.posY + Math.random() * 1 + 0.5, target.posZ + (Math.random() - 0.5) * 0.5)));

            target = null;
        }
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if (particles.isEmpty())
            return;

        for (int i = 0; i <= timer.getElapsedTime() / 1E+11 * aliveTime.getValue(); i++) {
            if (physics.isEnabled())
                particles.forEach(HitParticleUtils::update);
            else
                particles.forEach(HitParticleUtils::updateWithoutPhysics);
        }

        particles.removeIf(particle -> mc.thePlayer.getDistanceSq(particle.getPosition().xCoord, particle.getPosition().yCoord, particle.getPosition().zCoord) > 50 * 10);

        timer.reset();

        RenderUtil.renderParticles(particles);
    }
}