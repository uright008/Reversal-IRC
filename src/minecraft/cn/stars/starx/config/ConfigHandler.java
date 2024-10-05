package cn.stars.starx.config;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.render.ThemeUtil;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@UtilityClass
public final class ConfigHandler {

    private final String s = File.separator;

    public void save(final String name) {
        final StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("StarX_Version_").append(StarX.VERSION).append("\r\n");
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");
        configBuilder.append("CustomPlayerName_").append(StarX.customName).append("\r\n");
        configBuilder.append("MainMenuBackground_").append(StarX.backgroundId).append("\r\n");
        configBuilder.append("DisableShader_").append(false).append("\r\n");

        for (final Module m : StarX.moduleManager.getModuleList()) {
            final String moduleName = m.getModuleInfo().name();
            configBuilder.append("Toggle_").append(moduleName).append("_").append(m.isEnabled()).append("\r\n");

            if (m.getModuleInfo().category().equals(Category.HUD)) {
                configBuilder.append("PositionX_").append(moduleName).append("_").append(m.getX()).append("\r\n");
                configBuilder.append("PositionY_").append(moduleName).append("_").append(m.getY()).append("\r\n");
            }

            for (final Setting setting : m.getSettings()) {
                if (setting instanceof BoolValue) {
                    configBuilder.append("BoolValue_").append(moduleName).append("_").append(setting.name).append("_").append(((BoolValue) setting).enabled).append("\r\n");
                }
                if (setting instanceof NumberValue) {
                    configBuilder.append("NumberValue_").append(moduleName).append("_").append(setting.name).append("_").append(((NumberValue) setting).value).append("\r\n");
                }
                if (setting instanceof ModeValue) {
                    configBuilder.append("ModeValue_").append(moduleName).append("_").append(setting.name).append("_").append(((ModeValue) setting).getMode()).append("\r\n");
                }
            }
            configBuilder.append("Bind_").append(moduleName).append("_").append(m.getKeyBind()).append("\r\n");
        }

        FileUtil.saveFile("Config" + s + name + ".txt", true, configBuilder.toString());
        StarX.notificationManager.registerNotification("Config saved " + name + ".");
    }

    public void create(final String name) {
        final StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("StarX_Version_").append(StarX.VERSION).append("\r\n");
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");

        FileUtil.saveFile("Config" + s + name + ".txt", true, configBuilder.toString());
        StarX.notificationManager.registerNotification("Config created " + name + ".");
    }

    public void load(final String name) {
        final String config = FileUtil.loadFile("Config" + s + name + ".txt");
        if (config == null) {
            StarX.notificationManager.registerNotification("Config does not exist.");
            return;
        }

        final String[] configLines = config.split("\r\n");

        for (final Module m : StarX.moduleManager.getModuleList()) {
            if (m.isEnabled()) {
                m.toggleModule();
            }
        }

        boolean gotConfigVersion = false;
        for (final String line : configLines) {

            final String[] split = line.split("_");
            if (split[0].contains("StarX")) {
                if (split[1].contains("Version")) {
                    gotConfigVersion = true;

                    final String clientVersion = StarX.VERSION;
                    final String configVersion = split[2];

                    if (!clientVersion.equalsIgnoreCase(configVersion)) {
                        StarX.showMsg("This config was made in a different version of StarX! Incompatibilities are expected.");
                        StarX.notificationManager.registerNotification(
                                "This config was made in a different version of StarX! Incompatibilities are expected.", NotificationType.WARNING
                        );
                    }
                }
            }

            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split.length > 1 ? split[1] : "");
                continue;
            }

            if (split[0].contains("CustomPlayerName")) {
                StarX.customName = split.length > 1 ? split[1] : "";
                continue;
            }

            if (split[0].contains("ChatMSG")) {
                StarX.showMsg(split[1]);
            }

            if (split[0].contains("MainMenuBackground")) {
                StarX.backgroundId = Integer.parseInt(split[1]);
                continue;
            }

