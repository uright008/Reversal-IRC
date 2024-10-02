package cn.stars.starx.ui.gui.breakout;

import cn.stars.starx.font.CustomFont;
import cn.stars.starx.ui.gui.GuiMainMenu;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@NativeObfuscation
public class BreakoutGuiPauseMenu extends GuiScreen implements GuiYesNoCallback {
    ScaledResolution sr;
    int alpha = 0;
    float imageAlpha = 0f;
    int progress1 = 0;
    int progress2 = 0;
    int progress3 = 0;
    int progress4 = 0;
    int progress5 = 0;
    int progress6 = 0;
    TimeUtil timerBackground = new TimeUtil();
    TimeUtil timerButton1 = new TimeUtil();
    TimeUtil timerButton2 = new TimeUtil();
    TimeUtil timerButton3 = new TimeUtil();
    TimeUtil timerButton4 = new TimeUtil();
    TimeUtil timerButton5 = new TimeUtil();
    TimeUtil timerButton6 = new TimeUtil();
    boolean canOpenToLan = false;

    @Override
    public void updateScreen() {
        sr = new ScaledResolution(mc);
        super.updateScreen();
    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);
        canOpenToLan = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
        if (timerBackground.hasReached(4)) {
            imageAlpha += 0.01f;
            alpha += 1;
            timerBackground.reset();
        }
        if (imageAlpha > 0.84f) imageAlpha = 0.84f;
        if (alpha > 150) alpha = 150;
        RenderUtil.rect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0, alpha));
        RenderUtil.imageWithAlpha(new ResourceLocation("starx/images/breakout/pause.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), imageAlpha);
        RenderUtil.circle(60, 75, 5, true, new Color(230,230,230, 130));
        RenderUtil.rect(65, 77.5, 320, 0.5, new Color(230,230,230, 130));
        CustomFont.drawStringHuge("Pause", 65, 35, new Color(230,230,230,70 + alpha).getRGB());

        if (mouseOver(65, 95, 300, 30, mouseX, mouseY)) {
            if (timerButton1.hasReached(1)) {
                progress1 += 2;
                timerButton1.reset();
            }
            if (progress1 > 300) progress1 = 300;
        } else {
            if (timerButton1.hasReached(1)) {
                progress1 -= 2;
                timerButton1.reset();
            }
            if (progress1 < 0) progress1 = 0;
        }
        drawGradientSidewaysHPlus(65, 95, progress1, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Continue", 72, 100, new Color(230,230,230,70 + alpha).getRGB());

        if (mouseOver(65, 130, 300, 30, mouseX, mouseY)) {
            if (timerButton2.hasReached(1)) {
                progress2 += 2;
                timerButton2.reset();
            }
            if (progress2 > 300) progress2 = 300;
        } else {
            if (timerButton2.hasReached(1)) {
                progress2 -= 2;
                timerButton2.reset();
            }
            if (progress2 < 0) progress2 = 0;
        }
        drawGradientSidewaysHPlus(65, 130, progress2, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Achievement", 72, 135, new Color(230,230,230,70 + alpha).getRGB());

        if (mouseOver(65, 165, 300, 30, mouseX, mouseY)) {
            if (timerButton3.hasReached(1)) {
                progress3 += 2;
                timerButton3.reset();
            }
            if (progress3 > 300) progress3 = 300;
        } else {
            if (timerButton3.hasReached(1)) {
                progress3 -= 2;
                timerButton3.reset();
            }
            if (progress3 < 0) progress3 = 0;
        }
        drawGradientSidewaysHPlus(65, 165, progress3, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Settings", 72, 170, new Color(230,230,230,70 + alpha).getRGB());

        if (mouseOver(65, 200, 300, 30, mouseX, mouseY)) {
            if (timerButton4.hasReached(1)) {
                progress4 += 2;
                timerButton4.reset();
            }
            if (progress4 > 300) progress4 = 300;
        } else {
            if (timerButton4.hasReached(1)) {
                progress4 -= 2;
                timerButton4.reset();
            }
            if (progress4 < 0) progress4 = 0;
        }
        drawGradientSidewaysHPlus(65, 200, progress4, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Game Profile", 72, 205, new Color(230,230,230,70 + alpha).getRGB());

        if (canOpenToLan) {
            if (mouseOver(65, 235, 300, 30, mouseX, mouseY)) {
                if (timerButton5.hasReached(1)) {
                    progress5 += 2;
                    timerButton5.reset();
                }
                if (progress5 > 300) progress5 = 300;
            } else {
                if (timerButton5.hasReached(1)) {
                    progress5 -= 2;
                    timerButton5.reset();
                }
                if (progress5 < 0) progress5 = 0;
            }
            drawGradientSidewaysHPlus(65, 235, progress5, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        }

        CustomFont.drawStringBig("Open to LAN", 72, 240, canOpenToLan ? new Color(230,230,230,70 + alpha).getRGB() : new Color(30,30,30, 70 + alpha).getRGB());

        if (mouseOver(65, 270, 300, 30, mouseX, mouseY)) {
            if (timerButton6.hasReached(1)) {
                progress6 += 2;
                timerButton6.reset();
            }
            if (progress6 > 300) progress6 = 300;
        } else {
            if (timerButton6.hasReached(1)) {
                progress6 -= 2;
                timerButton6.reset();
            }
            if (progress6 < 0) progress6 = 0;
        }
        drawGradientSidewaysHPlus(65, 270, progress6, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Quit", 72, 275, new Color(230,230,230,70 + alpha).getRGB());
      //  StarX.showMsg("X:"+ mouseX + ",Y:"+ mouseY);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (mouseOver(65, 95, 300, 30, mouseX, mouseY)) {
            mc.getSoundHandler().playButtonPress();
            mc.displayGuiScreen(null);
        }
        if (mouseOver(65, 130, 300, 30, mouseX, mouseY)) {
            mc.getSoundHandler().playButtonPress();
            mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }
        if (mouseOver(65, 165, 300, 30, mouseX, mouseY)) {
            mc.getSoundHandler().playButtonPress();
            mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (mouseOver(65, 200, 300, 30, mouseX, mouseY)) {
            mc.getSoundHandler().playButtonPress();
            mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
        if (mouseOver(65, 235, 300, 30, mouseX, mouseY) && canOpenToLan) {
            mc.getSoundHandler().playButtonPress();
            mc.displayGuiScreen(new GuiShareToLan(this));
        }
        if (mouseOver(65, 270, 300, 30, mouseX, mouseY)) {
            mc.getSoundHandler().playButtonPress();
            boolean flag = this.mc.isIntegratedServerRunning();
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);

            if (flag)
            {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            else
            {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @Override
    public void initGui() {
        progress1 = 0;
        progress2 = 0;
        progress3 = 0;
        progress4 = 0;
        progress5 = 0;
        progress6 = 0;
        timerButton2.reset();
        timerButton3.reset();
        timerButton4.reset();
        timerButton5.reset();
        timerButton6.reset();
    }


    public static void drawGradientSidewaysHPlus(double left, double top, double right, double bottom, int col1, int col2) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);

        RenderUtils.quickDrawGradientSidewaysH(left, top,left + right,top+ bottom, col1, col2);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glShadeModel(GL_FLAT);
    }
}
