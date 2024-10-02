package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.starx.Branch;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

@CommandInfo(name = "ClientTitle", description = "Customize the client window title", syntax = ".clienttitle <name/reset>", aliases = {"clienttitle", "ct"})
public final class ClientTitle extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        if (args[0].equals("%reset%")) {
            Display.setTitle(StarX.NAME + " " + StarX.VERSION + " " + Branch.getBranchName(StarX.BRANCH) + " | " + StarX.wittyTitle[RandomUtil.INSTANCE.nextInt(0, StarX.wittyTitle.length)]);
            StarX.notificationManager.registerNotification("Successfully reset the client window title.", "Command", NotificationType.SUCCESS);
            StarX.showMsg("Successfully reset the client window title.");
        } else {
            Display.setTitle(String.join(" ", args));
            StarX.notificationManager.registerNotification("Successfully set the client window title.", "Command", NotificationType.SUCCESS);
            StarX.showMsg("Successfully set the client window title.");
        }
    }
}
