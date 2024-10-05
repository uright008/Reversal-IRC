package cn.stars.starx.ui.splash;

import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.awt.*;

public final class SplashProgress {
    public static boolean isFinished = false;
    // Max amount of progress updates
    private static final int DEFAULT_MAX = 9;
   // private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("PSM 18");
    private static int PROGRESS;
    private static String CURRENT = "";
    // Background texture
    private static ResourceLocation splash;
    // Texture manager
    private static TextureManager ctm;
    Animation animation = new Animation(Easing.LINEAR, 200);

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
        if (PROGRESS == DEFAULT_MAX) isFinished = true;
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
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(),
                scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());

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
        Display.sync(60);
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
        final float startX = (sr.getScaledWidth() / 2.0F - 100);
        final float endX = (sr.getScaledWidth() / 2.0F + 100);

        // Calculate the progress bar
        final double nProgress = PROGRESS;
        final double calc = (nProgress / DEFAULT_MAX) * 200;


        // Draw the transparent bar before the green bar
        //  RenderUtil.roundedOutlineGradientRectangle(startX, sr.getScaledHeight() / 2.0F + 20.0F, endX - startX, 10F, 3f, 1f, new Color(0, 200, 100, 200), new Color(0, 200, 200, 200));
    //    RenderUtil.rect(startX, sr.getScaledHeight() / 2.0F + 30.0F, 200, 5f, new Color(255, 255, 255, 60));

        // Render the blue progress bar
        //   RenderUtil.roundedRectangle(startX + 1, sr.getScaledHeight() / 2.0F + 21.0F, MathUtil.round(calc, 2), 8F, 3f, new Color(180, 180, 180, 220));
    //    RenderUtil.gradientSideways(startX, sr.getScaledHeight() / 2.0F + 30.0F, (float) calc, 5f, new Color(0,255,255,50), new Color(0,255,255,255));
    }
}
