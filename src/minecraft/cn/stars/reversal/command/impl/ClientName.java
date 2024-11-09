package cn.stars.reversal.command.impl;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import cn.stars.reversal.ui.notification.NotificationType;
import cn.stars.reversal.util.render.ThemeUtil;

@CommandInfo(name = "ClientName", description = "Customize the client name", syntax = ".clientname <name/reset>", aliases = {"clientname", "cn"})
public final class ClientName extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equals("reset")) {
            ThemeUtil.setCustomClientName("");
            Reversal.notificationManager.registerNotification("Successfully reset the client name.", "Command", NotificationType.SUCCESS);
            Reversal.showMsg("Successfully reset the client name.");
        } else {
            ThemeUtil.setCustomClientName(String.join(" ", args));
            Reversal.notificationManager.registerNotification("Successfully set the client name.", "Command", NotificationType.SUCCESS);
            Reversal.showMsg("Successfully set the client name.");
        }
    }
}
