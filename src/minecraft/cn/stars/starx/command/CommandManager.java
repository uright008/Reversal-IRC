package cn.stars.starx.command;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.misc.ClassUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public final class CommandManager {

    public static Command[] commandList = new Command[0];

    public void callCommand(final String input) {
        final String[] spit = input.split(" ");
        final String command = spit[0];
        final String args = input.substring(command.length()).trim();

        for (final Command c : commandList) {
            for (final String alias : c.getCommandInfo().aliases()) {
                if (alias.equalsIgnoreCase(command)) {
                    try {
                        c.onCommand(args, args.split(" "));
                    } catch (final Exception e) {
                        e.printStackTrace();
                        StarX.notificationManager.registerNotification(
                                "Invalid command usage \"" + c.getCommandInfo().syntax() + "\"."
                                , "Command", NotificationType.ERROR);
                        StarX.showMsg("Invalid command usage \"" + c.getCommandInfo().syntax() + "\".");
                    }

                    return;
                }
            }
        }

        for (final Module module : StarX.moduleManager.getModuleList()) {
            if (module.getModuleInfo().name().equalsIgnoreCase(command)) {
                if (spit.length > 1) {

                    if (module.getSettingAlternative(spit[1]) != null) {
                        final Setting setting = module.getSettingAlternative(spit[1]);

                        try {
                            try {
                                if (setting instanceof BoolValue) {
                                    ((BoolValue) setting).setEnabled(Boolean.parseBoolean(spit[2]));
                                } else if (setting instanceof NumberValue) {
                                    ((NumberValue) setting).setValue(Double.parseDouble(spit[2]));
                                } else if (setting instanceof ModeValue) {
                                    ((ModeValue) setting).set(spit[2]);
                                }

                            } catch (final NumberFormatException ignored) {
                                StarX.notificationManager.registerNotification("Settings name error. Dont type space!", "Command", NotificationType.ERROR);
                                StarX.showMsg("Settings name error. Dont type space!");
                                return;
                            }
                        } catch (final ArrayIndexOutOfBoundsException ignored) {
                            StarX.notificationManager.registerNotification("Settings name error. Dont type space!", "Command", NotificationType.ERROR);
                            StarX.showMsg("Settings name error. Dont type space!");
                        }

                        return;
                    }

                    StarX.notificationManager.registerNotification("Settings " + spit[1].toLowerCase() + " in " + command.toLowerCase() + " doesn't exist!", "Command", NotificationType.ERROR);
                    StarX.showMsg("Settings " + spit[1].toLowerCase() + " in " + command.toLowerCase() + " doesn't exist!");
                    return;
                } else {
                    module.toggleModule();
                    return;
                }
            }
        }

        StarX.notificationManager.registerNotification("Module or command " + command.toLowerCase() + " doesn't exist.", "Command", NotificationType.ERROR);
        StarX.showMsg("Module or command " + command.toLowerCase() + " doesn't exist.");
    }

    public void registerCommands() {
        for (Command command : ClassUtil.instantiateList(ClassUtil.resolvePackage(this.getClass().getPackage().getName() + ".impl", Command.class))) {
            commandList = Arrays.stream(Stream.concat(Arrays.stream(commandList), Stream.of(command))
                    .toArray(Command[]::new)).sorted(Comparator.comparing(c -> c.getCommandInfo().name())).toArray(Command[]::new);
        }
    }
}
