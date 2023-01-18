package cn.stars.starx;

import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.impl.Bind;
import cn.stars.starx.command.impl.ClientName;
import cn.stars.starx.command.impl.ClientTitle;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleManager;
import cn.stars.starx.module.impl.addons.NoClickDelay;
import cn.stars.starx.module.impl.addons.WaveyCapes;
import cn.stars.starx.module.impl.movement.KeepSprint;
import cn.stars.starx.module.impl.misc.ClientSpoofer;
import cn.stars.starx.module.impl.misc.NoAchievements;
import cn.stars.starx.module.impl.misc.Plugins;
import cn.stars.starx.module.impl.player.HealthWarn;
import cn.stars.starx.module.impl.render.*;
import cn.stars.starx.module.impl.world.BanChecker;
import cn.stars.starx.module.impl.world.LightningTracker;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.clickgui.ClickGUI;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.ui.theme.Theme;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.render.ThemeUtil;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.awt.*;
import java.io.File;
import java.util.Objects;
/*
 * TODO: IMPORTANT INFORMATION
 *
 * Assets are missing because of optifine.We fixed it.
 */
@Getter
public enum StarX {
    INSTANCE;
    // Client Info
    public static final String NAME = "StarX";
    public static final String VERSION = "0015";
    public static int CLIENT_THEME_COLOR_DEFAULT = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT = new Color(185, 69, 255).hashCode();
    public static Color CLIENT_THEME_COLOR_BRIGHT_COLOR = new Color(185, 69, 255);

    // Init
    public ModuleManager moduleManager;
    public NotificationManager notificationManager;
    public CommandManager cmdManager;
    public ClickGUI clickGUI;
    private ResourceLocation backgroundTexture;
    private DynamicTexture viewportTexture;
    public GuiTheme guiTheme;
    public static String ip;
    public static int totalKills;
    public static int totalDeaths;
    public static float distanceRan;
    public static float distanceFlew;
    public static float distanceJumped;
    public static int amountOfModulesOn;
    public static int amountOfVoidSaves;
    public static int amountOfConfigsLoaded;
    public static boolean firstBoot;

    // Core
    public void start() {
        try {
            Display.setTitle(NAME + " " + VERSION);
            // ViaMCP init
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
            // configs
            initCore();
            loadConfigs();
            loadStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stop() {
        saveAll();
    }

    // Usages
    public void showMsg(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b" + NAME + "§7] " + msg));
        }
    }

    public void textureStart() {
        viewportTexture = new DynamicTexture(256, 256);
        backgroundTexture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
    }

    // run required
    public void saveAll() {
        saveConfig();
        saveStatistics();
    }
    public void loadConfigs() {
        final String config = FileUtil.loadFile("settings.txt");
        if (config == null) return;
        final String[] configLines = config.split("\r\n");

        for (final Module m : getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleNoEvent();
            }
        }

