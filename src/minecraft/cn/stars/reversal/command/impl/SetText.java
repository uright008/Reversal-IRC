package cn.stars.reversal.command.impl;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import cn.stars.reversal.ui.notification.NotificationType;

@CommandInfo(name = "SetText", description = "Set the custom text", syntax = ".settext <name>", aliases = "settext")
public final class SetText extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Reversal.customText = String.join(" ", args);
        Reversal.notificationManager.registerNotification("Successfully set the custom text.", "Command", NotificationType.SUCCESS);
        Reversal.showMsg("Successfully set the custom text.");
    }
}
