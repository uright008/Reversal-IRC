package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.render.ThemeUtil;

@CommandInfo(name = "ClientName", description = "Customize the client name", syntax = ".clientname <name/reset>", aliases = {"clientname", "cn"})
public final class ClientName extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equals("reset")) {
            ThemeUtil.setCustomClientName("");
            StarX.notificationManager.registerNotification("Successfully reset the client name.", "Command", NotificationType.SUCCESS);
            StarX.showMsg("Successfully reset the client name.");
        } else {
            ThemeUtil.setCustomClientName(String.join(" ", args));
            StarX.notificationManager.registerNotification("Successfully set the client name.", "Command", NotificationType.SUCCESS);
            StarX.showMsg("Successfully set the client name.");
        }
    }
}
