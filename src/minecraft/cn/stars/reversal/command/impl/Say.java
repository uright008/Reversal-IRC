package cn.stars.reversal.command.impl;

import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@CommandInfo(name = "Say", description = "Say things", syntax = ".say <message>", aliases = "say")
public final class Say extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        if (args[0] != null) {
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(String.join(" ", args)));
        }
    }
}
