package net.minecraft.client.gui;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.ui.theme.GuiTheme;
import cn.stars.starx.ui.theme.Theme;
import cn.stars.starx.util.animation.normal.Animation;
import cn.stars.starx.util.animation.normal.Direction;
import cn.stars.starx.util.animation.normal.impl.EaseBackIn;
import cn.stars.starx.util.render.GlUtils;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.render.UIUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.CustomPanorama;
import net.minecraft.src.CustomPanoramaProperties;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import viamcp.gui.GuiProtocolSelector;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    //Timer used to rotate the panorama, increases every tick.
    public static int panoramaTimer = 1500;

    //Path to images
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("starx/images/panorama_0.png"), new ResourceLocation("starx/images/panorama_1.png"), new ResourceLocation("starx/images/panorama_2.png"), new ResourceLocation("starx/images/panorama_3.png"), new ResourceLocation("starx/images/panorama_4.png"), new ResourceLocation("starx/images/panorama_5.png")};

    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");

    //Positions
    private ScaledResolution sr;

    private float x;
    private float y;

    private int cocks;
    private static boolean rolled;

    private float screenWidth;
    private float screenHeight;

    private float buttonWidth = 50;
    private float buttonHeight = 20;
    private float gap = 4;
    public static float smoothedX, smoothedY;
    public static float xOffSet;
    public static float yOffSet;

    private boolean easterEgg;

    public float pitch;

    private float themeX;
    private float themeY;
    private float themeWidth;
    private float themeHeight;

    private Animation introAnimation;


    //Called from the main game loop to update the screen.
    public void updateScreen() {
        ++panoramaTimer;
    }

    public void initGui() {
        panoramaTimer = 150;
        introAnimation = new EaseBackIn(400, 1, 2);
        easterEgg = Math.random() > 0.99;
    }

    @Override
    public void onGuiClosed() {
        mc.timer.timerSpeed = 1;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(mc);

        if (mc.mouseHelper != null) mc.mouseHelper.mouseGrab(false);

        mc.timer.timerSpeed = 3f;

        //Draws background
        //this.renderSkybox(mouseX, mouseY, partialTicks);
        if (StarX.INSTANCE.getGuiTheme() == null)
            StarX.INSTANCE.guiTheme = new GuiTheme();

        RenderUtil.color(StarX.INSTANCE.getGuiTheme().getThemeColor());
        mc.getTextureManager().bindTexture(new ResourceLocation("starx/images/blue.png"));

        final float scale = 1.66f;
        final float amount = height;

        if (panoramaTimer % 100 == 0) {
            xOffSet = (float) (Math.random() - 0.5f) * amount;
            yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        smoothedX = (smoothedX * 250 + xOffSet) / 259;
        smoothedY = (smoothedY * 250 + yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + smoothedX - 150, height / scale + smoothedY - 100, width, height, width * scale, height * scale);

        GlUtils.startScale(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, (float) introAnimation.getValue());

        screenWidth = fontRenderer.getWidth(StarX.NAME);
        screenHeight = fontRenderer.getHeight(StarX.NAME);

        UIUtil.logoPosition = /*MathUtil.lerp(UIUtil.logoPosition, */sr.getScaledHeight() / 2.0F - (screenHeight / 2.0F) - 6/*, 0.2f)*/;

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        // Box
        //RenderUtil.roundedRect(x - 10, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 125, 145, 10, new Color(0, 0, 0, 35));

        fontRenderer.drawString(StarX.NAME, x, UIUtil.logoPosition, new Color(255, 255, 255, 150).getRGB());

        buttonWidth = 50;
        buttonHeight = 20;
        gap = 4;

        final ArrayList<String> changes = new ArrayList();

        changes.add("");
        changes.add("Version " + StarX.VERSION);
        changes.add("[+] TNTTimer");
        changes.add("[+] Better ScreenshotViewer");
        changes.add("[+] Better GuiMainMenu");


        if (sr.getScaledWidth() > 600 && sr.getScaledHeight() > 300) {
            CustomFont.drawString("Changelog:", 5, 5, new Color(255, 255, 255, 220).hashCode());

            for (int i = 0; i < changes.size(); i++) {
                CustomFont.drawString(changes.get(i), 5, 16 + i * 12, new Color(255, 255, 255, 220).hashCode());
            }
        }

        //Close
        //CustomFont.drawString("X", x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, new Color(255, 255, 255, 200).hashCode());

        //Singleplayer
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Single", x + buttonWidth - 28, y + fontRenderer.getHeight() + 1 + 6, new Color(255, 255, 255, 240).hashCode());

        //Multiplayer
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Multi", x + buttonWidth * 2 + gap - 27, y + fontRenderer.getHeight() + 6 + 1, new Color(255, 255, 255, 240).hashCode());

        //Altmanager
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("idk", x + buttonWidth * 2 + gap - 19, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Version
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("idk", x + gap + 23, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Settings
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Settings", x + buttonWidth - 35, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        //Proxy
        RenderUtil.roundedRect(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("ViaVersion", x + buttonWidth * 2 + gap - 45, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        //Exit
        RenderUtil.roundedRect(sr.getScaledWidth() - 50.0,5.0, buttonWidth, buttonHeight, 10, new Color(255,255,255,35));
        CustomFont.drawString("Exit", sr.getScaledWidth() - 30, 10.0, new Color(255, 255, 255, 240).hashCode());
        /*
        RenderUtil.roundedRect(4, 4, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString(mc.riseMusicTicker.shouldKeepPlaying ? "Stop music" : "Start Music", buttonWidth - CustomFont.getWidth("Stop music") - 2.5f + 4, 8 + 4, new Color(255, 255, 255, 240).hashCode());

        if (mc.riseMusicTicker.shouldKeepPlaying) {
            final String currentlyPlaying = "Currently playing: The Final Flash of Existence (SCP: SL Main Theme) by Jacek 'Fragik' Rogal";
            CustomFont.drawString(currentlyPlaying, buttonWidth + 8, 4, new Color(255, 255, 255, 240).hashCode());
        }*/

        //Quit
        /*
        RenderUtil.roundedRect(sr.getScaledWidth() - 15, 6, 10, 10, 5, new Color(255, 255, 255, 110));
        CustomFont.drawString("x", sr.getScaledWidth() - 12.5, 6, -1);
        */

        //Note
        final String message = "Made with <3 by Stars";
        final String message2 = "Notice: THIS IS NOT A HACK CLIENT!";

        if (sr.getScaledHeight() > 300) {
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
            CustomFont.drawString(message2, sr.getScaledWidth() / 2 - CustomFont.getWidth(message2) / 2, sr.getScaledHeight() - 12.5, new Color(255, 0,0, 180).hashCode());

            //Theme selector
            themeX = 10;
            themeY = sr.getScaledHeight() - 61;
            themeWidth = 1920f / 22;
            themeHeight = 1080f / 22;

            float offset = 0;
            boolean mouseOverAnyThemes = false;
            for (final Theme theme : Theme.values()) {
                final boolean mouseOver = mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY);

                if (!mouseOverAnyThemes && mouseOver) mouseOverAnyThemes = true;

                for (int i = 1; i <= (mouseOver ? 7 : 6); i++)
                    RenderUtil.roundedRect(themeX - i + offset, themeY - i, themeWidth + i * 2, themeHeight + i * 2, 9, new Color(0, 0, 0, 6));

                final Color themeColor = StarX.INSTANCE.getGuiTheme().getThemeColor(theme);

                RenderUtil.color(themeColor);

                int opacity = 100;

                if (theme == StarX.INSTANCE.getGuiTheme().getCurrentTheme())
                    opacity = 200;
                else if (mouseOver)
                    opacity = 150;

                if (theme.opacityInMainMenu < opacity) opacity = (int) theme.opacityInMainMenu;

                RenderUtil.color(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), opacity));

                if (mouseOver) {
                    RenderUtil.image(new ResourceLocation("starx/images/blue.png"), themeX + offset - 1, themeY - 1, themeWidth + 2, themeHeight + 2);
                    theme.nameOpacityInMainMenu += 20;
                } else {
                    RenderUtil.image(new ResourceLocation("starx/images/blue.png"), themeX + offset, themeY, themeWidth, themeHeight);
                    theme.nameOpacityInMainMenu -= 20;
                }

             //   CustomFont.drawStringBig("News:", 10, 110, Color.WHITE.getRGB());
                mc.fontRendererObj.drawString("裱起来让我笑一辈子^^", 10, 110, Color.WHITE.getRGB());
                RenderUtils.drawImage(new ResourceLocation("starx/FwNoName.png"), 10, 130, 228, 212);

                theme.nameOpacityInMainMenu = Math.max(0, Math.min(225, theme.nameOpacityInMainMenu));

                if (theme.nameOpacityInMainMenu > 1)
                    CustomFont.drawCenteredString(theme.getName(), themeX + offset + 44, themeY + 2, new Color(255, 255, 255, Math.round(theme.getNameOpacityInMainMenu())).getRGB());

                offset += (themeWidth + 10);
            }

            for (final Theme theme : Theme.values()) {
                if (mouseOverAnyThemes) {
                    theme.opacityInMainMenu += 4;
                } else {
                    theme.opacityInMainMenu -= 2;
                }

                if (theme.opacityInMainMenu > 255) theme.opacityInMainMenu = 255;
                if (theme.opacityInMainMenu < 15) theme.opacityInMainMenu = 15;
            }
        }

        GlUtils.stopScale();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        sr = new ScaledResolution(mc);
        // Rick roll
        if (mouseOver(x, y, fontRenderer.getWidth(StarX.NAME), fontRenderer.getHeight(StarX.NAME) - 8, mouseX, mouseY)) {
                try {
                    openWebpage(new URI("https://www.bilibili.com/video/BV1GJ411x7h7"));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
        }

        if (mouseOver(10, 130, 183, 256, mouseX, mouseY) && sr.getScaledHeight() > 300) {
            try {
                openWebpage(new URI("https://www.bilibili.com/video/BV1XR4y1b7P2"));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        float offset = 0;
        for (final Theme theme : Theme.values()) {

            if (mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY)) {
                StarX.INSTANCE.getGuiTheme().setCurrentTheme(theme);
            }
            offset += (themeWidth + 10);
        }

        //Close
//        if (mouseOver(x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, buttonWidth / 7, buttonHeight / 2 - 1, mouseX, mouseY)) {
//            System.exit(-1);
//        }

        sr = new ScaledResolution(Minecraft.getMinecraft());

        //Singleplayer
        if (mouseOver(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        //Exit
        if (mouseOver(sr.getScaledWidth() - 50f,5f, buttonWidth, buttonHeight + 2, mouseX, mouseY)){
            mc.shutdown();
        }

        //Multiplayer
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        //Altmanager
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
        //    mc.displayGuiScreen(Rise.INSTANCE.getAltGUI() /*Rise.INSTANCE.getAltManagerGUI()*/);
        }

        //Settings
        if (mouseOver(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }

        //Proxy
        if (mouseOver(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiProtocolSelector(this));
        }

        //Version
     //   if (mouseOver(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
     //       if (!Rise.INSTANCE.isViaHasFailed()) {
     //           if (Rise.INSTANCE.versionSwitcher == null) {
     //               Rise.INSTANCE.versionSwitcher = new VersionGui();
     //           }

     //           mc.displayGuiScreen(Rise.INSTANCE.versionSwitcher);
     //       }
     //   }

     /*   if (mouseOver(0, 0, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            if (!mc.riseMusicTicker.shouldKeepPlaying) {
                mc.riseMusicTicker.shouldKeepPlaying = true;
                mc.mcMusicTicker.func_181557_a();
            } else {
                mc.riseMusicTicker.shouldKeepPlaying = false;
                mc.riseMusicTicker.stopPlaying();
            }
        } */

    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }



    public static boolean openWebpage(final URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
