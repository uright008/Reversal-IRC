package cn.stars.starx.module.impl.misc;

import cn.stars.starx.event.impl.PacketReceiveEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import net.minecraft.network.play.server.S37PacketStatistics;

@ModuleInfo(name = "NoAchievements", description = "Disable your achievements info", category = Category.MISC)
public class NoAchievements extends Module {
    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S37PacketStatistics)
            event.setCancelled(true);
    }
}
