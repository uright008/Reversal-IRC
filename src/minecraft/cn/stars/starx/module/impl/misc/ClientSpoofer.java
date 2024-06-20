package cn.stars.starx.module.impl.misc;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.PacketSendEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.ui.notification.NotificationType;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(name = "ClientSpoofer", description = "Makes servers think you're on other clients",
        chineseDescription = "让服务器认为你在使用别的端", category = Category.MISC)
public final class ClientSpoofer extends Module {
    private final ModeValue mode = new ModeValue("Mode", this, "Forge", "Forge", "Lunar", "LabyMod", "PvP Lounge", "CheatBreaker", "Geyser");

    @Override
    protected void onEnable() {
        ScaledResolution sr = new ScaledResolution(mc);
        StarX.INSTANCE.notificationManager.registerNotification("Rejoin for " + this.getModuleInfo().name() + " to work.", NotificationType.NOTIFICATION);
        StarX.INSTANCE.showMsg("W:"+sr.getScaledWidth() + " ,H:"+sr.getScaledHeight());
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            switch (mode.getMode()) {
                case "Forge": {
                    packet.setData(createPacketBuffer("FML", true));
                    break;
                }

                case "Lunar": {
                    packet.setChannel("REGISTER");
                    packet.setData(createPacketBuffer("Lunar-Client", false));
                    break;
                }

                case "LabyMod": {
                    packet.setData(createPacketBuffer("LMC", true));
                    break;
                }

                case "PvP Lounge": {
                    packet.setData(createPacketBuffer("PLC18", false));
                    break;
                }

                case "CheatBreaker": {
                    packet.setData(createPacketBuffer("CB", true));
                    break;
                }

                case "Geyser": {
                    packet.setData(createPacketBuffer("Geyser", false));
                    break;
                }
            }
        }
    }

    private PacketBuffer createPacketBuffer(final String data, final boolean string) {
        if (string)
            return new PacketBuffer(Unpooled.buffer()).writeString(data);
        else
            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
    }
}
