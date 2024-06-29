package cn.stars.starx.module.impl.hud

import cn.stars.starx.event.impl.Render2DEvent
import cn.stars.starx.font.modern.FontManager
import cn.stars.starx.module.Category
import cn.stars.starx.module.Module
import cn.stars.starx.module.ModuleInfo
import cn.stars.starx.util.misc.TransUtils
import cn.stars.starx.util.player.PotionData
import cn.stars.starx.util.render.ColorUtil
import cn.stars.starx.util.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.resources.I18n
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.abs

@ModuleInfo(name = "EffectsDisplay", description = "Display your potion effects", chineseDescription = "显示你的药水效果", category = Category.HUD)
class EffectsDisplay : Module() {
    init {
        canBeEdited = false
        x = 830
        y = 470
        width = 120
        height = 50
    }

    private val potionMap: MutableMap<Potion, PotionData> = HashMap()
    private fun intToRomanByGreedy(num: Int): String {
        var num = num
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val symbols = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
        val stringBuilder = StringBuilder()
        var i = 0
        while (i < values.size && num >= 0) {
            while (values[i] <= num) {
                num -= values[i]
                stringBuilder.append(symbols[i])
            }
            i++
        }
        return stringBuilder.toString()
    }

    override fun onRender2D(event: Render2DEvent?) {
        GlStateManager.pushMatrix()
        var y = y.toFloat()
        var x = x.toFloat()
        for (potionEffect in mc.thePlayer.activePotionEffects) {
            val potion = Potion.potionTypes[potionEffect.potionID]
            val name = I18n.format(potion.name)
            val potionData: PotionData?
            if (potionMap.containsKey(potion) && potionMap[potion]!!.level == potionEffect.amplifier) potionData =
                potionMap[potion] else potionMap[potion] =
                PotionData(potion, TransUtils(x, y), potionEffect.amplifier).also { potionData = it }
            var flag = true
            for (checkEffect in mc.thePlayer.activePotionEffects) if (checkEffect.amplifier == potionData!!.level) {
                flag = false
                break
            }
            if (flag) potionMap.remove(potion)
            var potionTime: Int
            var potionMaxTime: Int
            try {
                potionTime = Potion.getDurationString(potionEffect).split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].toInt()
                potionMaxTime =
                    Potion.getDurationString(potionEffect).split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1].toInt()
            } catch (ignored: Exception) {
                potionTime = 100
                potionMaxTime = 1000
            }
            val lifeTime = potionTime * 60 + potionMaxTime
            if (potionData!!.getMaxTimer() == 0 || lifeTime > potionData.getMaxTimer().toDouble()) potionData.maxTimer =
                lifeTime
            var state = 0.0f
            if (lifeTime >= 0.0) state = (lifeTime / potionData.getMaxTimer().toFloat().toDouble() * 100.0).toFloat()
            val position = Math.round(potionData.translate.y + 5)
            state = Math.max(state, 2.0f)
            potionData.translate.interpolate(x, y, 0.1)
            potionData.animationX = getAnimationState(
                potionData.getAnimationX().toDouble(), (1.2f * state).toDouble(), (Math.max(
                    10.0f, abs(
                        potionData.animationX - (1.2f * state)
                    ) * 15.0f
                ) * 0.3f).toDouble()
            ).toFloat()

//            System.out.println(potionData.translate.getY());
            RenderUtils.drawRect(
                x,
                potionData.translate.y,
                x + 120f,
                potionData.translate.y + 30f,
                ColorUtil.reAlpha(-9868951, 0.1f) // GRAY
            )
            RenderUtils.drawRect(
                x, potionData.translate.y, potionData.animationX, potionData.translate.y + 30f, ColorUtil.reAlpha(
                    Color(34, 24, 20).brighter().rgb, 0.3f
                )
            )
            RenderUtils.drawShadow(x, Math.round(potionData.translate.y).toFloat(), 120f, 30f)
            val posY = potionData.translate.y + 13.0
            FontManager.getPSM(24).drawString(
                name + " " + intToRomanByGreedy(potionEffect.amplifier + 1),
                x + 29.0,
                posY - 8.0,
                ColorUtil.reAlpha(
                    -65794, 0.8f
                ) // WHITE
            )
            FontManager.getPSR(20).drawString(
                Potion.getDurationString(potionEffect), x + 29.0, posY + 5.0, ColorUtil.reAlpha(
                    Color(200, 200, 200).rgb, 0.5f
                )
            )
            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix()
                GL11.glDisable(2929)
                GL11.glEnable(3042)
                GL11.glDepthMask(false)
                OpenGlHelper.glBlendFunc(770, 771, 1, 0)
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                val statusIconIndex = potion.statusIconIndex
                mc.textureManager.bindTexture(ResourceLocation("textures/gui/container/inventory.png"))
                mc.ingameGUI.drawTexturedModalRect(
                    x + 6f,
                    (position + 1).toFloat(),
                    statusIconIndex % 8 * 18,
                    198 + statusIconIndex / 8 * 18,
                    18,
                    18
                )
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
                GL11.glEnable(2929)
                GlStateManager.popMatrix()
            }
            y -= 35
        }
        GlStateManager.popMatrix()
    }

    fun getAnimationState(animation: Double, finalState: Double, speed: Double): Double {
        var animation = animation
        val add = (0.01 * speed).toFloat()
        if (animation < finalState) {
            if (animation + add < finalState) animation += add.toDouble()
            else animation = finalState
        } else {
            if (animation - add > finalState) animation -= add.toDouble()
            else animation = finalState
        }
        return animation
    }
}

