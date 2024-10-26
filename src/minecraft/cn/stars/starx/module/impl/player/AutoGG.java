package cn.stars.starx.module.impl.player;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.PacketReceiveEvent;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.MiscUtil;
import cn.stars.starx.util.math.TimeUtil;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "AutoGG", chineseName = "自动GG", description = "Auto send gg when game end", chineseDescription = "在游戏结束时自动发送gg", category = Category.PLAYER)
public class AutoGG extends Module {
    String[] winMessage = new String[] {"获胜", "Winner", "第一名", "1st", "胜利"};
    TimeUtil timeUtil = new TimeUtil();
    private boolean isWinning = false;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) event.packet).getChatComponent().getUnformattedText();
            if (MiscUtil.containsAnyIgnoreCase(message, winMessage) && !isWinning) {
                isWinning = true;
                mc.thePlayer.sendChatMessage("gg");
                StarX.notificationManager.registerNotification("Sent GG at chat!", "AutoGG", 3000L, NotificationType.SUCCESS);
            }
        }
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (timeUtil.hasReached(3000L) && isWinning) {
            isWinning = false;
            timeUtil.reset();
        }
    }
}
