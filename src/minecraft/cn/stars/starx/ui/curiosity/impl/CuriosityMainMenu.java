package cn.stars.starx.ui.curiosity.impl;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.ui.curiosity.CuriosityTextButton;
import cn.stars.starx.ui.gui.GuiMicrosoftLoginPending;
import cn.stars.starx.ui.gui.mainmenu.MenuButton;
import cn.stars.starx.util.Transformer;
import cn.stars.starx.util.animation.advanced.composed.ColorAnimation;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.starx.Branch;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

@NativeObfuscation
public class CuriosityMainMenu extends GuiScreen implements GameInstance {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Animation updateLogAnimationX = new Animation(Easing.EASE_OUT_EXPO, 500);
    private final Animation updateLogAnimationY = new Animation(Easing.EASE_OUT_EXPO, 500);

    private final Animation loginAnimationX = new Animation(Easing.EASE_OUT_EXPO, 500);
    private final Animation settingsAnimationX = new Animation(Easing.EASE_OUT_EXPO, 500);
    private final Animation backgroundIdAnimationX = new Animation(Easing.EASE_OUT_EXPO, 500);
    private final Animation exitAnimationX = new Animation(Easing.EASE_OUT_EXPO, 500);

    private Animation textAnimation = new Animation(Easing.EASE_OUT_EXPO, 1000);
    private final ColorAnimation colorAnimation = new ColorAnimation(new Color(220,220,220,220), new Color(120,120,120,220), 2000);
    private String title = "";

    private CuriosityTextButton singlePlayerButton, multiPlayerButton, settingsButton, viaVersionButton, exitButton, cbButton, updateLogButton, loginButton;
    private CuriosityTextButton[] buttons;
    private boolean showUpdateLog = false;
    private final ArrayList<String> updateLog = new ArrayList<>();


    @SneakyThrows
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // background
        drawMenuBackground(partialTicks, mouseX, mouseY);

        updatePostProcessing(true, partialTicks);

        for (CuriosityTextButton button : buttons) {
            button.draw(mouseX, mouseY, partialTicks);
        }

        // 动画
        loginAnimationX.run(RenderUtil.isHovered(this.width - 60, this.height - 190, 35, 35, mouseX, mouseY) ? 100 : 0);
        settingsAnimationX.run(RenderUtil.isHovered(this.width - 60, this.height - 145, 35, 35, mouseX, mouseY) ? 100 : 0);
        backgroundIdAnimationX.run(RenderUtil.isHovered(this.width - 60, this.height - 100, 35, 35, mouseX, mouseY) ? 100 : 0);
        exitAnimationX.run(RenderUtil.isHovered(this.width - 60, this.height - 55, 35, 35, mouseX, mouseY) ? 100 : 0);

        updateLogAnimationX.run(showUpdateLog ? 270 : 0);
        updateLogAnimationY.run(showUpdateLog ? 400 : 0);

        RenderUtil.roundedRectangle(this.width - 25 - updateLogAnimationX.getValue(), 10, updateLogAnimationX.getValue(), updateLogAnimationY.getValue(), 4, new Color(30, 30, 30, 200));
        RenderUtil.roundedRectangle(this.width - 60 - loginAnimationX.getValue(), this.height - 190, loginAnimationX.getValue(), 35, 4, new Color(30, 30, 30, 200));
        RenderUtil.roundedRectangle(this.width - 60 - settingsAnimationX.getValue(), this.height - 145, settingsAnimationX.getValue(), 35, 4, new Color(30, 30, 30, 200));
        RenderUtil.roundedRectangle(this.width - 60 - backgroundIdAnimationX.getValue(), this.height - 100, backgroundIdAnimationX.getValue(), 35, 4, new Color(30, 30, 30, 200));
        RenderUtil.roundedRectangle(this.width - 60 - exitAnimationX.getValue(), this.height - 55, exitAnimationX.getValue(), 35, 4, new Color(30, 30, 30, 200));

