package cn.stars.starx.util.misc

import cn.stars.starx.GameInstance
import cn.stars.starx.util.world.BlockUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos

object MiscUtils {
    fun canBeClicked(blockPos: BlockPos?) = BlockUtil.getBlock(blockPos)?.canCollideCheck(BlockUtil.getState(blockPos), false) ?: false &&
            Minecraft.getMinecraft().theWorld.worldBorder.contains(blockPos)
}