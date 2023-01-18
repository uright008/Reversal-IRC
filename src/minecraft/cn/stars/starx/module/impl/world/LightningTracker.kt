package cn.stars.starx.module.impl.world

import cn.stars.starx.StarX
import cn.stars.starx.event.impl.PacketReceiveEvent
import cn.stars.starx.module.Category
import cn.stars.starx.module.Module
import cn.stars.starx.module.ModuleInfo
import cn.stars.starx.ui.notification.NotificationType
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
import java.text.DecimalFormat

@ModuleInfo(name = "LightingTracker", description = "Detect the lightning in the world", category = Category.WORLD)
class LightningTracker : Module() {

    var decimalFormat = DecimalFormat("0.0")

    override fun onPacketReceive(event: PacketReceiveEvent) {
        val p = event.packet as S2CPacketSpawnGlobalEntity
        if (p is S2CPacketSpawnGlobalEntity) {
            if (p.func_149053_g() != 1) return
            StarX.INSTANCE.showMsg("Lightning at X:${decimalFormat.format(p.func_149051_d() / 32.0)} Y:${decimalFormat.format(p.func_149050_e() / 32.0)} Z:${decimalFormat.format(p.func_149049_f() / 32.0)}")
            StarX.INSTANCE.notificationManager.registerNotification("Lightning at X:${decimalFormat.format(p.func_149051_d() / 32.0)} Y:${decimalFormat.format(p.func_149050_e() / 32.0)} Z:${decimalFormat.format(p.func_149049_f() / 32.0)}"
            , "LightningTracker", NotificationType.NOTIFICATION)
        }
    }
}