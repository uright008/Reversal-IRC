package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@CommandInfo(name = "Chat", description = "IRC Chat", syntax = ".chat <message>", aliases = "chat")
public final class Chat extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        if (args[0] != null) {
            if (StarX.user != null) {
                new Thread(() -> StarX.user.sendMessage("Message", "[ID:" + mc.session.getUsername() + "] " + String.join(" ", args))).start();
            } else {
                StarX.showMsg("You haven't connected to IRC yet!");
            }
        }
    }
}