        for (final Module m : getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleNoEvent();
            }
        }

        boolean gotConfigVersion = false;
        for (final String line : configLines) {
            if (line == null) return;

            final String[] split = line.split("_");
            if (split[0].contains("StarX")) {
                if (split[1].contains("Version")) {
                    gotConfigVersion = true;

                    final String clientVersion = VERSION;
                    final String configVersion = split[2];

                    if (!clientVersion.equalsIgnoreCase(configVersion)) {
                        showMsg("This config was made in a different version of StarX.");
                        getNotificationManager().registerNotification(
                                "This config was made in a different version of StarX.", NotificationType.WARNING
                        );
                    }
                }
            }

            if (split[0].contains("MainMenuTheme")) {
                getGuiTheme().setCurrentTheme(Theme.valueOf(split[1]));
                continue;
            }

            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split.length > 1 ? split[1] : "");
                continue;
            }

        //    if (split[0].contains("PlayMusic")) {
        //        Minecraft.getMinecraft().riseMusicTicker.shouldKeepPlaying = Boolean.parseBoolean(split[1]);
        //        continue;
        //    }

            if (split[0].contains("Toggle")) {
                if (split[2].contains("true")) {
                    if (getModuleManager().getModule(split[1]) != null) {
                        final Module module = Objects.requireNonNull(getModuleManager().getModule(split[1]));

                        if (!module.isEnabled()) {
                            module.toggleNoEvent();
                        }
                    }
                }
            }

            final Setting setting = getModuleManager().getSetting(split[1], split[2]);

            if (split[0].contains("BoolValue") && setting instanceof BoolValue) {
                if (split[3].contains("true")) {
                    ((BoolValue) setting).enabled = true;
                }

                if (split[3].contains("false")) {
                    ((BoolValue) setting).enabled = false;
                }
            }

            if (split[0].contains("NumberValue") && setting instanceof NumberValue)
                ((NumberValue) setting).setValue(Double.parseDouble(split[3]));

            if (split[0].contains("ModeValue") && setting instanceof ModeValue)
                ((ModeValue) setting).set(split[3]);

            if (split[0].contains("Bind")) {
                final Module m = getModuleManager().getModule(split[1]);

                if (m != null)
                    Objects.requireNonNull(m).setKeyBind(Integer.parseInt(split[2]));
            }
        }
        if (!gotConfigVersion) {
            showMsg("This config was made in a different version of StarX.");
            getNotificationManager().registerNotification(
                    "This config was made in a different version of StarX.", NotificationType.WARNING
            );
        }
    }

    public void loadStatistics() {
        final String statistics = FileUtil.loadFile("statistics.txt");
        if (statistics == null) return;

        final String[] statisticsLines = statistics.split("\r\n");

        for (final String line : statisticsLines) {
            if (line == null) return;

            final String[] split = line.split("_");

            if (split[0].contains("Kills"))
                totalKills = Integer.parseInt(split[1]);

            if (split[0].contains("Deaths"))
                totalDeaths = Integer.parseInt(split[1]);

            if (split[0].contains("DistanceRan"))
                distanceRan = Float.parseFloat(split[1]);

            if (split[0].contains("DistanceFlew"))
                distanceFlew = Float.parseFloat(split[1]);

            if (split[0].contains("DistanceJumped"))
                distanceJumped = Float.parseFloat(split[1]);

            if (split[0].contains("VoidSaves"))
                amountOfVoidSaves = Integer.parseInt(split[1]);

            if (split[0].contains("ConfigsLoaded"))
                amountOfConfigsLoaded = Integer.parseInt(split[1]);
        }
    }
    private void saveConfig() {
        final StringBuilder configBuilder = new StringBuilder();
        configBuilder.append("StarX_Version_").append(VERSION).append("\r\n");
      //  configBuilder.append("PlayMusic_").append(Minecraft.getMinecraft().riseMusicTicker.shouldKeepPlaying).append("\r\n");
        configBuilder.append("MainMenuTheme_").append(getGuiTheme().getCurrentTheme()).append("\r\n");
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");

        for (final Module m : getModuleManager().getModuleList()) {
            final String moduleName = m.getModuleInfo().name();
            configBuilder.append("Toggle_").append(moduleName).append("_").append(m.isEnabled()).append("\r\n");

            for (final Setting s : m.getSettings()) {
                if (s instanceof BoolValue) {
                    configBuilder.append("BoolValue_").append(moduleName).append("_").append(s.name).append("_").append(((BoolValue) s).enabled).append("\r\n");
                }
                if (s instanceof NumberValue) {
                    configBuilder.append("NumberValue_").append(moduleName).append("_").append(s.name).append("_").append(((NumberValue) s).value).append("\r\n");
                }
                if (s instanceof ModeValue) {
                    configBuilder.append("ModeValue_").append(moduleName).append("_").append(s.name).append("_").append(((ModeValue) s).getMode()).append("\r\n");
                }
            }
            configBuilder.append("Bind_").append(moduleName).append("_").append(m.getKeyBind()).append("\r\n");
        }

        FileUtil.saveFile("settings.txt", true, configBuilder.toString());
    }
    private void saveStatistics() {
        final String statisticsBuilder = "Kills_" + totalKills + "\r\n" +
                "Deaths_" + totalDeaths + "\r\n" +
                "DistanceRan_" + distanceRan + "\r\n" +
                "DistanceFlew_" + distanceFlew + "\r\n" +
                "DistanceJumped_" + distanceJumped + "\r\n" +
                "VoidSaves_" + amountOfVoidSaves + "\r\n" +
                "ConfigsLoaded_" + amountOfConfigsLoaded + "\r\n";
        FileUtil.saveFile("statistics.txt", true, statisticsBuilder);
    }

    public void initCore() {
        try {
            (moduleManager = new ModuleManager()).moduleList = modules;
            notificationManager = new NotificationManager();
            cmdManager = new CommandManager();
            CommandManager.COMMANDS = commands;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            guiTheme = new GuiTheme();
            clickGUI = new ClickGUI();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final Minecraft mc = Minecraft.getMinecraft();
        // 开了崩端
    //    mc.gameSettings.guiScale = 2;
    //    mc.gameSettings.ofFastRender = false;
    //    mc.gameSettings.ofSmoothFps = false;

        try {
            // Creating Rise folder
            if (!FileUtil.coreDirectoryExists()) {
                firstBoot = true;
                FileUtil.createCoreDirectory();
            }

            if (!FileUtil.exists("Config" + File.separator)) {
                FileUtil.createDirectory("Config" + File.separator);
            }

            if (!FileUtil.exists("Script" + File.separator)) {
                FileUtil.createDirectory("Script" + File.separator);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onSendChatMessage(final String s) {
        if (s.startsWith(".")) {
            cmdManager.callCommand(s.substring(1));
            return false;
        }
        //ircClient.write(s.substring(1));
        return !s.startsWith("-");
    }

    private final Command[] commands = new Command[] {
            new Bind(),
            new ClientName(),
            new ClientTitle()
    };

    private final Module[] modules = new Module[] {
            // Addons
            new WaveyCapes(),
            new NoClickDelay(),
            // Combat 我测你这要combat有什么用(
            // 走个仪式
            // Movement
            new KeepSprint(),
            // Misc
            new ClientSpoofer(),
            new NoAchievements(),
            new Plugins(),
            // World
            new BanChecker(),
            new LightningTracker(),
            // Player
            new HealthWarn(),
            // Render
            new Animations(),
            new Breadcrumbs(),
            new ChunkAnimator(),
            new ClickGui(),
            new ClientSettings(),
            new CPSCounter(),
            new ChinaHat(),
            new Fullbright(),
            new GuiAnimation(),
            new HitEffect(),
            new ItemPhysics(),
            new NoBob(),
            new TargetHud(),
            new TimeTraveller(),
            new Particles(),
            new SpeedGraph(),
            new Wings() // SB
    };
}