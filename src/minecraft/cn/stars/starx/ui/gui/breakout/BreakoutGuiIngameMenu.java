package cn.stars.starx.ui.gui.breakout;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.render.Stencil;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.sql.Time;

public class BreakoutGuiIngameMenu extends GuiScreen implements GuiYesNoCallback {
    ScaledResolution sr;
    int alpha = 0;
    int alpha1 = 0;
    int alpha2 = 0;
    int alpha3 = 0;
    int alpha4 = 0;
    int alpha5 = 0;
    int alpha6 = 0;
    TimeUtil t = new TimeUtil();
    TimeUtil t1 = new TimeUtil();
    TimeUtil t2 = new TimeUtil();
    TimeUtil t3 = new TimeUtil();
    TimeUtil t4 = new TimeUtil();
    TimeUtil t5 = new TimeUtil();
    TimeUtil t6 = new TimeUtil();
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
        if (t.hasReached(4)) {
            alpha += 1;
            t.reset();
        }
        if (alpha > 120) alpha = 120;
        RenderUtil.rect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0, alpha));
        RenderUtil.imageWithAlpha(new ResourceLocation("starx/images/breakout/pause.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0.83f);
        RenderUtil.circle(60, 75, 5, true, new Color(230,230,230, 130));
        RenderUtil.rect(65, 77.5, 320, 0.5, new Color(230,230,230, 130));
        CustomFont.drawStringHuge("Pause", 65, 35, new Color(230,230,230,230).getRGB());

        if (mouseOver(65, 95, 300, 30, mouseX, mouseY)) {
            if (t1.hasReached(1)) {
                alpha1 += 2;
                t1.reset();
            }
            if (alpha1 > 300) alpha1 = 300;
        } else {
            if (t1.hasReached(1)) {
                alpha1 -= 2;
                t1.reset();
            }
            if (alpha1 < 0) alpha1 = 0;
        }
        RenderUtils.drawGradientSidewaysHPlus(65, 95, alpha1, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Continue", 72, 100, new Color(230,230,230,230).getRGB());

        if (mouseOver(65, 130, 300, 30, mouseX, mouseY)) {
            if (t2.hasReached(1)) {
                alpha2 += 2;
                t2.reset();
            }
            if (alpha2 > 300) alpha2 = 300;
        } else {
            if (t2.hasReached(1)) {
                alpha2 -= 2;
                t2.reset();
            }
            if (alpha2 < 0) alpha2 = 0;
        }
        RenderUtils.drawGradientSidewaysHPlus(65, 130, alpha2, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Achievement", 72, 135, new Color(230,230,230,230).getRGB());

        if (mouseOver(65, 165, 300, 30, mouseX, mouseY)) {
            if (t3.hasReached(1)) {
                alpha3 += 2;
                t3.reset();
            }
            if (alpha3 > 300) alpha3 = 300;
        } else {
            if (t3.hasReached(1)) {
                alpha3 -= 2;
                t3.reset();
            }
            if (alpha3 < 0) alpha3 = 0;
        }
        RenderUtils.drawGradientSidewaysHPlus(65, 165, alpha3, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Settings", 72, 170, new Color(230,230,230,230).getRGB());

        if (mouseOver(65, 200, 300, 30, mouseX, mouseY)) {
            if (t4.hasReached(1)) {
                alpha4 += 2;
                t4.reset();
            }
            if (alpha4 > 300) alpha4 = 300;
        } else {
            if (t4.hasReached(1)) {
                alpha4 -= 2;
                t4.reset();
            }
            if (alpha4 < 0) alpha4 = 0;
        }
        RenderUtils.drawGradientSidewaysHPlus(65, 200, alpha4, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Game Profile", 72, 205, new Color(230,230,230,230).getRGB());

        if (canOpenToLan) {
            if (mouseOver(65, 235, 300, 30, mouseX, mouseY)) {
                if (t5.hasReached(1)) {
                    alpha5 += 2;
                    t5.reset();
                }
                if (alpha5 > 300) alpha5 = 300;
            } else {
                if (t5.hasReached(1)) {
                    alpha5 -= 2;
                    t5.reset();
                }
                if (alpha5 < 0) alpha5 = 0;
            }
            RenderUtils.drawGradientSidewaysHPlus(65, 235, alpha5, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        }

        CustomFont.drawStringBig("Open to LAN", 72, 240, canOpenToLan ? new Color(230,230,230,230).getRGB() : new Color(30,30,30, 230).getRGB());

        if (mouseOver(65, 270, 300, 30, mouseX, mouseY)) {
            if (t6.hasReached(1)) {
                alpha6 += 2;
                t6.reset();
            }
            if (alpha6 > 300) alpha6 = 300;
        } else {
            if (t6.hasReached(1)) {
                alpha6 -= 2;
                t6.reset();
            }
            if (alpha6 < 0) alpha6 = 0;
        }
        RenderUtils.drawGradientSidewaysHPlus(65, 270, alpha6, 30, new Color(100, 250, 200, 180).getRGB(), new Color(100, 250, 200, 20).getRGB());
        CustomFont.drawStringBig("Quit", 72, 275, new Color(230,230,230,230).getRGB());
      //  StarX.INSTANCE.showMsg("X:"+ mouseX + ",Y:"+ mouseY);
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
            boolean flag1 = this.mc.func_181540_al();
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);

            if (flag)
            {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            else if (flag1)
            {
                RealmsBridge realmsbridge = new RealmsBridge();
                realmsbridge.switchToRealms(new GuiMainMenu());
            }
            else
            {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @Override
    public void initGui() {
        alpha1 = 0;
        alpha2 = 0;
        alpha3 = 0;
        alpha4 = 0;
        alpha5 = 0;
        alpha6 = 0;
        t2.reset();
        t3.reset();
        t4.reset();
        t5.reset();
        t6.reset();
    }
}
