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
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name = "HitEffect", description = "Renders particle effect when you attack someone", category = Category.RENDER)
public final class HitEffect extends Module {

    private final ModeValue mode = new ModeValue("Mode", this, "Blood", "None", "Blood", "Lightning", "Heart", "Flame", "Portal", "Explosion", "Lava",
            "Smoke", "Note", "Slime", "Enchant");
    private final BoolValue sharp = new BoolValue("Sharpness", this ,false);
    private final BoolValue crit = new BoolValue("Critical", this ,false);
    private final NumberValue amount = new NumberValue("Amount", this, 5, 1, 10, 1);
    private final BoolValue sound = new BoolValue("Sound", this, true);

    private EntityLivingBase target;

    @Override
    public void onUpdateAlwaysInGui() {
        sound.hidden = !mode.is("Blood") && !mode.is("Lightning");
    }

    @Override
    public void onAttack(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            if (mc.thePlayer.ticksExisted > 3) {
                if (sharp.isEnabled()) {
                    for (int i = 0; i < amount.getValue(); i++)
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
                }
                if (crit.isEnabled()) {
                    for (int i = 0; i < amount.getValue(); i++)
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                }
                switch (mode.getMode()) {
                    case "Blood":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));

                        if (sound.isEnabled())
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        break;

                    case "Lightning":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.getNetHandler().handleSpawnGlobalEntity(new S2CPacketSpawnGlobalEntity(new EntityLightningBolt(mc.theWorld, target.posX, target.posY, target.posZ)));

                        if (sound.isEnabled()) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.explode"), 1.0f));
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("ambient.weather.thunder"), 1.0f));
                        }

                    case "Heart":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.HEART);
                        break;
                    case "Flame":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.FLAME);
                        break;
                    case "Portal":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.PORTAL);
                        break;
                    case "Explosion":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.EXPLOSION_NORMAL);
                        break;
                    case "Lava":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.DRIP_LAVA);
                        break;
                    case "Note":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.NOTE);
                        break;
                    case "Slime":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.SLIME);
                        break;
                    case "Smoke":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.SMOKE_NORMAL);
                        break;
                    case "Enchant":
                        for (int i = 0; i < amount.getValue(); i++)
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.ENCHANTMENT_TABLE);
                        break;
                }
            }

            target = null;
        }
    }
}
