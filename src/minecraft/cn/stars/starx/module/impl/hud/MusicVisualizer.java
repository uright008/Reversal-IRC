/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.hud;

import cn.stars.starx.LonelyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.music.ui.ThemeColor;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.RoundedUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "MusicVisualizer", chineseName = "音乐可视化", description = "Visualize the music using rects",
        chineseDescription = "用边框使音乐可视化", category = Category.HUD)
public class MusicVisualizer extends Module {
    private final NumberValue bands = new NumberValue("Bands", this, 128, 16, 256, 1);
    private final NumberValue heightValue = new NumberValue("Height", this, 50, 30, 100, 1);
    private final NumberValue widthValue = new NumberValue("Width", this, 100, 50, 500, 1);
    private final BoolValue fillValue = new BoolValue("Fill", this, true);
    private final NumberValue rectSpace = new NumberValue("RectSpace", this, 2f, 1f, 5f, 0.01f);
    private final NumberValue rectRadius = new NumberValue("RectRadius", this, 3f, 0f, 5f, 0.1f);
    private final NumberValue indexOffset = new NumberValue("IndexOffset", this, 6f, 0f, 20f, 1f);
    ScaledResolution sr;
    public static float[] magnitudeInterp = null;
    TimeUtil timeUtil = new TimeUtil();

    public MusicVisualizer() {
        setCanBeEdited(true);
        setX(100);
        setY(100);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!LonelyAPI.hasJavaFX) return;
        sr = new ScaledResolution(mc);
        GL11.glPushMatrix();
        setWidth(fillValue.isEnabled() ? sr.getScaledWidth() : (int) widthValue.getValue());
        setHeight((int) heightValue.getValue());
        float renderX = getX();
        float renderY = getY();
        float step = (float) (getWidth() / bands.getValue());
        MediaPlayer mediaPlayer = StarX.musicManager.screen.player.getMediaPlayer();
        if (mediaPlayer != null && StarX.musicManager.screen.player.getMagnitudes() != null) {
            mediaPlayer.setAudioSpectrumNumBands((int) bands.getValue());
            float[] magnitudes = StarX.musicManager.screen.player.getMagnitudes();
            float[] vertex = new float[magnitudes.length * 2 + 2];

            if (magnitudeInterp == null) {
                magnitudeInterp = magnitudes;
            }

            for (int i = 0; i < magnitudeInterp.length; i++) {
                magnitudeInterp[i] = (float) lerp(magnitudeInterp[i], magnitudes[i], timeUtil.getElapsedTime() * 0.5);
            }

            vertex[0] = renderX;
            vertex[1] = renderY + getHeight();
            renderX += step;
            int vertexIndex = 2;
            int colorIndex = 0;
            for (float magnitude : magnitudeInterp) {
                float realY = (float) (renderY + heightValue.getValue() - (1 - (-magnitude / 60f)) * 1.2f * heightValue.getValue());
                vertex[vertexIndex] = renderX;
                vertex[vertexIndex + 1] = realY;

                RoundedUtil.drawGradientVertical(renderX - step, (float) (realY + (renderY + heightValue.getValue()) * (sr.getScaleFactor() * 0.5f - 1)), step, Math.max(renderY + getHeight() - realY, 1) * 2, (float) rectRadius.getValue(), ThemeUtil.getThemeColor(colorIndex,ThemeType.ARRAYLIST), new Color(0,0,0,0));
                colorIndex += (int) indexOffset.getValue();

                vertexIndex += 2;
                renderX += (float) (step + rectSpace.getValue());
            }
        }
        GL11.glPopMatrix();
    }

    public static double lerp(double old, double newVal, double amount) {

        boolean increasing = old < newVal;

        double result = (1.0 - amount) * old + amount * newVal;

        if (increasing) {
            return Math.min(newVal, result);
        } else {
            return Math.max(newVal, result);
        }

    }
}
