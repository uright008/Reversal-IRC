package cn.stars.starx.module.impl.render;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.AttackEvent;
import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.event.impl.WorldEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.Objects;

@ModuleInfo(name = "TargetESP", description = "Display a ESP when you hit targets", chineseDescription = "当你攻击目标时渲染ESP", category = Category.RENDER)
public class TargetESP extends Module {
    private final ModeValue mode = new ModeValue("Mode", this, "Rectangle", "Rectangle", "Round");
    EntityLivingBase attackedEntity;

    @Override
    public void onAttack(AttackEvent event) {
        if (event.getTarget() != null) {
            try {
                attackedEntity = (EntityLivingBase) event.getTarget();
            } catch (ClassCastException e) {
                attackedEntity = null;
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (attackedEntity != null) {
            if (attackedEntity.isDead || mc.thePlayer.getDistanceToEntity(attackedEntity) > 10) {
                attackedEntity = null;
                return;
            }
            float dst = mc.thePlayer.getSmoothDistanceToEntity(attackedEntity);
            try {
                RenderUtil.drawTargetESP2D(Objects.requireNonNull(RenderUtil.targetESPSPos(attackedEntity)).x, Objects.requireNonNull(RenderUtil.targetESPSPos(attackedEntity)).y,
                        Color.RED,
                        Color.BLUE,
                        Color.GREEN,
                        Color.YELLOW,
                        (1.0f - MathHelper.clamp_float(Math.abs(dst - 6.0f) / 60.0f, 0f, 0.75f)) * 1, 1);
            } catch (Exception e) {
                attackedEntity = null;
            }
        }
    }

    @Override
    public void onWorld(WorldEvent event) {
        attackedEntity = null;
    }
}
