package cn.stars.starx.util.render;

import cn.stars.starx.event.impl.BlurEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
public class InGameBlurUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtil gaussianProgram = new ShaderUtil("starx/shader/blur.frag");
    public static Framebuffer toBlurBuffer = new Framebuffer(1, 1, false);
    public static Framebuffer blurredBuffer = new Framebuffer(1, 1, false);
    private static Framebuffer blurPass2 = new Framebuffer(1, 1, false);
    private static Framebuffer blurPass3 = new Framebuffer(1, 1, false);

    public static ScaledResolution sr = new ScaledResolution(mc);

    private static void setupBlurUniforms(final float radius) {
        GL20.glUniform1i(gaussianProgram.getUniform("texture"), 0);
        GL20.glUniform2f(gaussianProgram.getUniform("texelSize"), 1.0F / mc.displayWidth, 1.0F / mc.displayHeight);
        GL20.glUniform1f(gaussianProgram.getUniform("radius"), MathHelper.ceiling_float_int((2 * radius)));
    }


    public static void setupBuffers() {
        if (mc.displayWidth != blurredBuffer.framebufferWidth || mc.displayHeight != blurredBuffer.framebufferHeight) {
            blurredBuffer.deleteFramebuffer();
            blurredBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);

            blurPass2.deleteFramebuffer();
            blurPass2 = new Framebuffer(mc.displayWidth, mc.displayHeight, false);

            blurPass3.deleteFramebuffer();
            blurPass3 = new Framebuffer(mc.displayWidth, mc.displayHeight, false);

            toBlurBuffer.deleteFramebuffer();
            toBlurBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        }
    }

    public static void preBlur() {
        toBlurBuffer.bindFramebuffer(false);
        setupBuffers();
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
    }


}
