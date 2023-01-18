package cn.stars.starx.util.world;

import cn.stars.starx.GameInstance;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@UtilityClass
public final class BlockUtil implements GameInstance {
    public boolean isInLiquid() {
        return mc.thePlayer.isInLava() || mc.thePlayer.isInWater();
    }

    public static Block getBlock(Vec3 vec3) {
        return getBlock(new BlockPos(vec3));
    }

    public static Block getBlock(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    public static IBlockState getState(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos);
    }
}
