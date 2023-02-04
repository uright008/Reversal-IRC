package cn.stars.starx.util.render;

import cn.stars.starx.StarX;
import cn.stars.starx.util.animation.simple.SimpleAnimation;

import java.awt.Color;

public class ClickEffect {

    private float x, y;

    private SimpleAnimation animation = new SimpleAnimation(0.0F);

    public ClickEffect(float x, float y) {
        this.x = x;
        this.y = y;
        animation.setValue(0);
    }

    public void draw() {
        animation.setAnimation(100, 12);
        double radius = 8 * animation.getValue() / 100;
        int alpha = (int)(255 - 255 * animation.getValue() / 100);
        boolean accentColor = true;
        int color = new Color(255, 255, 255, alpha).getRGB();

        RenderUtils.drawArc(x, y, radius, color, 0, 360, 3);
    }

    public boolean canRemove() {
        return animation.getValue() > 99;
    }
}
