package cn.stars.starx.ui.splash;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.util.animation.lb.AnimatedValue;
import cn.stars.starx.util.animation.lb.EaseUtils;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.animation.simple.SimpleAnimation;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.shader.round.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class SplashProgress {
    // Max amount of progress updates
    private static final int DEFAULT_MAX = 14;
   // private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("PSM 18");
    private static int PROGRESS;
    private static String CURRENT = "";
    // Background texture
    private static ResourceLocation splash;
    // Texture manager
    private static TextureManager ctm;

    /**
     * Update the splash text
     */
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    /**
     * Update the splash progress
     *
     * @param givenProgress Stage displayed on the splash
     * @param givenSplash   Text displayed on the splash
     */
    public static void setProgress(final int givenProgress, final String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    /**
     * Render the splash screen background
     *
     * @param tm {@link TextureManager}
     */
    public static void drawSplash(final TextureManager tm) {
        // Initialize the texture manager if null
        if (ctm == null) ctm = tm;

        // Get the users screen width and height to apply
        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        // Create the scale factor
        final int scaleFactor = scaledresolution.getScaleFactor();

        // Bind the width and height to the framebuffer
        final Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
                scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        // Create the projected image to be rendered
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, (double)scaledresolution.getScaledWidth(), (double)scaledresolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // Initialize the splash texture
        splash = new ResourceLocation("starx/images/splash.png");

        // Bind the texture
        tm.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the image
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080,
                scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);

        // Draw the progress bar
        drawProgress();

        // Unbind the width and height as it's no longer needed
        framebuffer.unbindFramebuffer();

        // Render the previously used frame buffer
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);

        // Update the texture to enable alpha drawing
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);

        // Update the users screen
        Minecraft.getMinecraft().updateDisplay();
    }

    /**
     * Render the progress bar and text
     */
    private static void drawProgress() {
        if (Minecraft.getMinecraft().getTextureManager() == null)
            return;
        // Screen Height
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Pos
        final float startX = (sr.getScaledWidth() / 2.0F - 80);
        final float endX = (sr.getScaledWidth() / 2.0F + 80);

        // Calculate the progress bar
        final double nProgress = PROGRESS;
        final double calc = (nProgress / DEFAULT_MAX) * 160 - 2;
        // Draw the transparent bar before the green bar
      //  RenderUtil.roundedOutlineGradientRectangle(startX, sr.getScaledHeight() / 2.0F + 20.0F, endX - startX, 10F, 3f, 1f, new Color(0, 200, 100, 200), new Color(0, 200, 200, 200));
      //  Gui.drawRect(startX, sr.getScaledHeight() / 2.0F + 15.0F, endX, sr.getScaledHeight() / 2.0F + 20.0F, new Color(255, 255, 255, 60).getRGB());

        // Render the blue progress bar
     //   RenderUtil.roundedRectangle(startX + 1, sr.getScaledHeight() / 2.0F + 21.0F, MathUtil.round(calc, 2), 8F, 3f, new Color(180, 180, 180, 220));
     //   Gui.drawRect(startX, sr.getScaledHeight() / 2.0F + 15.0F, (float) (startX + calc), sr.getScaledHeight() / 2.0F + 20.0F, new Color(20, 255, 20, 160).getRGB());

    }
}
