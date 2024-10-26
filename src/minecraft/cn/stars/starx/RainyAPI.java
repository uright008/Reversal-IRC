/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx;

import cn.stars.starx.module.impl.hud.HUD;
import cn.stars.starx.ui.curiosity.impl.CuriosityMainMenu;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.irc.User;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.misc.VideoUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
@UtilityClass
@SuppressWarnings("all")
public class RainyAPI {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static long window;
    
    public static User ircUser = null;
    public static boolean hasJavaFX = true;

    public static int backgroundId = 9;
    public static boolean isShaderCompatibility = false;
    public static boolean isViaCompatibility = false;
    public static boolean isLicenseReviewed = false;
    public static boolean mainMenuDate = true;
    public static boolean guiSnow = false;

    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // 崩溃报告随机语录
    public static final String[] wittyCrashReport = new String[]
            {"玩原神玩的", "粥批差不多得了", "原神?启动!", "哇真的是你啊", "你怎么似了", "加瓦,救一下啊", "Bomb has been planted",
                    "闭嘴!我的父亲在mojang工作,他可以使你的mInEcRaFt崩溃", "纪狗气死我了", "致敬传奇耐崩王MiNeCrAfT", "你的客户端坠机了",
                    "It's been a long day without you my friend", "回来吧牢端", "为了你,我变成狼人模样"};
    // 随机标题
    public static final String[] wittyTitle = new String[]
            { "我有鱼鱼症", "我们因缘分而相遇，因共同而相聚", "人生有欢喜也有悲剧,一切已是命中注定", "在那灿烂的群星中,总有一颗代表我正与你对视"
            };

    public static String getRandomTitle() {
        return wittyTitle[RandomUtil.INSTANCE.nextInt(0, wittyTitle.length)];
    }

    public static boolean canDrawHUD() {
        if (ModuleInstance.getModule(HUD.class).isEnabled()) {
            if (ModuleInstance.getBool("HUD", "Display when debugging").isEnabled()) {
                return true;
            } else return !mc.gameSettings.showDebugInfo;
        }
        return false;
    }

    /**
     * LWJGL3: 初始化GLFW
     * 获取窗口GL Context, 允许使用GLFW操作
     */
    public static void setupGLFW() {
        window = Display.getWindow();
        GLFW.glfwMakeContextCurrent(window);
    }

    /**
     * 检测窗口拖入文件
     * 在主菜单接受MP4文件并替换背景
     */
    public static void setupDrag() {
        GLFW.glfwSetDropCallback(window, (window, count, names) -> {
            String filePath = GLFWDropCallback.getName(names, 0);
            if (mc.currentScreen instanceof CuriosityMainMenu) {
                if (count == 1) {
                    File droppedFile = new File(filePath);

                    // 检测后缀名
                    if (droppedFile.exists() && droppedFile.getName().toLowerCase().endsWith(".mp4")) {
                        StarXLogger.info("Dragged file successfully detected: " + droppedFile.getAbsolutePath());

                        try {
                            // 停止加载
                            VideoUtil.stop();
                            // 将获取的文件替换现有的文件
                            File tempFile = new File(Minecraft.getMinecraft().mcDataDir, "StarX/Background");
                            File videoFile = new File(tempFile, "background.mp4");
                            Files.copy(droppedFile.toPath(), videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            // 重新加载
                            VideoUtil.init(videoFile);
                            StarX.notificationManager.registerNotification("成功更换视频背景!", "主菜单", 2000L, NotificationType.SUCCESS);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to load new background file.");
                        }
                    } else {
                        StarX.notificationManager.registerNotification("不支持的文件类型!", "主菜单", 2000L, NotificationType.ERROR);
                        StarXLogger.error("Dragged file read failed.");
                    }
                }
            }
        });
    }

    /**
     * 加载客户端设置
     */
    public static void loadAPI() {
        StarXLogger.info("Loading RainyAPI...");
        final String client = FileUtil.loadFile("client.txt");

        // re-save if not available on start.
        if (client == null || !client.contains("DisableShader") || !client.contains("DisableViaMCP") || !client.contains("LicenseReviewed")) {
            processAPI();
            return;
        }

        final String[] clientLines = client.split("\r\n");

        for (final String line : clientLines) {
            if (line == null) return;

            final String[] split = line.split("_");

            if (split[0].contains("DisableShader")) {
                isShaderCompatibility = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("DisableViaMCP")) {
                isViaCompatibility = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("LicenseReviewed")) {
                isLicenseReviewed = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("MainMenuDate")) {
                mainMenuDate = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("GuiSnow")) {
                guiSnow = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("CustomText")) {
                StarX.customText = split[1];
            }
        }
    }

    // 保存客户端设置
    public static void processAPI() {
        final StringBuilder clientBuilder = new StringBuilder();
        clientBuilder.append("DisableShader_").append(isShaderCompatibility).append("\r\n");
        clientBuilder.append("DisableViaMCP_").append(isViaCompatibility).append("\r\n");
        clientBuilder.append("LicenseReviewed_").append(isLicenseReviewed).append("\r\n");
        clientBuilder.append("MainMenuDate_").append(mainMenuDate).append("\r\n");
        clientBuilder.append("GuiSnow_").append(guiSnow).append("\r\n");
        clientBuilder.append("CustomText_").append(StarX.customText).append("\r\n");

        FileUtil.saveFile("client.txt", true, clientBuilder.toString());
    }
}
