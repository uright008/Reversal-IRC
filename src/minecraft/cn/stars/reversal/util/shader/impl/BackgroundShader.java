package cn.stars.reversal.util.shader.impl;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.util.render.RenderUtils;
import cn.stars.reversal.util.shader.base.CompactShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;

public final class BackgroundShader extends CompactShader implements GameInstance {

    public final static BackgroundShader BACKGROUND_SHADER = new BackgroundShader();

    private float time;

    public BackgroundShader() {
        super("background2.frag");
    }

    @Override
    public void setupUniforms() {
        setupUniform("iResolution");
        setupUniform("iTime");
    }

    @Override
    public void updateUniforms() {
        if (!Display.isVisible()) {
            return;
        }
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        final int resolutionID = getUniform("iResolution");
        if(resolutionID > -1)
            GL20.glUniform2f(resolutionID, (float) scaledResolution.getScaledWidth() * 2, (float) scaledResolution.getScaledHeight() * 2);
        final int timeID = getUniform("iTime");
        if(timeID > -1) GL20.glUniform1f(timeID, time);

        time += 0.005F * RenderUtils.deltaTime;
    }

}
