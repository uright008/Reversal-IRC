package cn.stars.reversal.command.impl;

import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;

@CommandInfo(name = "Chat", description = "IRC Chat", syntax = ".chat <message>", aliases = "chat")
public final class Chat extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        if (args[0] != null) {
            if (RainyAPI.ircUser != null) {
                new Thread(() -> RainyAPI.ircUser.sendMessage("Message", "[ID:" + mc.session.getUsername() + "] " + String.join(" ", args))).start();
            } else {
                Reversal.showMsg("You haven't connected to IRC yet!");
            }
        }
    }
}
