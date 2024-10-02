package cn.stars.starx.module.impl.render

import cn.stars.starx.StarX
import cn.stars.starx.event.impl.PacketReceiveEvent
import cn.stars.starx.event.impl.Render2DEvent
import cn.stars.starx.module.Category
import cn.stars.starx.module.Module
import cn.stars.starx.module.ModuleInfo
import cn.stars.starx.module.impl.render.ClickGui.speedValue
import cn.stars.starx.setting.impl.ModeValue
import cn.stars.starx.setting.impl.NumberValue
import cn.stars.starx.ui.clickgui.ClickGUI
import cn.stars.starx.util.render.RenderUtil
import cn.stars.starx.util.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.network.play.server.S19PacketEntityStatus
import java.awt.Color


@ModuleInfo(name = "HurtCam", chineseName = "受伤抖动", description = "Modify the shake when you get hurt", chineseDescription = "修改你受伤时的抖动效果", category = Category.RENDER)
class HurtCam : Module() {
    private val mode = ModeValue("Mode", this, "Vanilla", "Cancel", "Vanilla", "FPS")
    private val fpsTimeValue = NumberValue("FPS Time", this, 1000.0, 0.0, 1500.0, 1.0)
    private val fpsHeightValue = NumberValue("FPS Height", this,25.0, 10.0, 50.0, 1.0)
    private var sr = ScaledResolution(mc)

    override fun onUpdateAlways() {
        if (!this.enabled) toggleModule()
        suffix = mode.mode
    }

    override fun onUpdateAlwaysInGui() {
        fpsTimeValue.hidden = !mode.mode.equals("FPS")
        fpsHeightValue.hidden = !mode.mode.equals("FPS")
    }

    override fun onRender2D(event: Render2DEvent) {
        sr = ScaledResolution(mc)
        if (hurt == 0L) return

        val passedTime = System.currentTimeMillis() - hurt
        if (passedTime> fpsTimeValue.value) {
            hurt = 0L
            return
        }

        val color = getColor((((fpsTimeValue.value - passedTime) / fpsTimeValue.value.toFloat()) * 255).toInt())
        val color1 = getColor(0)
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        RenderUtil.drawGradientRect(0, 0, width, fpsHeightValue.value.toInt(), color.rgb, color1.rgb)
        RenderUtil.drawGradientRect(0, height - fpsHeightValue.value.toInt(), width, height, color1.rgb, color.rgb)
    }

    private fun getColor(alpha: Int): Color {
        return Color(220,0,0, alpha)
    }

    companion object {
        @JvmField
        var hurt = 0L
    }
}
