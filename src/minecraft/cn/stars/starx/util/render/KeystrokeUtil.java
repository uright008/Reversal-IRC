package cn.stars.starx.util.render;

import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class KeystrokeUtil {

    int ticksSinceLastPress;

    KeyBinding key;

    private final TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");

    public void setUpKey(final KeyBinding k) {
        key = k;
    }

    public void drawButton(final double x, final double y, final double width) {
        final String keyName = Keyboard.getKeyName(key.getKeyCode());

        float offset = -5;

        switch (keyName) {
            case "A":
            case "D":
                offset = -4;
                break;
            case "S":
                offset = -3.5f;
                break;
        }

        if (key == Minecraft.getMinecraft().gameSettings.keyBindJump) {
            RenderUtil.rect(x - width - 2, y, width * 3 + 8, width, new Color(255, 255, 255, 15 + ticksSinceLastPress));
            comfortaa.drawString(keyName, (float) (x - 2), (float) (y + 15 - 4), -1);
        } else {
            RenderUtil.rect(x, y, width, width, new Color(255,255,255, 15 + ticksSinceLastPress));
            comfortaa.drawString(keyName, (float) (x + 13 + offset), (float) (y + 15 - 4.5), -1);
        }
    }

    public void updateAnimations() {

        if (key == null)
            return;

        if (key.isKeyDown())
            ticksSinceLastPress += 5;
        else
            ticksSinceLastPress -= 5;

        if (ticksSinceLastPress > 150)
            ticksSinceLastPress = 150;

        if (ticksSinceLastPress < 0)
            ticksSinceLastPress = 0;

    }

    public void blur(final double x, final double y, final double width) {

        if (key == null)
            return;

        final String keyName = Keyboard.getKeyName(key.getKeyCode());
        if (key == Minecraft.getMinecraft().gameSettings.keyBindJump) {
            comfortaa.drawString(keyName, (float) (x), (float) (y + 15 - 2.5), Color.BLACK.hashCode());
        } else {
            RenderUtil.circle(x, y, width, Color.BLACK);
        }

    }

}
