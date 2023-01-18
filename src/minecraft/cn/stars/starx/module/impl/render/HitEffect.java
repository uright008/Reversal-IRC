/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.AttackEvent;
import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name = "HitEffect", description = "Renders particle effect when you attack someone", category = Category.RENDER)
public final class HitEffect extends Module {

    private final ModeValue mode = new ModeValue("Mode", this, "Blood", "Blood", "Critical", "MagicCritical", "Heart");
    private final NumberValue amount = new NumberValue("Amount", this, 5, 1, 10, 1);
    private final BoolValue sound = new BoolValue("BloodSound", this, true);

    private EntityLivingBase target;

    @Override
    public void onUpdateAlwaysInGui() {
        sound.hidden = !mode.is("Blood");
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            if (mc.thePlayer.ticksExisted > 3) {
                switch (mode.getMode()) {
                    case "Blood":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;

                    case "Critical":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                        break;

                    case "Heart":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.HEART);

                    case "MagicCritical":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
                        break;
                }
            }

            target = null;
        }
    }
}
