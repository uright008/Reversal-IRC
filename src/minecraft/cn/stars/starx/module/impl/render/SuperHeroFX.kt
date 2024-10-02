package cn.stars.starx.module.impl.render

import cn.stars.starx.event.impl.AttackEvent
import cn.stars.starx.event.impl.Render3DEvent
import cn.stars.starx.event.impl.WorldEvent
import cn.stars.starx.font.modern.FontManager
import cn.stars.starx.module.Category
import cn.stars.starx.module.Module
import cn.stars.starx.module.ModuleInfo
import cn.stars.starx.util.math.MSTimer
import cn.stars.starx.util.math.RandomUtil
import cn.stars.starx.util.render.ColorUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.abs

@ModuleInfo(name = "SuperheroFX", chineseName = "零日攻击特效", description = "Display string on hit", chineseDescription = "在攻击时显示字", category = Category.RENDER)
class SuperHeroFX : Module() {

    private val textParticles = mutableListOf<FXParticle>()
    private val generateTimer = MSTimer()


    override fun onWorld(event: WorldEvent) = textParticles.clear()


    override fun onAttack(event: AttackEvent) {
        val entity = event.target
        if (mc.theWorld.loadedEntityList.contains(entity) && generateTimer.hasTimePassed(500L)) {
            val dirX = RandomUtil.nextDouble(-0.5, 0.5)
            val dirZ = RandomUtil.nextDouble(-0.5, 0.5)
            generateTimer.reset()
            textParticles.add(
                FXParticle(
                    entity.posX + dirX,
                    entity.entityBoundingBox.minY + (entity.entityBoundingBox.maxY - entity.entityBoundingBox.minY) / 2.0,
                    entity.posZ + dirZ,
                    dirX, dirZ
                )
            )
        }
    }


    override fun onRender3D(event: Render3DEvent) {
        val removeList = mutableListOf<FXParticle>()
        for (particle in textParticles) {
            if (particle.canRemove) {
                removeList.add(particle)
                continue
            }
            particle.draw()
        }
        textParticles.removeAll(removeList)
    }

    class FXParticle(val posX: Double, val posY: Double, val posZ: Double, val animHDir: Double, val animVDir: Double) {
        private val messageString: String = listOf("Smash!", "Boom!", "Hit!", "Critical!", "Koo!", "Hoo!", "Ouch!").random()
        private val color: Color = listOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW).random()

        private val fadeTimer = MSTimer()
        private val stringLength = FontManager.getRegular(16).getStringWidth(messageString)
        private val fontHeight = FontManager.getRegular(16).height()

        var canRemove = false
        private var firstDraw = true

        fun draw() {
            val renderManager = mc.renderManager ?: return
            if (firstDraw) {
                fadeTimer.reset()
                firstDraw = false
            }
            val alpha = (if (fadeTimer.hasTimePassed(250L)) fadeTimer.hasTimeLeft(500L) else 250L - fadeTimer.hasTimeLeft(250L)).toFloat().coerceIn(0F, 250F) / 250F
            val progress = (if (fadeTimer.hasTimePassed(250L)) abs(fadeTimer.hasTimeLeft(250L) - 250L) else 250L - fadeTimer.hasTimeLeft(250L)).toFloat().coerceIn(0F, 500F) / 250F
            val textY = if (mc.gameSettings.thirdPersonView != 2) -1.0f else 1.0f
            val offsetX = stringLength / 2.0 * 0.02 * progress.toDouble()
            val offsetY = fontHeight / 2.0 * 0.02 * progress.toDouble()
            if (progress >= 2F) {
                canRemove = true
                return
            }
            GlStateManager.pushMatrix()
            GlStateManager.enablePolygonOffset()
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f)
            GL11.glTranslated(posX + animHDir * progress - offsetX - renderManager.renderPosX, posY + animVDir * progress - offsetY - renderManager.renderPosY, posZ - renderManager.renderPosZ)
            GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            GL11.glScalef(progress * -0.02F, progress * -0.02F, progress * 0.02F)
            GlStateManager.rotate(textY * renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            GL11.glDepthMask(false)
            FontManager.getRegular(16).drawString(messageString, 0.25, 0.25, Color(0F, 0F, 0F, alpha * 0.75F).rgb)
            FontManager.getRegular(16).drawString(messageString, 0.0, 0.0, ColorUtil.reAlpha(color.rgb, alpha))
            GL11.glColor4f(187.0f, 255.0f, 255.0f, 1.0f)
            GL11.glDepthMask(true)
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f)
            GlStateManager.disablePolygonOffset()
            GlStateManager.popMatrix()
        }
    }

}