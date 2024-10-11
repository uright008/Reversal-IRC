package cn.stars.starx;

import cn.stars.addons.creativetab.StarXTab;
import cn.stars.addons.optimization.entityculling.EntityCullingMod;
import cn.stars.addons.optimization.util.FastTrig;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.impl.*;
import cn.stars.starx.config.DefaultHandler;
import cn.stars.starx.config.MusicHandler;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleManager;
import cn.stars.starx.module.impl.addons.*;
import cn.stars.starx.module.impl.combat.ClickSound;
import cn.stars.starx.module.impl.combat.ExperimentReachDistanceChecker;
import cn.stars.starx.module.impl.combat.NoClickDelay;
import cn.stars.starx.module.impl.hud.*;
import cn.stars.starx.module.impl.misc.ClientSpoofer;
import cn.stars.starx.module.impl.misc.CustomName;
import cn.stars.starx.module.impl.misc.NoAchievements;
import cn.stars.starx.module.impl.misc.Protocol;
import cn.stars.starx.module.impl.movement.Sprint;
import cn.stars.starx.module.impl.player.HealthWarn;
import cn.stars.starx.module.impl.player.IRC;
import cn.stars.starx.module.impl.render.*;
import cn.stars.starx.module.impl.world.TimeTraveller;
import cn.stars.starx.music.MusicManager;
import cn.stars.starx.ui.clickgui.modern.MMTClickGUI;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.ui.hud.Hud;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.irc.User;
import cn.stars.starx.util.misc.VideoUtils;
import cn.stars.starx.util.starx.Branch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kAIS.KAIMyEntity.KAIMyEntity;
import de.florianmichael.viamcp.ViaMCP;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;
import tech.skidonion.obfuscator.annotations.StringEncryption;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * TODO: IMPORTANT INFORMATION
 *
 * Assets are missing because of optifine. We fixed it.
 */
@NativeObfuscation
@StringEncryption
@Getter
public class StarX {
    // Client Info
    public static final String NAME = "StarX";
    public static final String VERSION = "v3.0.0 Pre-release.2";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public static final String AUTHOR = "Starlight Team";
    public static final Branch BRANCH = Branch.DEVELOPMENT;

    // Witty comments
    public static final String[] wittyCrashReport = new String[]
            {"玩原神玩的", "粥批差不多得了", "原神?启动!", "哇真的是你啊", "你怎么似了", "加瓦,救一下啊", "Bomb has been planted",
                    "闭嘴!我的父亲在mojang工作,他可以使你的mInEcRaFt崩溃", "纪狗气死我了", "致敬传奇耐崩王MiNeCrAfT", "你的客户端坠机了",
            "It's been a long day without you my friend", "回来吧牢端", "为了你,我变成狼人模样"};

    // maybe
    public static final String[] wittyTitle = new String[]
            {"乌云再厚也遮不住阳光,风雨过后总会有彩虹", "与你的日常,就是奇迹", "虚假的真实是真实吗?", "未来仍有无限可能", "在那灿烂的群星中,总有一颗代表我正与你对视", "王女手握钥匙,方舟恭候多时",
            "一缕星光会给予我和你无尽的力量", "vanitas vanitatum et omnia vanitas", "情感在于表达,更在于理解", "古希腊掌握PVP的神", "从来如此,便对吗?", "你所珍视的那个人,是否也同样珍视你?", "我有鱼鱼症"};
//    public static final String[] wittyTitle = new String[] { "你所珍视的那个人,是否也同样珍视你?", "虚空的虚空,凡事都是虚空"};


    // Init
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static EntityCullingMod entityCullingMod;

    public static User user = null;
    public static boolean hasJavaFX = true;
    public static boolean isAMDShaderCompatibility = false;
    public static boolean isViaCompatibility = false;

    public static int backgroundId = 9; // ?
    public static String customName = "";
    public static String customText = ".setText <text>";

    public static ModuleManager moduleManager;
    public static NotificationManager notificationManager;
    public static CommandManager cmdManager;
    public static MusicManager musicManager;
    public static ModernClickGUI modernClickGUI;
    public static MMTClickGUI mmtClickGUI;
    public static GuiTheme guiTheme;
    public static CreativeTabs creativeTab;
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
    public static void start() {
        try {
            StarXLogger.info("Loading client...");

            DefaultHandler.loadClientMod();

            // ViaMCP init
            Minecraft.setStage(4);
            if (!isViaCompatibility) {
                ViaMCP.create();
                ViaMCP.INSTANCE.initAsyncSlider();
            }

            initialize();

            Minecraft.setStage(7);
            DefaultHandler.loadConfigs();

            Minecraft.setStage(8);
            DefaultHandler.loadStatistics();

            Minecraft.setStage(9);
            postInitialize();

            Display.setTitle(NAME + " " + VERSION + " " + Branch.getBranchName(BRANCH) + " | " + wittyTitle[RandomUtil.INSTANCE.nextInt(0, wittyTitle.length)]);
            StarXLogger.info("Client loaded successfully.");
            StarXLogger.info(NAME + " " + VERSION + " (Minecraft " + MINECRAFT_VERSION + "), made with love by " + AUTHOR + ".");
        } catch (Exception e) {
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }
    }
    public static void stop() {
        saveAll();
    }

