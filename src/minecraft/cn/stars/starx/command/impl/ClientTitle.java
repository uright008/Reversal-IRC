package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.render.ThemeUtil;
import cn.stars.starx.util.starx.Branch;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

@CommandInfo(name = "ClientTitle", description = "Customize the client window title", syntax = ".clienttitle <name/reset>", aliases = {"clienttitle", "ct"})
public final class ClientTitle extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equals("%reset%")) {
            Display.setTitle(StarX.NAME + " " + StarX.VERSION + " " + Branch.getBranchName(StarX.BRANCH) + " | LWJGL " + Sys.getVersion() + " | " + StarX.wittyTitle[RandomUtils.nextInt(0, StarX.wittyTitle.length)]);
            StarX.INSTANCE.getNotificationManager().registerNotification("Successfully reset the client window title.", "Command", NotificationType.SUCCESS);
            StarX.INSTANCE.showMsg("Successfully reset the client window title.");
        } else  {
            Display.setTitle(String.join(" ", args));
            StarX.INSTANCE.getNotificationManager().registerNotification("Successfully set the client window title.", "Command", NotificationType.SUCCESS);
            StarX.INSTANCE.showMsg("Successfully set the client window title.");
        }
    }
}
