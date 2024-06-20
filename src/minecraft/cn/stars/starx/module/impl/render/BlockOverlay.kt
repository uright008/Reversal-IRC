package cn.stars.starx.module.impl.render

import cn.stars.starx.event.impl.Render3DEvent
import cn.stars.starx.module.Category
import cn.stars.starx.module.Module
import cn.stars.starx.module.ModuleInfo
import cn.stars.starx.setting.impl.BoolValue
import cn.stars.starx.setting.impl.NumberValue
import cn.stars.starx.util.render.ColorUtil
import cn.stars.starx.util.render.ThemeType
import cn.stars.starx.util.wrapper.WrapperAxisAlignedBB
import cn.stars.starx.util.wrapper.WrapperBlockPos
import cn.stars.starx.util.wrapper.WrapperBufferBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MovingObjectPosition
import org.lwjgl.opengl.GL11

@ModuleInfo(name = "BlockOverlay", description = "Render a overlay around blocks", chineseDescription = "在方块周围渲染边框", category = Category.RENDER)
class BlockOverlay : Module() {
    private var fill = BoolValue("Fill", this, false)
    private var outline = BoolValue("Outline", this, true)
    private var throughBlock = BoolValue("ThroughBlock", this, false)
    private var lineWidth = NumberValue("Width", this,  1.0, 0.1, 10.0, 0.1)

    override fun onRender3D(event: Render3DEvent?) {
        val color = ColorUtil.getClientColor()
        if (mc.objectMouseOver != null) {
            if (isHoveringOverBlock()) {
                val pos = WrapperBlockPos(mc.objectMouseOver.blockPos)
                val state = getBlockState(pos)
                val block = getBlock(pos)
                val x = pos.x - mc.renderManager.viewerPosX
                val y = pos.y - mc.renderManager.viewerPosY
                val z = pos.z - mc.renderManager.viewerPosZ
                GL11.glPushMatrix()
                GlStateManager.enableAlpha()
                GlStateManager.enableBlend()
                GL11.glBlendFunc(770, 771)
                GL11.glDisable(3553)
                GL11.glEnable(2848)
                if (throughBlock.enabled) {
                    GL11.glDisable(2929)
                }
                GL11.glDepthMask(false)
                val blockBoundingBox = getBlockBoundingBox(pos, state)
                val minX: Double = if (block is BlockStairs) 0.0 else blockBoundingBox.minX()
                val minY: Double = if (block is BlockStairs) 0.0 else blockBoundingBox.minY()
                val minZ: Double = if (block is BlockStairs) 0.0 else blockBoundingBox.minZ()
                if (fill.enabled) {
                    val color = ColorUtil.withAlpha(color, 100)
                    GL11.glPushMatrix()
                    GlStateManager.color(
                        color.red / 255.0f,
                        color.green / 255.0f,
                        color.blue / 255.0f,
                        color.alpha / 255.0f
                    )
                    drawBoundingBox(
                        WrapperAxisAlignedBB(
                            x + minX - 0.01,
                            y + minY - 0.01,
                            z + minZ - 0.01,
                            x + blockBoundingBox.maxX() + 0.01,
                            y + blockBoundingBox.maxY() + 0.01,
                            z + blockBoundingBox.maxZ() + 0.01
                        )
                    )
                    GL11.glPopMatrix()
                }
                if (outline.enabled) {
                    val color = color
                    GL11.glPushMatrix()
                    GlStateManager.color(
                        color.red / 255.0f,
                        color.green / 255.0f,
                        color.blue / 255.0f,
                        color.alpha / 255.0f
                    )
                    GL11.glLineWidth(lineWidth.value.toFloat())
                    drawBoundingBoxOutline(
                        WrapperAxisAlignedBB(
                            x + minX - 0.005,
                            y + minY - 0.005,
                            z + minZ - 0.005,
                            x + blockBoundingBox.maxX() + 0.005,
                            y + blockBoundingBox.maxY() + 0.005,
                            z + blockBoundingBox.maxZ() + 0.005
                        )
                    )
                    GL11.glPopMatrix()
                }
                GL11.glDisable(2848)
                GL11.glEnable(3553)
                if (throughBlock.enabled) {
                    GL11.glEnable(2929)
                }
                GL11.glDepthMask(true)
                GL11.glLineWidth(1.0f)
                GL11.glPopMatrix()
            }
        }
    }
    fun isHoveringOverBlock(): Boolean {
        return Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
    }

    fun getBlockState(pos: WrapperBlockPos): IBlockState {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos.pos)
    }

    fun getBlock(pos: WrapperBlockPos): Block {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos.pos).block
    }

    fun getBlockBoundingBox(pos: WrapperBlockPos, state: IBlockState?): WrapperAxisAlignedBB {
        return WrapperAxisAlignedBB(
            AxisAlignedBB(
                getBlock(pos).blockBoundsMinX,
                getBlock(pos).blockBoundsMinY,
                getBlock(pos).blockBoundsMinZ,
                getBlock(pos).blockBoundsMaxX,
                getBlock(pos).blockBoundsMaxY,
                getBlock(pos).blockBoundsMaxZ
            )
        )
    }

    private fun posBoundingBoxLeftHalf(boundingBox: WrapperAxisAlignedBB, bufferBuilder: WrapperBufferBuilder) {
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
    }

    fun drawBoundingBoxOutline(boundingBox: WrapperAxisAlignedBB) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder: WrapperBufferBuilder = WrapperBufferBuilder(tessellator)
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION)
        posBoundingBoxHalf(boundingBox, bufferBuilder)
        tessellator.draw()
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION)
        posBoundingBoxLeftHalf(boundingBox, bufferBuilder)
        tessellator.draw()
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION)
        posBoundingBoxSquare(boundingBox, tessellator, bufferBuilder)
    }

    private fun posBoundingBoxHalf(boundingBox: WrapperAxisAlignedBB, bufferBuilder: WrapperBufferBuilder) {
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
    }

    private fun posBoundingBoxSquare(
        boundingBox: WrapperAxisAlignedBB,
        tessellator: Tessellator,
        bufferBuilder: WrapperBufferBuilder
    ) {
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        tessellator.draw()
    }
    fun drawBoundingBox(boundingBox: WrapperAxisAlignedBB) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder: WrapperBufferBuilder = WrapperBufferBuilder(tessellator)
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        posBoundingBoxSquare(boundingBox, tessellator, bufferBuilder)
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        tessellator.draw()
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        posBoundingBoxLeftHalf(boundingBox, bufferBuilder)
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        tessellator.draw()
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        posBoundingBoxHalf(boundingBox, bufferBuilder)
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        tessellator.draw()
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        tessellator.draw()
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION)
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.minZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()).endVertex()
        bufferBuilder.pos(boundingBox.maxX(), boundingBox.minY(), boundingBox.maxZ()).endVertex()
        tessellator.draw()
    }
}