    // Usages
    public static void showMsg(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b" + NAME + "§7] §r" + msg));
        }
    }
    public static void showMsg(Object msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b" + NAME + "§7] §r" + msg));
        }
    }

    // run required
    public static void saveAll() {
        DefaultHandler.saveConfig(false);
        DefaultHandler.saveStatistics();
        DefaultHandler.saveClientMod();
    }


    public static void initialize() {
        try {
            Minecraft.setStage(5);

            // Minecraft Pre-Initialize
            // Shut the fuck fast render off
            Minecraft.getMinecraft().gameSettings.ofFastRender = false;

            // StarX Initialize
            moduleManager = new ModuleManager();
            moduleManager.registerModules(modules);

            notificationManager = new NotificationManager();

            cmdManager = new CommandManager();
            CommandManager.COMMANDS = commands;

            try {
                musicManager = new MusicManager();
                musicManager.initGUI();
            } catch (NoClassDefFoundError e) {
                hasJavaFX = false;
                StarXLogger.warn("No JavaFX found in the current java version! Music player is disabled.");
            }

            Minecraft.setStage(6);
            guiTheme = new GuiTheme();
            modernClickGUI = new ModernClickGUI();
            mmtClickGUI = new MMTClickGUI();

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

            if (!FileUtil.exists("Background" + File.separator)) {
                FileUtil.createDirectory("Background" + File.separator);
            }

            if (!FileUtil.exists("Cache" + File.separator)) {
                FileUtil.createDirectory("Cache" + File.separator);
            }
        } catch (final Exception e) {
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }
    }

    public static void postInitialize() {
        try {
            Hud.initializeModules();

            FastTrig.init();

            entityCullingMod = new EntityCullingMod();
            entityCullingMod.onInitialize();

            if (hasJavaFX) MusicHandler.load();

            if (!FileUtil.exists("Background" + File.separator)) {
                FileUtil.createDirectory("Background" + File.separator);
            }

            File tempFile = new File(Minecraft.getMinecraft().mcDataDir, "StarX/Background");
            File videoFile = new File(tempFile, "background.mp4");
            if (!tempFile.exists() || !videoFile.exists()) {
                tempFile.mkdir();
                try {
                    FileUtil.unpackFile(videoFile, "assets/minecraft/starx/background.mp4");
                } catch (IOException e) {
                    StarXLogger.error("Failed to unpack background file. Are there any Chinese characters in the booting dir?");
                    throw new RuntimeException(e);
                }
            }
            try {
                VideoUtils.init(videoFile);
            } catch (IOException e) {
                StarXLogger.error("Failed to load background file.");
                throw new RuntimeException(e);
            }

        } catch (final Exception e) {
            StarXLogger.error("An error has occurred while loading StarX: " + e);
        }
    }

    public static boolean onSendChatMessage(final String s) {
        if (s.startsWith(".") && !s.startsWith("./")) {
            cmdManager.callCommand(s.substring(1));
            return false;
        }
    //    return !s.startsWith("-");
        return true;
        // 我操你妈的irc
    }

    private static final Command[] commands = new Command[] {
            new Bind(),
            new Chat(),
            new ClientName(),
            new ClientTitle(),
            new Config(),
            new Help(),
            new Name(),
            new Say(),
            new SelfDestruct(),
            new SetText()
    };

    public static final Module[] modules = new Module[] {
            // Addons
            new GuiSettings(), // Special Module
            new Optimization(), // Special Module
            new FreeLook(),
            new MoBends(),
            new MusicPlayer(),
            new WaveyCapes(),
            new SkinLayers3D(), // Special Module
            // Combat
            new ClickSound(),
            new ExperimentReachDistanceChecker(),
            new NoClickDelay(),
            // Movement
            new Sprint(),
            // Misc
            new ClientSpoofer(),
            new CustomName(),
            new NoAchievements(),
            new Protocol(),
            // World
            // Player
            new HealthWarn(),
            new IRC(),
            // Render
            new Animations(),
            new BAHalo(),
            new BetterFont(),
            new BlockOverlay(),
            new Breadcrumbs(),
            new ChinaHat(),
            new ClickGui(), // Special Module
            new Crosshair(),
            new DamageParticle(),
            new Fullbright(),
            new HitEffect(),
            new HurtCam(), // Special Module
            new ItemPhysics(),
            new JumpCircle(),
            new MotionBlur(),
            new NoBob(),
            new ReachDisplay(),
            new TargetESP(),
            new TNTTimer(),
            new TimeTraveller(),
            new TrueSights(),
            new Particles(),
            new SelfTag(),
            new SpeedGraph(),
            new Wings(),
            // Hud
            new ClientSettings(), // Special Module
            new PostProcessing(), // Special Module
            new Arraylist(),
            new BASticker(),
            new BPSCounter(),
            new CPSCounter(),
            new HUD(),
            new Keystrokes(),
            new MusicInfo(),
            new MusicVisualizer(),
            new PotionEffects(),
            new Scoreboard(),
            new SessionInfo(),
            new TargetHud(),
            new TextGui(),
            new CustomText()
    };

    public static int CLIENT_THEME_COLOR = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_2 = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT = new Color(185, 69, 255).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT_2 = new Color(185, 69, 255).hashCode();
    public static Color CLIENT_THEME_COLOR_BRIGHT_COLOR = new Color(185, 69, 255);
    public static Color CLIENT_THEME_COLOR_BRIGHT_COLOR_2 = new Color(185, 69, 255);
    public static boolean isDestructed = false;

}