        NORMAL_BLUR_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(this.width - 60 - loginAnimationX.getValue(), this.height - 190, loginAnimationX.getValue(), 35, 4, Color.BLACK);
            RenderUtil.roundedRectangle(this.width - 25 - updateLogAnimationX.getValue(), 10, updateLogAnimationX.getValue(), updateLogAnimationY.getValue(), 4, Color.BLACK);
            RenderUtil.roundedRectangle(this.width - 60 - settingsAnimationX.getValue(), this.height - 145, settingsAnimationX.getValue(), 35, 4, Color.BLACK);
            RenderUtil.roundedRectangle(this.width - 60 - backgroundIdAnimationX.getValue(), this.height - 100, backgroundIdAnimationX.getValue(), 35, 4, Color.BLACK);
            RenderUtil.roundedRectangle(this.width - 60 - exitAnimationX.getValue(), this.height - 55, exitAnimationX.getValue(), 35, 4, Color.BLACK);
        });

        // 动画
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(this.width - 60 - loginAnimationX.getValue(), this.height - 190, loginAnimationX.getValue(), 35);
        regular32.drawString("微软登录", this.width - 142, this.height - 178, new Color(220, 220, 220, 240).getRGB());

        RenderUtil.scissor(this.width - 60 - settingsAnimationX.getValue(), this.height - 145, settingsAnimationX.getValue(), 35);
        regular32.drawString("游戏设置", this.width - 142, this.height - 133, new Color(220, 220, 220, 240).getRGB());

        RenderUtil.scissor(this.width - 60 - backgroundIdAnimationX.getValue(), this.height - 97, backgroundIdAnimationX.getValue(), 35);
        regular32.drawString("更换背景", this.width - 142, this.height - 91, new Color(220, 220, 220, 240).getRGB());
        regular16.drawString("(当前ID: " + StarX.backgroundId + ")", this.width - 132, this.height - 75, new Color(220, 220, 220, 240).getRGB());

        RenderUtil.scissor(this.width - 60 - exitAnimationX.getValue(), this.height - 55, exitAnimationX.getValue(), 35);
        regular32.drawString("退出游戏", this.width - 142, this.height - 43, new Color(220, 220, 220, 240).getRGB());

        // 更新日志
        //    RenderUtil.rect(4, 41, updateLogAnimationX.getValue(), 0.5, new Color(220, 220, 220, 240));
        RenderUtil.scissor(this.width - 25 - updateLogAnimationX.getValue(), 10, updateLogAnimationX.getValue(), updateLogAnimationY.getValue());
        int y = 55;
        for (String s : updateLog) {
            regular20.drawString(s, this.width - 290, y, new Color(220, 220, 220, 240).getRGB());
            y += 10;
        }
        regular32.drawString("StarX", this.width - 280, 14, new Color(220, 220, 220, 240).getRGB());
        regular32.drawString("Announcements™", this.width - 250, 31, new Color(220, 220, 220, 240).getRGB());
        regular16.drawString(StarX.VERSION + " " + Branch.getBranchName(StarX.BRANCH), this.width - 290, 400, new Color(220, 220, 220, 240).getRGB());

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // 动画
    /*    boolean isHoveringLeft = RenderUtil.isHovered(0, height - 37, 140, 37, mouseX, mouseY);
        boolean isHoveringRight = RenderUtil.isHovered(width - regular.getWidth(copyright) - 1, height - 13, regular.getWidth(copyright), 13, mouseX, mouseY);
        textHoverAnimation.run(isHoveringLeft ? 255 : 155);
        textHoverAnimation2.run(isHoveringRight ? 255 : 155);

        if (viaVersionButton.isHovering) tipString = "跨版本";
        else if (exitButton.isHovering) tipString = "退出游戏";
        else if (cbButton.isHovering) tipString = "更换背景";
        else if (isHoveringLeft) tipString = "版本日志";
        else if (isHoveringRight) tipString = "作者信息";

        fontAnimation.run(viaVersionButton.isHovering || exitButton.isHovering || cbButton.isHovering || isHoveringLeft || isHoveringRight ? 240 : 0);


        Color stringColor = new Color(160, 160, 160, 220);
        regular.drawString(copyright, width - regular.getWidth(copyright) - 1, height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation2.getValue()).getRGB());
        regular.drawString("Minecraft 1.8.9 (StarX/mcp/vanilla)", 2, height - 36, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        regular.drawString("OptiFine_1.8.9_HD_U_M6_pre2", 2, height - 24, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        regular.drawString("当前背景ID: " + StarX.backgroundId, 2, height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB()); */

        // MainMenu
        RenderUtil.rect(0, 0, 230, height, new Color(0, 0, 0, 50));
        RenderUtil.rect(230, 0, 1, height, new Color(220, 220, 220, 240));
        if (Transformer.betterMainMenu) {
            FontManager.getRainbowParty(96).drawCenteredString(LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH), width / 2f, 20, new Color(250,250,250,250).getRGB());
            FontManager.getRainbowParty(48).drawCenteredString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), width / 2f, 72, new Color(250,250,250,250).getRGB());
        }
        RenderUtil.image(new ResourceLocation("starx/images/curiosity.png"), 10, height / 6f - 20, 100, 100, new Color(255,255,255,255));
        FontManager.getRegular(64).drawCenteredString("STARX", 140, height / 6f + 18, new Color(250,250,250,250).getRGB());

        regular18.drawCenteredString("Copyright © 2024 Starlight, All rights reserved.", this.width / 2f, this.height - 20, colorAnimation.getOutput().getRGB());
        if (StarX.isAMDShaderCompatibility) regular20.drawCenteredString("警告: 配置已开启DisableShader选项！你将只能使用ID 9作为背景！", this.width / 2f, this.height - 35, new Color(220, 20, 20, 220).getRGB());

        //    regular.drawCenteredString(tipString, width / 2f, height / 2f + 100,
        //            ColorUtil.withAlpha(new Color(250, 250, 250, 250), (int) fontAnimation.getValue()).getRGB());
        if (colorAnimation.isFinished()) colorAnimation.changeDirection();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        textAnimation.run(1);
        RenderUtil.scissor(this.width / 2f - 1, this.height - 40, regular20.width(title) / 2f * textAnimation.getValue() + 2, 20);
        regular20.drawCenteredString(title, this.width / 2f, this.height - 30, colorAnimation.getOutput().getRGB());
        RenderUtil.scissor(this.width / 2f - regular20.width(title) / 2f * textAnimation.getValue() + 1, this.height - 40, regular20.width(title) / 2f * textAnimation.getValue(), 20);
        regular20.drawCenteredString(title, this.width / 2f, this.height - 30, colorAnimation.getOutput().getRGB());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        regular20.drawString(">", this.width / 2f + regular20.width(title) / 2f * textAnimation.getValue() + 10, this.height - 30, colorAnimation.getOutput().getRGB());
        regular20.drawString("<", this.width / 2f - regular20.width(title) / 2f * textAnimation.getValue() - 10, this.height - 30, colorAnimation.getOutput().getRGB());

        updatePostProcessing(false, partialTicks);
    }

    @Override
    public void initGui() {
        title = StarX.wittyTitle[RandomUtil.INSTANCE.nextInt(0, StarX.wittyTitle.length)];
        textAnimation = new Animation(Easing.EASE_OUT_EXPO, 1000);

        updateLog.clear();
        updateLog.add("[-] 删除Minecraft/StarX/Modern/Simple以外所有设计");
        updateLog.add("[-] 删除旧的TTFFontRenderer");
        updateLog.add("[-] 删除TargetHud一些模式");
        updateLog.add("[+] 所有设计现在均支持三种颜色模式");
        updateLog.add("[/] 大幅优化启动速度");


        // 定义按钮
        this.singlePlayerButton = new CuriosityTextButton(45, this.height / 6f + 80, 120, 35, () -> mc.displayGuiScreen(new GuiSelectWorld(this)),
                "单人游戏", "a", true, 12, 40, 11);
        this.multiPlayerButton = new CuriosityTextButton(45, this.height / 6f + 125, 120, 35, () -> mc.displayGuiScreen(new GuiMultiplayer(this)),
                "多人游戏", "b", true, 9, 40, 11);
        this.viaVersionButton = new CuriosityTextButton(45, this.height / 6f + 170, 120, 35, () -> mc.displayGuiScreen(new CuriosityViaMenu(this)),
                "跨版本", "d", true, 10, 44, 11);

        this.loginButton = new CuriosityTextButton(this.width - 60, this.height - 190, 35, 35, () -> mc.displayGuiScreen(new GuiMicrosoftLoginPending(this)),
                "", "9", true, 9, 50, 11);
        this.settingsButton = new CuriosityTextButton(this.width - 60, this.height - 145, 35, 35, () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)),
                "", "e", true, 11, 50, 11);
        this.cbButton = new CuriosityTextButton(this.width - 60, this.height - 100, 35, 35, () -> changeMenuBackground(false),
                "", "c", true, 9, 0, 11);
        this.exitButton = new CuriosityTextButton(this.width - 60, this.height - 55, 35, 35, mc::shutdown,
                "", "g", true, 9, 0, 11);

        this.updateLogButton = new CuriosityTextButton(this.width - 60, 10, 35, 35, () -> showUpdateLog = !showUpdateLog,
                "", "f", true, 9, 0, 11);

        // 简化MouseClicked方法
        this.buttons = new CuriosityTextButton[] {this.singlePlayerButton, this.multiPlayerButton, this.settingsButton, this.viaVersionButton, this.exitButton, this.cbButton, this.updateLogButton, this.loginButton } ;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.buttons == null) return;

        // 执行按钮点击
        if (mouseButton == 0) {
            for (CuriosityTextButton menuButton : this.buttons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }

        if (mouseButton == 1) {
            for (MenuButton menuButton : this.buttons) {
                if (menuButton == cbButton && RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    changeMenuBackground(true);
                    break;
                }
            }
        }
    }
}
