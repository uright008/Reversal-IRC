package cn.stars.reversal.command.impl;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.ui.notification.NotificationType;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", description = "Binds the given module to the given key", syntax = ".bind <module> <key>", aliases = "bind")
public final class Bind extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final Module[] modules = Reversal.moduleManager.getModuleList();

        for (final Module m : modules) {
            if (args[0].equalsIgnoreCase(m.getModuleInfo().name())) {
                args[1] = args[1].toUpperCase();
                final int key = Keyboard.getKeyIndex(args[1]);

                m.setKeyBind(key);

                Reversal.notificationManager.registerNotification("Bound " + m.getModuleInfo().name() + " with key " + Keyboard.getKeyName(key) + ".", NotificationType.SUCCESS);
                Reversal.showMsg("Bound " + m.getModuleInfo().name() + " with key " + Keyboard.getKeyName(key) + ".");
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

        Reversal.notificationManager.registerNotification("Invalid module.", "Command", NotificationType.ERROR);
        Reversal.showMsg("Invalid module.");
    }
}
