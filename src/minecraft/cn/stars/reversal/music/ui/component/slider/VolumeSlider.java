package cn.stars.reversal.music.ui.component.slider;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.music.ui.ThemeColor;
import cn.stars.reversal.util.render.RenderUtil;
import cn.stars.reversal.util.render.RoundedUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class VolumeSlider implements GameInstance {
    private boolean dragging, hovering;
    private float dragDelta;

    public void draw(float x, float y, int mouseX, int mouseY) {
        float width = 100f;

        hovering = RenderUtil.isHovered(x, y - 3f, width, 7f, mouseX, mouseY);

        float sliderX = x + 15f;
        double currentVolume = Reversal.musicManager.screen.player.getVolume();
        double totalVolume = 100d;

        regular16.drawString("音量", x - 2f, y - 6.5f / 2f + 1f, Color.WHITE.getRGB());
        RoundedUtil.drawRound(sliderX, y, 70, 1f, 1f, ThemeColor.barColor);

        dragDelta = dragging ? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 70f) dragDelta = 70f;
        float currentWidth = dragging ? dragDelta : (float) (70 * (currentVolume / totalVolume));
        if (dragging)
            Reversal.musicManager.screen.player.setVolume((dragDelta / 70f) * 100d);
        RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, ThemeColor.barPlayedColor);
        RoundedUtil.drawRound(x + 13f + currentWidth, y - 1.5f, 4f, 4f, 1.5f, ThemeColor.barPlayedColor);
        regular16.drawString(Math.round(currentVolume) + "%", x + 88f, y - 6.5f / 2f + 1f, Color.WHITE.getRGB());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
        }
    }
}
