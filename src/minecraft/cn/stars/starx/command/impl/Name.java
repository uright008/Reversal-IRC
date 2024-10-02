package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.render.ThemeUtil;

@CommandInfo(name = "Name", description = "Customize the player name", syntax = ".name <name>", aliases = {"name", "cn"})
public final class Name extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        StarX.customName = String.join(" ", args);
        StarX.notificationManager.registerNotification("Successfully set the custom name.", "Command", NotificationType.SUCCESS);
        StarX.showMsg("Successfully set the custom name.");
    }
}