            if (split[0].contains("DisableShader")) {
                StarXLogger.warn("Detected <DisableShader> option while loading config in game, ignored.");
                continue;
            }


            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (StarX.moduleManager.getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(StarX.moduleManager.getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                            module.onLoad();
                        }
                    }
                }
            }

            if (split[0].contains("PositionX")) {
                Objects.requireNonNull(StarX.moduleManager.getModule(split[1])).setX(Integer.parseInt(split[2]));
            }
            if (split[0].contains("PositionY")) {
                Objects.requireNonNull(StarX.moduleManager.getModule(split[1])).setY(Integer.parseInt(split[2]));
            }

            final Setting setting = StarX.moduleManager.getSetting(split[1], split[2]);

            if (StarX.moduleManager.getModule(split[1]) != null) {

                if (split[0].contains("BoolValue") && setting instanceof BoolValue) {
                    if (split[3].contains("true")) {
                        ((BoolValue) setting).enabled = true;
                    }

                    if (split[3].contains("false")) {
                        ((BoolValue) setting).enabled = false;
                    }
                }

                if (split[0].contains("NumberValue") && setting instanceof NumberValue) {
                    ((NumberValue) setting).setValue(Double.parseDouble(split[3]));
                }

                if (split[0].contains("ModeValue") && setting instanceof ModeValue) {
                    ((ModeValue) setting).set(split[3]);
                }
            }

            if (split[0].contains("Bind")) {
                final Module m = StarX.moduleManager.getModule(split[1]);

                if (m != null)
                    Objects.requireNonNull(m).setKeyBind(Integer.parseInt(split[2]));
            }
        }
        if (!gotConfigVersion) {
            StarX.showMsg("This config was made in a different version of StarX! Incompatibilities are expected.");
            StarX.notificationManager.registerNotification(
                    "This config was made in a different version of StarX! Incompatibilities are expected.", NotificationType.WARNING
            );
        }


        //Notification
        StarX.notificationManager.registerNotification("Config loaded " + name + ".");
    }

    public void loadFromList(final List<String> list) {
        for (final Module m : StarX.moduleManager.getModuleList()) {
            if (m.isEnabled() && !m.getModuleInfo().name().toLowerCase().contains("clickgui")) {
                m.toggleModule();
            }
        }

        for (final String line : list) {
            if (line == null) return;

            final String[] split = line.split("_");

            if (StarX.moduleManager.getModule(split[1]) != null) {
                final Module module = Objects.requireNonNull(StarX.moduleManager.getModule(split[1]));

                if (module.getModuleInfo().name().toLowerCase().contains("clickgui")) {
                    continue;
                }
            }


            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split[1]);
                continue;
            }

            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (StarX.moduleManager.getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(StarX.moduleManager.getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                        }
                    }
                }
            }

            final Setting setting = StarX.moduleManager.getSetting(split[1], split[2]);

            if (split[0].contains("BoolValue") && setting instanceof BoolValue) {
                if (split[3].contains("true")) {
                    ((BoolValue) setting).enabled = true;
                }

                if (split[3].contains("false")) {
                    ((BoolValue) setting).enabled = false;
                }
            }

            if (split[0].contains("NumberValue") && setting instanceof NumberValue) {
                ((NumberValue) setting).setValue(Double.parseDouble(split[3]));
            }

            if (split[0].contains("ModeValue") && setting instanceof ModeValue) {
                ((ModeValue) setting).set(split[3]);
            }
        }
    }

    public void list() {
        if (!FileUtil.exists("Config\\"))
            StarX.notificationManager.registerNotification("No configs created.");
        final File configFolder = FileUtil.getFileOrPath("Config\\");

        if (configFolder.listFiles() == null || Objects.requireNonNull(configFolder.listFiles()).length < 1) {
            StarX.notificationManager.registerNotification("No configs created.");
        } else {
            StarX.showMsg("List of configuration files: ");

            for (final File file : Objects.requireNonNull(configFolder.listFiles())) {
                StarX.showMsg(" - " + file.getName().replace(".txt", ""));
            }
        }
    }

    public void delete(final String name) {
        if (FileUtil.exists("Config\\" + name + ".txt")) {
            StarX.notificationManager.registerNotification("Config does not exist.");
            return;
        }

        FileUtil.delete("Config\\" + name + ".txt");
        StarX.notificationManager.registerNotification("Config " + name + " has been deleted.");
    }

    public void loadFromRes(final String name) {
        final URL defaultImage = ConfigHandler.class.getResource(s + "assets" + s + "minecraft" + s + "rise" + s + "defaultcfg" + s + name + ".txt");
        final File loadFile;
        try {
            loadFile = new File(defaultImage.toURI());
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            StarX.notificationManager.registerNotification("Error while loading config");
            return;
        }

        StarX.notificationManager.registerNotification(loadFile.getAbsolutePath());

        final boolean exists = loadFile.exists();

        if (!exists) {
            StarX.notificationManager.registerNotification("Error while loading config");
            return;
        }

        Scanner scan = null;

        try {
            scan = new Scanner(loadFile);
        } catch (final IOException e1) {
            StarX.notificationManager.registerNotification("Error while loading config");
            e1.printStackTrace();
        }

        for (final Module m : StarX.moduleManager.getModuleList()) {
            if (m.isEnabled()) {
                m.toggleModule();
            }
        }

        while (true) {
            assert scan != null;
            if (!scan.hasNextLine()) break;

            final String line = scan.nextLine();

            if (line == null) return;

            final String[] spit = line.split("_");

            if (spit[0].contains("Toggle")) {
                if (spit[2].contains("true")) {
                    if (StarX.moduleManager.getModule(spit[1]) != null) {
                        final Module module = Objects.requireNonNull(StarX.moduleManager.getModule(spit[1]));

                        if (!module.isEnabled()) {
                            module.toggleModule();
                        }
                    }
                }
            }

            final Setting setting = StarX.moduleManager.getSetting(spit[1], spit[2]);

            if (spit[0].contains("BoolValue") && setting instanceof BoolValue) {
                if (spit[3].contains("true")) {
                    ((BoolValue) setting).enabled = true;
                }

                if (spit[3].contains("false")) {
                    ((BoolValue) setting).enabled = false;
                }
            }

            if (spit[0].contains("NumberValue") && setting instanceof NumberValue) {
                ((NumberValue) setting).setValue(Double.parseDouble(spit[3]));
            }

            if (spit[0].contains("ModeValue") && setting instanceof ModeValue) {
                ((ModeValue) setting).set(spit[3]);
            }
        }

        //Notification
        StarX.notificationManager.registerNotification("Config loaded " + name + ".");
    }
}
