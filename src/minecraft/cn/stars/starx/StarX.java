package cn.stars.starx;

import cn.stars.addons.creativetab.StarXTab;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.impl.*;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleManager;
import cn.stars.starx.module.impl.addons.*;
import cn.stars.starx.module.impl.combat.NoClickDelay;
import cn.stars.starx.module.impl.hud.*;
import cn.stars.starx.module.impl.misc.ClientSpoofer;
import cn.stars.starx.module.impl.misc.NoAchievements;
import cn.stars.starx.module.impl.misc.Protocol;
import cn.stars.starx.module.impl.movement.Sprint;
import cn.stars.starx.module.impl.player.HealthWarn;
import cn.stars.starx.module.impl.render.*;
import cn.stars.starx.module.impl.world.LightningTracker;
import cn.stars.starx.module.impl.world.TimeTraveller;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.clickgui.ClickGUI;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.ui.clickgui.strikeless.StrikeGUI;
import cn.stars.starx.ui.hud.Hud;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.ThemeUtil;
import cn.stars.starx.util.starx.Branch;
import de.florianmichael.viamcp.ViaMCP;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * TODO: IMPORTANT INFORMATION
 *
 * Assets are missing because of optifine. We fixed it.
 */
@Getter
public enum StarX implements GameInstance {
    INSTANCE;
    // Client Info
    public static final String NAME = "StarX";
    public static final String VERSION = "v1.3.2";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public static final String AUTHOR = "Stars";
    public static final Branch BRANCH = Branch.PRODUCTION;
    public static int CLIENT_THEME_COLOR_DEFAULT = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT = new Color(185, 69, 255).hashCode();
    public static Color CLIENT_THEME_COLOR_BRIGHT_COLOR = new Color(185, 69, 255);
    public static boolean isDestructed = false;

    // Witty comments
    public static final String[] wittyCrashReport = new String[]
            {"玩原神玩的", "粥批差不多得了", "原神?启动!", "哇真的是你啊", "你怎么似了", "加瓦,救一下啊", "Bomb has been planted",
                    "闭嘴!我的父亲在mojang工作,他可以使你的mInEcRaFt崩溃", "纪狗气死我了", "致敬传奇耐崩王MiNeCrAfT", "你的客户端坠机了",
            "It's been a long day without you my friend", "回来吧牢端"};

    public static final String[] wittyTitle = new String[]
            {"沙勒味精怎么那么喜欢白洲梓? #(疑问)", "为什么删我的帖子,流小珍珠了", "How high is your priority?", "起星鱼了", "你是玩什么游戏玩的?",
                    "Hey shitass wanna see me open source?", "即使被全世界抛弃"};


    // Init
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static boolean isAMDShaderCompatibility = false;
    public static int backgroundId = 0;

    public ModuleManager moduleManager;
    public NotificationManager notificationManager;
    public CommandManager cmdManager;
    public ModernClickGUI modernClickGUI;
    public ClickGUI clickGUI;
    public StrikeGUI strikeGUI;
    public GuiTheme guiTheme;
    public CreativeTabs creativeTab;
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
            Display.setTitle(NAME + " " + VERSION + " " + Branch.getBranchName(BRANCH) + " | LWJGL " + Sys.getVersion() + " | " + wittyTitle[RandomUtils.nextInt(0, wittyTitle.length)]);

            // ViaMCP init
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();

            StarXLogger.info("Loading client...");
            initialize();
            loadConfigs();
            loadStatistics();

