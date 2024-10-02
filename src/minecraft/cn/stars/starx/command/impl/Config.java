package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.config.ConfigHandler;
import cn.stars.starx.ui.notification.NotificationType;

@CommandInfo(name = "Config", description = "Modify your configs", syntax = ".config <save/create/load/list/delete> <name>", aliases = {"config", "cfg"})
public final class Config extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        StarX.executorService.execute(() -> {
            switch (args[0].toLowerCase()) {
                case "save": {
                    if (args[1].isEmpty()) {
                        StarX.showMsg("Invalid config name.");
                        StarX.showMsg(".config save <name>");
                        StarX.notificationManager.registerNotification("Invalid config name.", "Command", NotificationType.ERROR);
                        return;
                    }
                    ConfigHandler.save(args[1]);
                    break;
                }

                case "create": {
                    if (args[1].isEmpty()) {
                        StarX.showMsg("Invalid config name.");
                        StarX.showMsg(".config create <name>");
                        StarX.notificationManager.registerNotification("Invalid config name.", "Command", NotificationType.ERROR);
                        return;
                    }
                    ConfigHandler.create(args[1]);
                    break;
                }

                case "load": {
                    ConfigHandler.load(args[1]);
                    break;
                }

                case "list": {
                    ConfigHandler.list();
                    break;
                }

                case "delete": {
                    ConfigHandler.delete(args[1]);
                    break;
                }

                default: {
                    StarX.showMsg(".config <save/create/load/list/delete> <name>");
                    StarX.notificationManager.registerNotification("Invalid usage of command.", "Command", NotificationType.ERROR);
                }
            }
        });
    }
}
