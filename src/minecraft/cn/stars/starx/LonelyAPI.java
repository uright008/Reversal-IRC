/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx;

import cn.stars.starx.module.impl.hud.HUD;
import cn.stars.starx.util.Transformer;
import cn.stars.starx.util.irc.User;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.lwjgl.opengl.Display;

@Getter
@Setter
@UtilityClass
public class LonelyAPI implements GameInstance {

    public static int backgroundId = 9;
    public static User ircUser = null;
    public static boolean hasJavaFX = true;

    public static boolean isShaderCompatibility = false;
    public static boolean isViaCompatibility = false;

    // 崩溃报告随机语录
    public static final String[] wittyCrashReport = new String[]
            {"玩原神玩的", "粥批差不多得了", "原神?启动!", "哇真的是你啊", "你怎么似了", "加瓦,救一下啊", "Bomb has been planted",
                    "闭嘴!我的父亲在mojang工作,他可以使你的mInEcRaFt崩溃", "纪狗气死我了", "致敬传奇耐崩王MiNeCrAfT", "你的客户端坠机了",
                    "It's been a long day without you my friend", "回来吧牢端", "为了你,我变成狼人模样"};
    // 随机标题
    public static final String[] wittyTitle = new String[]
            {"乌云再厚也遮不住阳光,风雨过后总会有彩虹", "与你的日常,就是奇迹", "虚假的真实是真实吗?", "未来仍有无限可能", "在那灿烂的群星中,总有一颗代表我正与你对视", "王女手握钥匙,方舟恭候多时",
                    "一缕星光会给予我和你无尽的力量", "vanitas vanitatum et omnia vanitas", "情感在于表达,更在于理解", "古希腊掌握PVP的神", "从来如此,便对吗?", "你所珍视的那个人,是否也同样珍视你?", "我有鱼鱼症"};

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

    // 加载客户端设置
    public static void loadAPI() {
        Display.setTitle("Loading LonelyAPI...");
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
                Transformer.isLicenseReviewed = Boolean.parseBoolean(split[1]);
            }
            if (split[0].contains("BetterMainMenu")) {
                Transformer.betterMainMenu = Boolean.parseBoolean(split[1]);
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
        clientBuilder.append("LicenseReviewed_").append(Transformer.isLicenseReviewed).append("\r\n");
        clientBuilder.append("BetterMainMenu_").append(Transformer.betterMainMenu).append("\r\n");
        clientBuilder.append("CustomText_").append(StarX.customText).append("\r\n");

        FileUtil.saveFile("client.txt", true, clientBuilder.toString());
    }
}
