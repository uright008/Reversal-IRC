package cn.stars.starx.ui.gui;

import cn.stars.starx.GameInstance;
import cn.stars.starx.RainyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.font.MFont;
import cn.stars.starx.ui.gui.mainmenu.MenuButton;
import cn.stars.starx.ui.gui.mainmenu.MenuTextButton;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.misc.WebUtil;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.shader.RiseShaders;
import cn.stars.starx.util.shader.base.ShaderRenderType;
import cn.stars.starx.util.shader.impl.BackgroundShader;
import cn.stars.starx.util.starx.Branch;
import de.florianmichael.viamcp.gui.GuiProtocolSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;
import java.io.IOException;

@NativeObfuscation
public class GuiMainMenu extends GuiScreen implements GameInstance {
    ScaledResolution sr;
    Minecraft mc = Minecraft.getMinecraft();
    MFont psr = FontManager.getRegular(20);
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600);
    private Animation textHoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250);
    private Animation textHoverAnimation2 = new Animation(Easing.EASE_OUT_SINE, 250);

    private MenuTextButton singlePlayerButton, multiPlayerButton, settingsButton, viaVersionButton, exitButton, cbButton, authorButton, msLoginButton;
    private MenuButton[] menuButtons;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.singlePlayerButton == null || this.multiPlayerButton == null) {
            return;
        }
        sr = new ScaledResolution(mc);

        // 背景
        drawDefaultBackground();

        //    NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.rectangle(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK));

        // blur
        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);

        // bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

        GameInstance.clearRunnables();

        // 旁边按钮
        this.cbButton.draw(mouseX, mouseY, partialTicks);
        this.authorButton.draw(mouseX, mouseY, partialTicks);
        this.msLoginButton.draw(mouseX, mouseY, partialTicks);

        // Logo
        final double destination = height / 2f - 180;
        animation.run(destination);
        RenderUtil.image(new ResourceLocation("starx/images/starx.png"), width / 2f - 110, (float) animation.getValue() - 50, 280, 190);

        // 测试版 watermark
        if (StarX.BRANCH == Branch.SNAPSHOT) {
            RenderUtil.image(new ResourceLocation("starx/666.png"), width - 110, 0, 100, 100);
            psr.drawString("快照版本.仅供测试目的.", width - 110, 110, Color.WHITE.getRGB());
        }


        // 字
        String s1 = "© Starlight 2024, 保留所有权利.";
        // 动画
        textHoverAnimation.run(RenderUtil.isHovered(0, height - 37, 140, 37, mouseX, mouseY) ? 255 : 155);
        textHoverAnimation2.run(RenderUtil.isHovered(width - psr.getWidth(s1) - 1, height - 13, psr.getWidth(s1), 13, mouseX, mouseY) ? 255 : 155);

        Color stringColor = new Color(50, 150, 250, 155);
        psr.drawString(s1, width - psr.getWidth(s1) - 1, height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation2.getValue()).getRGB());
        psr.drawString("Minecraft 1.8.9 (StarX/mcp/vanilla)", 2, height - 36, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        psr.drawString("OptiFine_1.8.9_HD_U_M6_pre2", 2, height - 24, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());
        psr.drawString("当前背景ID: " + RainyAPI.backgroundId, 2, height - 12, ColorUtil.withAlpha(stringColor, (int) textHoverAnimation.getValue()).getRGB());


        // 主要按钮
        this.singlePlayerButton.draw(mouseX, mouseY, partialTicks);
        this.multiPlayerButton.draw(mouseX, mouseY, partialTicks);
        this.settingsButton.draw(mouseX, mouseY, partialTicks);
        this.viaVersionButton.draw(mouseX, mouseY, partialTicks);
        this.exitButton.draw(mouseX, mouseY, partialTicks);


        if (RainyAPI.isShaderCompatibility && RainyAPI.backgroundId <= 8) {
            psr.drawCenteredString("警告: 检测到Shader兼容性错误,背景已强制关闭. (AMD CPU?)", width / 2f, height / 2f + 100, new Color(250,50,50, 250).getRGB());
        }

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.menuButtons == null) return;

        // 执行runnable
        if (mouseButton == 0) {
            for (MenuButton menuButton : this.menuButtons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
        if (mouseButton == 1) {
            for (MenuButton menuButton : this.menuButtons) {
                if (menuButton == cbButton && RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    changeMenuBackground(true);
                    break;
                }
            }
        }
    }

    @Override
    public void initGui() {
        GameInstance.clearRunnables();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600);
        this.textHoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250);
        this.textHoverAnimation2 = new Animation(Easing.EASE_OUT_SINE, 250);
        RiseShaders.MAIN_MENU_SHADER.setActive(true);

        // 重新定义按钮
        this.singlePlayerButton = new MenuTextButton(centerX - 200, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiSelectWorld(this)), "单人游戏", "K");
        this.multiPlayerButton = new MenuTextButton(centerX - 120, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiMultiplayer(this)), "多人游戏", "L");
        this.settingsButton = new MenuTextButton(centerX - 40, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)), "设置", "N");
        this.viaVersionButton = new MenuTextButton(centerX + 40, centerY - 25, 75, 75, () -> mc.displayGuiScreen(new GuiProtocolSelector(this)), "跨版本", "x");
        this.exitButton = new MenuTextButton(centerX + 120, centerY - 25, 75, 75, () -> mc.shutdown(), "退出游戏", "O");
        this.cbButton = new MenuTextButton(4, 4, 85, 25, () -> changeMenuBackground(false), "更换背景", "q", true, 10, 8);
        this.msLoginButton = new MenuTextButton(4, 34, 80, 25, () -> mc.displayGuiScreen(new GuiMicrosoftLoginPending(this)), "微软登录", "M", true, 6, 8);
        this.authorButton = new MenuTextButton(4, 64, 80, 25, () -> WebUtil.openWebPage("https://space.bilibili.com/670866766"), "作者B站", "e", true, 6, 8);

        // 简化MouseClicked方法
        this.menuButtons = new MenuButton[]{this.singlePlayerButton, this.multiPlayerButton, this.settingsButton, this.viaVersionButton, this.exitButton
                ,this.cbButton, this.authorButton, this.msLoginButton};
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        GameInstance.clearRunnables();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        RiseShaders.MAIN_MENU_SHADER.setActive(false);
        super.onGuiClosed();
    }
}
