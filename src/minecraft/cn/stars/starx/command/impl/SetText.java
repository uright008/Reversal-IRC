package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.render.ThemeUtil;

@CommandInfo(name = "SetText", description = "Set the custom text", syntax = ".settext <name>", aliases = "settext")
public final class SetText extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        StarX.customText = String.join(" ", args);
        StarX.notificationManager.registerNotification("Successfully set the custom text.", "Command", NotificationType.SUCCESS);
        StarX.showMsg("Successfully set the custom text.");
    }
}
