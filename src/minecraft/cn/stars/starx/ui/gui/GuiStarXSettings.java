/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.ui.gui;

import cn.stars.starx.GameInstance;
import cn.stars.starx.LonelyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.config.DefaultHandler;
import cn.stars.starx.ui.curiosity.CuriosityTextButton;
import cn.stars.starx.util.Transformer;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RoundedUtil;
import cn.stars.starx.util.render.UIUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

import static cn.stars.starx.GameInstance.*;

public class GuiStarXSettings extends GuiScreen {
    public GuiScreen parent;
    private CuriosityTextButton exitButton, shaderButton, viaButton, betterMainMenuButton;
    private CuriosityTextButton[] buttons;

    public GuiStarXSettings(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        updatePostProcessing(true, partialTicks);

        for (CuriosityTextButton button : buttons) {
            button.draw(mouseX, mouseY, partialTicks);
        }

        RoundedUtil.drawRound(width / 2f - 500, 10, 1000, height - 15, 4, new Color(30, 30, 30, 160));
        RenderUtil.rect(width / 2f - 500, 30, 1000, 0.5, new Color(220, 220, 220, 240));
        NORMAL_BLUR_RUNNABLES.add(() -> RoundedUtil.drawRound(width / 2f - 500, 10, 1000, height - 15, 4, Color.BLACK));
        regular24Bold.drawCenteredString("StarX 设置 (按钮显示当前状态)", width / 2f, 16, new Color(220, 220, 220, 240).getRGB());

        // Shader
        regular20Bold.drawString("Background Shader", width / 2f - 490, 40, new Color(220, 220, 220, 240).getRGB());
        regular16.drawString("开启这个选项后，你将可以在主菜单使用Shader背景。\n部分电脑不支持并会导致崩溃，如果崩溃请关闭。", width / 2f - 490, 55, new Color(220, 220, 220, 240).getRGB());

        // Via
        regular20Bold.drawString("Via Version", width / 2f - 490, 110, new Color(220, 220, 220, 240).getRGB());
        regular16.drawString("开启这个选项后，将允许客户端进行跨版本。\n如果你的客户端偶现无法加载，可以尝试关闭。", width / 2f - 490, 125, new Color(220, 220, 220, 240).getRGB());

        // BetterMainMenu
        regular20Bold.drawString("Better Main Menu", width / 2f - 490, 180, new Color(220, 220, 220, 240).getRGB());
        regular16.drawString("使你的主菜单变得更美丽。\n最上方增加日期显示，其他待更新。", width / 2f - 490, 195, new Color(220, 220, 220, 240).getRGB());

        updatePostProcessing(false, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        this.exitButton = new CuriosityTextButton(width / 2f - 60, height - 60, 120, 35, () -> mc.displayGuiScreen(parent),
                "返回主菜单", "g", true, 12, 38, 11);

        createButton();

        buttons = new CuriosityTextButton[]{exitButton, shaderButton, viaButton, betterMainMenuButton};
    }

    private void switchOption(Runnable runnable) {
        runnable.run();
        createButton();
        LonelyAPI.processAPI();
        buttons = new CuriosityTextButton[]{exitButton, shaderButton, viaButton, betterMainMenuButton};
    }

    private void createButton() {
        if (!LonelyAPI.isShaderCompatibility) {
            this.shaderButton = new CuriosityTextButton(width / 2f - 490, 75, 60, 25, () -> switchOption(() -> LonelyAPI.isShaderCompatibility = !LonelyAPI.isShaderCompatibility),
                    "开", "9", true, 10, 34, 7);
        } else {
            this.shaderButton = new CuriosityTextButton(width / 2f - 490, 75, 60, 25, () -> switchOption(() -> LonelyAPI.isShaderCompatibility = !LonelyAPI.isShaderCompatibility),
                    "关", "0", true, 10, 34, 7);
        }
        if (!LonelyAPI.isViaCompatibility) {
            this.viaButton = new CuriosityTextButton(width / 2f - 490, 145, 60, 25, () -> switchOption(() -> LonelyAPI.isViaCompatibility = !LonelyAPI.isViaCompatibility),
                    "开", "9", true, 10, 34, 7);
        } else {
            this.viaButton = new CuriosityTextButton(width / 2f - 490, 145, 60, 25, () -> switchOption(() -> LonelyAPI.isViaCompatibility = !LonelyAPI.isViaCompatibility),
                    "关", "0", true, 10, 34, 7);
        }
        if (Transformer.betterMainMenu) {
            this.betterMainMenuButton = new CuriosityTextButton(width / 2f - 490, 215, 60, 25, () -> switchOption(() -> Transformer.betterMainMenu = !Transformer.betterMainMenu),
                    "开", "9", true, 10, 34, 7);
        } else {
            this.betterMainMenuButton = new CuriosityTextButton(width / 2f - 490, 215, 60, 25, () -> switchOption(() -> Transformer.betterMainMenu = !Transformer.betterMainMenu),
                    "关", "0", true, 10, 34, 7);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        UIUtil.onButtonClick(buttons, mouseX, mouseY, mouseButton);
    }
}
