package cn.stars.starx;

import cn.stars.addons.creativetab.StarXTab;
import cn.stars.addons.optimization.entityculling.EntityCullingMod;
import cn.stars.addons.optimization.util.FastTrig;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.impl.*;
import cn.stars.starx.config.DefaultHandler;
import cn.stars.starx.config.MusicHandler;
import cn.stars.starx.module.ModuleManager;
import cn.stars.starx.music.MusicManager;
import cn.stars.starx.ui.clickgui.modern.MMTClickGUI;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.ui.hud.Hud;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.misc.VideoUtils;
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
    public static final String VERSION = "v3.0.0";
    public static final String MINECRAFT_VERSION = "1.8.9";
    public static final String AUTHOR = "Starlight Team";
    public static final Branch BRANCH = Branch.DEVELOPMENT;

    // Init
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();
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

            LonelyAPI.loadAPI();

            // ViaMCP init
            Minecraft.setStage(4);
            if (!LonelyAPI.isViaCompatibility) {
                ViaMCP.create();
                ViaMCP.INSTANCE.initAsyncSlider();
            }

            initialize();

            Minecraft.setStage(7);
            DefaultHandler.loadConfigs();

            Minecraft.setStage(8);
            postInitialize();

            Display.setTitle(NAME + " " + VERSION + " " + Branch.getBranchName(BRANCH) + " | " + LonelyAPI.getRandomTitle());
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
        LonelyAPI.processAPI();
    }


    public static void initialize() {
        try {
            Minecraft.setStage(5);

            // Minecraft Pre-Initialize
            // Shut the fuck fast render off
            Minecraft.getMinecraft().gameSettings.ofFastRender = false;

            // StarX Initialize
            moduleManager = new ModuleManager();
            moduleManager.registerModules();

            notificationManager = new NotificationManager();

            cmdManager = new CommandManager();
            cmdManager.registerCommands();

            try {
                musicManager = new MusicManager();
                musicManager.initGUI();
            } catch (NoClassDefFoundError e) {
                LonelyAPI.hasJavaFX = false;
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
            FastTrig.init();

            entityCullingMod = new EntityCullingMod();
            entityCullingMod.onInitialize();

            if (LonelyAPI.hasJavaFX) MusicHandler.load();

            Minecraft.setStage(9);
            Hud.initializeModules();

            Minecraft.setStage(10);
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
        return true;
    }

    public static int CLIENT_THEME_COLOR = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_2 = new Color(159, 24, 242).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT = new Color(185, 69, 255).hashCode();
    public static int CLIENT_THEME_COLOR_BRIGHT_2 = new Color(185, 69, 255).hashCode();
    public static boolean isDestructed = false;

}