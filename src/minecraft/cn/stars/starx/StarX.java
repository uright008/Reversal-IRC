package cn.stars.starx;

import cn.stars.addons.creativetab.StarXTab;
import cn.stars.addons.optimization.entityculling.EntityCullingMod;
import cn.stars.addons.optimization.util.FastTrig;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.impl.*;
import cn.stars.starx.config.DefaultHandler;
import cn.stars.starx.config.MusicHandler;
import cn.stars.starx.module.*;
import cn.stars.starx.module.impl.hud.*;
import cn.stars.starx.module.impl.render.*;
import cn.stars.starx.module.impl.player.*;
import cn.stars.starx.module.impl.combat.*;
import cn.stars.starx.module.impl.misc.*;
import cn.stars.starx.module.impl.movement.*;
import cn.stars.starx.module.impl.addons.*;
import cn.stars.starx.module.impl.world.*;
import cn.stars.starx.music.MusicManager;
import cn.stars.starx.ui.clickgui.modern.MMTClickGUI;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.ui.hud.Hud;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.misc.VideoUtil;
import cn.stars.starx.util.starx.Branch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.florianmichael.viamcp.ViaMCP;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;
import tech.skidonion.obfuscator.annotations.StringEncryption;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) 2024 Starlight Team, All rights reserved.
 * A Hack-Visual PVP Client, a small dream planted by Stars.
 */
@NativeObfuscation
@StringEncryption
@Getter
public class StarX {
    // Client Info
    public static final String NAME = "StarX";
    public static final String VERSION = "NEXT v0.2.0";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public static final String AUTHOR = "Stars, BzdHyp";
    public static final Branch BRANCH = Branch.PRIVATE;

    // Init
    public static Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static EntityCullingMod entityCullingMod;

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
    public static boolean firstBoot;

    // Core
    public static void start() {
        try {
            StarXLogger.info("Loading client...");

            RainyAPI.loadAPI();

            // ViaMCP init
            if (!RainyAPI.isViaCompatibility) {
                ViaMCP.create();
                ViaMCP.INSTANCE.initAsyncSlider();
            }

            initialize();

            DefaultHandler.loadConfigs();

            postInitialize();

        //    Display.setTitle(NAME + " " + VERSION + " " + Branch.getBranchName(BRANCH) + " | " + RainyAPI.getRandomTitle());
            Display.setTitle(NAME + " " + VERSION + " " + Branch.getBranchName(BRANCH));
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
    public static void showMsg(Object msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§b§l" + NAME + "§r§7] §r" + msg));
        }
    }
    public static void showCustomMsg(Object msg) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((String) msg));
        }
    }

    // run required
    public static void saveAll() {
        DefaultHandler.saveConfig(false);
        RainyAPI.processAPI();
    }


    public static void initialize() {
        try {
            // Minecraft Pre-Initialize
            // Shut the fuck fast render off
            Minecraft.getMinecraft().gameSettings.ofFastRender = false;

            // StarX Initialize
            moduleManager = new ModuleManager();
            moduleManager.registerModules(modules);

            notificationManager = new NotificationManager();

            cmdManager = new CommandManager();
            cmdManager.registerCommands(commands);

            try {
                musicManager = new MusicManager();
                musicManager.initGUI();
            } catch (NoClassDefFoundError e) {
                RainyAPI.hasJavaFX = false;
                StarXLogger.warn("No JavaFX found in the current java version! Music player is disabled.");
            }

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
            FastTrig.init();

            entityCullingMod = new EntityCullingMod();
            entityCullingMod.onInitialize();

            if (RainyAPI.hasJavaFX) MusicHandler.load();

            Hud.initializeModules();

            if (!FileUtil.exists("Background" + File.separator)) {
                FileUtil.createDirectory("Background" + File.separator);
            }

            File tempFile = new File(Minecraft.getMinecraft().mcDataDir, "StarX/Background");
            File videoFile = new File(tempFile, "background.mp4");
            if (!(tempFile.exists() && !videoFile.exists())) {
                tempFile.mkdir();
                try {
                    FileUtil.unpackFile(videoFile, "assets/minecraft/starx/background.mp4");
                } catch (IOException e) {
                    StarXLogger.error("Failed to unpack background file. Are there any Chinese characters in the booting dir?");
                    throw new RuntimeException(e);
                }
            }
            try {
                VideoUtil.init(videoFile);
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
        return true;
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
            new AutoGG(),
            new Dinnerbone(),
            new HealthWarn(),
            new IRC(),
            new SelfTag(),
            new SmallPlayer(),
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
            new Hotbar(),
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
            new SpeedGraph(),
            new Wings(),
            // Hud
            new ClientSettings(), // Special Module
            new PostProcessing(), // Special Module
            new Arraylist(),
            new BASticker(),
            new BPSCounter(),
            new CPSCounter(),
            new CustomText(),
            new HUD(),
            new Keystrokes(),
            new MusicInfo(),
            new MusicVisualizer(),
            new PotionEffects(),
            new PVPStats(),
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
    public static boolean isDestructed = false;

}