package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import cn.stars.starx.module.Module;
import cn.stars.starx.ui.notification.NotificationType;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", description = "Binds the given module to the given key", syntax = ".bind <module> <key>", aliases = "bind")
public final class Bind extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final Module[] modules = StarX.INSTANCE.getModuleManager().getModuleList();

        for (final Module m : modules) {
            if (args[0].equalsIgnoreCase(m.getModuleInfo().name())) {
                args[1] = args[1].toUpperCase();
                final int key = Keyboard.getKeyIndex(args[1]);

                m.setKeyBind(key);

                StarX.INSTANCE.getNotificationManager().registerNotification("Bound " + m.getModuleInfo().name() + " with key " + Keyboard.getKeyName(key) + ".", NotificationType.SUCCESS);
                StarX.INSTANCE.showMsg("Bound " + m.getModuleInfo().name() + " with key " + Keyboard.getKeyName(key) + ".");
                return;
            }
        }

//        for (final Script script : Rise.INSTANCE.getScriptManager().getScripts()) {
//            if (args[0].equalsIgnoreCase(script.getName())) {
//                args[1] = args[1].toUpperCase();
//                final int key = Keyboard.getKeyIndex(args[1]);
//
//                script.setKey(key);
//
//                Rise.INSTANCE.getNotificationManager().registerNotification("Set " + script.getName() + "'s bind to " + Keyboard.getKeyName(key) + ".");
//                return;
//            }
//        }

        StarX.INSTANCE.getNotificationManager().registerNotification("Invalid module.", "Command", NotificationType.ERROR);
        StarX.INSTANCE.showMsg("Invalid module.");
    }
}
