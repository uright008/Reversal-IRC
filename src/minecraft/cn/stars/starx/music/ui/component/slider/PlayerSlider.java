package cn.stars.starx.music.ui.component.slider;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.music.ui.ThemeColor;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RoundedUtil;

import java.awt.*;


/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class PlayerSlider implements GameInstance {
    private boolean dragging, hovering;

    private float dragDelta;

    public void draw(float x, float y, int mouseX, int mouseY) {
        float width = 200f;

        hovering = RenderUtil.isHovered(x, y, width, 5f, mouseX, mouseY);

        float sliderX = x + 15f;
        double currentTime = StarX.musicManager.screen.player.getCurrentTime();
        double totalTime = StarX.musicManager.screen.player.getMusic().getDuration();

        String formattedTime = formatTime(currentTime);
        regular16.drawString(formattedTime, x - 9f, y - 6.5f / 2f + 1f, Color.WHITE.getRGB());
        RoundedUtil.drawRound(sliderX, y, 170, 1f, 1f, ThemeColor.barColor);

        dragDelta = dragging? mouseX - sliderX : 0f;
        if (dragDelta < 0) dragDelta = 0;
        if (dragDelta > 170f) dragDelta = 170f;
        float currentWidth = dragging ? dragDelta : (float) (170 * (currentTime / totalTime));
        RoundedUtil.drawRound(x + 15f, y, currentWidth, 1f, 1f, ThemeColor.barPlayedColor);
        RoundedUtil.drawRound(x + 13f + currentWidth, y - 1.5f, 4f, 4f, 1.5f, ThemeColor.barPlayedColor);

        regular16.drawString(formatTime(totalTime), x + 190f, y - 6.5f / 2f + 1f, ThemeColor.greyColor.getRGB());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
            long duration = StarX.musicManager.screen.player.getMusic().getDuration();
            double newTime = duration * (dragDelta / 170f);
            if (newTime < 0) newTime = 0;
            if (newTime > duration) newTime = duration;
            StarX.musicManager.screen.player.setCurrentTime(newTime);
        }
    }

    public static String formatTime(double millis) {
        int totalSeconds = (int) (millis / 1000); // 将毫秒转换为总秒数
        int minutes = totalSeconds / 60;          // 计算分钟数
        int seconds = totalSeconds % 60;          // 计算剩余的秒数

        // 格式化为 "分钟:秒" 的形式，秒数如果小于10，前面补0
        return String.format("%02d:%02d", minutes, seconds);
    }
}
