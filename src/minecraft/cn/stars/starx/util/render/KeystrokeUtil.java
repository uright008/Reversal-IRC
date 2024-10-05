package cn.stars.starx.util.render;

import cn.stars.starx.GameInstance;
import cn.stars.starx.util.misc.ModuleInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeystrokeUtil implements GameInstance {

    int ticksSinceLastPress;

    KeyBinding key;

    public void setUpKey(final KeyBinding k) {
        key = k;
    }

    public void drawButton(final double x, final double y, final double width) {
        final boolean rainbow = ModuleInstance.getBool("Keystrokes", "Rainbow when key down").isEnabled();
        final boolean shadow = ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled();
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
            RenderUtil.roundedRectangle(x - width - 2, y, width * 3 + 8, width, 2, new Color(255, 255, 255, 15 + ticksSinceLastPress));
            regular18.drawString(keyName, (float) (x - 2), (float) (y + 15 - 4), key.isKeyDown() && rainbow ? ThemeUtil.getThemeColor(ThemeType.GENERAL).getRGB() : -1);
            if (shadow) {
                MODERN_BLOOM_RUNNABLES.add(() -> {
                    RenderUtil.roundedRectangle(x - width - 2, y, width * 3 + 8, width, 2, key.isKeyDown() ? new Color(255, 255, 255, 255) : new Color(255, 255, 255, 15));
                });
            }
        } else {
            RenderUtil.roundedRectangle(x, y, width, width, 2, new Color(255,255,255, 15 + ticksSinceLastPress));
            regular18.drawString(keyName, (float) (x + 13 + offset), (float) (y + 15 - 4.5), key.isKeyDown() && rainbow ? ThemeUtil.getThemeColor(ThemeType.GENERAL).getRGB() : -1);
            if (shadow) {
                MODERN_BLOOM_RUNNABLES.add(() -> {
                    RenderUtil.roundedRectangle(x, y, width, width, 2, key.isKeyDown() ? new Color(255, 255, 255, 255) : new Color(255, 255, 255, 15));
                });
            }
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
            regular18.drawString(keyName, (float) (x), (float) (y + 15 - 2.5), Color.BLACK.hashCode());
        } else {
            RenderUtil.circle(x, y, width, Color.BLACK);
        }

    }

}