            StarXLogger.info("Client loaded successfully.");
            StarXLogger.info(NAME + " " + VERSION + " (Minecraft " + MINECRAFT_VERSION + "), made with love by " + AUTHOR + ".");
        } catch (Exception e) {
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }
    }
    public void stop() {
        saveAll();
    }

    // Usages
    public void showMsg(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b" + NAME + "§7] §r" + msg));
        }
    }
    public void showMsg(Object msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b" + NAME + "§7] §r" + msg));
        }
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

                    final String configVersion = split[2];

                    if (!VERSION.equalsIgnoreCase(configVersion)) {
                        showMsg("This config was made in a different version of StarX.");
                        getNotificationManager().registerNotification(
                                "This config was made in a different version of StarX.", NotificationType.WARNING
                        );
                    }
                }
            }


            if (split[0].contains("ClientName")) {
                ThemeUtil.setCustomClientName(split.length > 1 ? split[1] : "");
                continue;
            }

            if (split[0].contains("MainMenuBackground")) {
                backgroundId = Integer.parseInt(split[1]);
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

            if (split[0].contains("PositionX")) {
                Objects.requireNonNull(getModuleManager().getModule(split[1])).setX(Integer.parseInt(split[2]));
            }
            if (split[0].contains("PositionY")) {
                Objects.requireNonNull(getModuleManager().getModule(split[1])).setY(Integer.parseInt(split[2]));
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
        configBuilder.append("ClientName_").append(ThemeUtil.getCustomClientName()).append("\r\n");
        configBuilder.append("MainMenuBackground_").append(backgroundId).append("\r\n");

        for (final Module m : getModuleManager().getModuleList()) {
            final String moduleName = m.getModuleInfo().name();
            configBuilder.append("Toggle_").append(moduleName).append("_").append(m.isEnabled()).append("\r\n");

            if (m.getModuleInfo().category().equals(Category.HUD)) {
                configBuilder.append("PositionX_").append(moduleName).append("_").append(m.getX()).append("\r\n");
                configBuilder.append("PositionY_").append(moduleName).append("_").append(m.getY()).append("\r\n");
            }
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

    public void initialize() {
        try {
            // Minecraft Pre-Initialize
            // Shut the fuck fast render off
            mc.gameSettings.ofFastRender = false;

            // StarX Initialize
            moduleManager = new ModuleManager();
            moduleManager.registerModules(modules);

            Hud.initializeModules();

            notificationManager = new NotificationManager();

            cmdManager = new CommandManager();
            CommandManager.COMMANDS = commands;

            guiTheme = new GuiTheme();
            modernClickGUI = new ModernClickGUI();
            clickGUI = new ClickGUI();
            strikeGUI = new StrikeGUI();

            creativeTab = new StarXTab();

        }
        catch (final Exception e) {
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }

        try {
            // 创建文件夹
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
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }
    }

    public boolean onSendChatMessage(final String s) {
        if (s.startsWith(".") && !s.startsWith("./") && !isDestructed) {
            cmdManager.callCommand(s.substring(1));
            return false;
        }
    //    return !s.startsWith("-");
        return true;
        // 我操你妈的irc
    }

    private final Command[] commands = new Command[] {
            new Bind(),
            new ClientName(),
            new ClientTitle(),
            new Config(),
            new Help(),
            new SelfDestruct()
    };

    public final Module[] modules = new Module[] {
            // Addons
            new FreeLook(),
            new MoBends(),
            new PostProcessing(),
            new WaveyCapes(),
            new SkinLayers3D(),
            new SpecialGuis(),
            new ScreenshotViewer(),
            // Combat
            new NoClickDelay(),
            // Movement
            new Sprint(),
            // Misc
            new ClientSpoofer(),
            new NoAchievements(),
            new Protocol(),
            // World
            new LightningTracker(),
            // Player
            new HealthWarn(),
            // Render
            new Animations(),
            new BlockOverlay(),
            new Breadcrumbs(),
            new ClickGui(),
            new ChinaHat(),
            new DamageParticle(),
            new Fullbright(),
            new HitEffect(),
            new ItemPhysics(),
            new MotionBlur(),
            new NoBob(),
            new TargetESP(),
            new TNTTimer(),
            new TimeTraveller(),
            new TrueSights(),
            new Particles(),
            new SpeedGraph(),
            new Wings(),
            // Hud
            new Arraylist(),
            new BASticker(),
            new BPSCounter(),
            new CPSCounter(),
            new ClientSettings(),
            new HUD(),
            new Keystrokes(),
            new Scoreboard(),
            new SessionInfo(),
            new TargetHud(),
            new TextGui(),
            new TestElement()
    };
}