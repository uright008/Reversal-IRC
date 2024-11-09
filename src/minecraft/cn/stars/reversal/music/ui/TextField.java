package cn.stars.reversal.music.ui;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.font.MFont;
import cn.stars.reversal.util.animation.advanced.Direction;
import cn.stars.reversal.util.animation.advanced.composed.ColorAnimation;
import cn.stars.reversal.util.render.RenderUtil;
import cn.stars.reversal.util.render.RoundedUtil;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/2
 **/
@Getter
public class TextField {
    private float posX, posY;
    public float width, height, textMaxWidth, offsetX;
    public boolean focused, cursorRestored = false;
    public float radius;
    public String text;

    private MFont font;
    public Color backgroundColor, outlineColor;
    private final ColorAnimation textColorAnim;
    private final ColorAnimation cursorColorAnim;

    public TextField(float width, float height, MFont font, Color backgroundColor, Color outlineColor) {
        this.text = "";
        this.width = width;
        this.textMaxWidth = width - 2f;
        this.height = height;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
        this.textColorAnim = new ColorAnimation(Color.WHITE, ThemeColor.greyColor, 100);
        this.cursorColorAnim = new ColorAnimation(Color.WHITE, new Color(255, 255, 255, 0), 500);
        this.radius = 2f;
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;

        if (focused)
            Keyboard.enableRepeatEvents(true);

        if (focused && textColorAnim.getDirection() == Direction.FORWARDS) {
            textColorAnim.changeDirection();
        } else if (!focused && textColorAnim.getDirection() == Direction.BACKWARDS) {
            textColorAnim.changeDirection();
        }

        if (focused && cursorColorAnim.isFinished()) {
            cursorColorAnim.changeDirection();
        }

        if (RenderUtil.isHovered(posX, posY, width, height, mouseX, mouseY)) {
            GLFW.glfwSetCursor(Display.getWindow(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR));
            cursorRestored = false;
        } else if (!cursorRestored) {
            GLFW.glfwSetCursor(Display.getWindow(), MemoryUtil.NULL);
            cursorRestored = true;
        }

        font = GameInstance.regular16;

        RoundedUtil.drawRoundOutline(posX, posY, width, height, radius, 0.1f, backgroundColor, outlineColor);

        String visibleText = font.getStringWidth(text) > textMaxWidth - 3f - offsetX ? font.trimStringToWidth(text, textMaxWidth - 3f - offsetX, true, false) : text;
        float textX = posX + 2f;
        float textY = posY + height / 2f - 1.5f;
        font.drawString(visibleText, textX + 1f, textY, textColorAnim.getOutput().getRGB());

        if (focused) {
            float h = height * 0.6f;
            RoundedUtil.drawRound(textX + offsetX + font.getStringWidth(visibleText) + 1.5f, posY + height / 2 - h / 2, 0.5f, h, 1f, cursorColorAnim.getOutput());
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovering = RenderUtil.isHovered(posX, posY, width, height, mouseX, mouseY);
        if (hovering && button == 0) focused = true;
        if (!hovering) focused = false;
    }

    public void keyTyped(char c, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) focused = false;

        if (!focused) return;

        if (keyCode == Keyboard.KEY_BACK) {
            int max = text.length() - 1;
            if (max <= 0) {
                text = "";
            } else {
                text = text.substring(0, max);
            }
        } else {
            if (c == '\u0000') return;
            text += c;
        }
    }
}
