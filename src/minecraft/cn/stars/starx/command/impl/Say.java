package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.opengl.Display;

@CommandInfo(name = "Say", description = "Say things", syntax = ".say <message>", aliases = "say")
public final class Say extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        if (args[0] != null) {
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(String.join(" ", args)));
        }
    }
}
