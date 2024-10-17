package cn.stars.starx.util.render.particle;

import cn.stars.starx.GameInstance;
import cn.stars.starx.util.math.RandomUtil;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Particle implements GameInstance {
    private final float radius;
    private float x;
    private float y;
    private final float speed;
    
    public Particle(float startX, float radius) {
        this.radius = radius;
        x = startX;
        y = -radius;
        speed = RandomUtil.INSTANCE.nextFloat(0.5f, 1.5f);
    }
    public void render() {
        ScaledResolution sr = new ScaledResolution(mc);
        RoundedUtil.drawRound(x, y, radius, radius, radius / 3.5f, ColorUtil.applyOpacity(Color.WHITE, 0.7f));


        x += speed * RenderUtil.deltaFrameTime * RandomUtil.INSTANCE.nextFloat(0.4f, 1.1f);
        y += speed * RenderUtil.deltaFrameTime;
        if (y > sr.getScaledHeight()) y = -radius;
        if (x > sr.getScaledWidth()) {
            x = 0;
        }
    }
}