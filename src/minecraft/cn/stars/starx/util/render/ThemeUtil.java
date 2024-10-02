package cn.stars.starx.util.render;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Objects;

@UtilityClass
public final class ThemeUtil implements GameInstance {

    @Getter
    @Setter
    private String customClientName = "";
    private final Color color1 = new Color(233,80,169);
    private final Color color2 = new Color(71, 253, 160);
    private Color color;
    private String theme;
    private boolean switcher;

    private final TimeUtil timer = new TimeUtil();

    public Color getThemeColor(final ThemeType type) {
        return getThemeColor(0, type, 1);
    }

    public int getThemeColorInt(final ThemeType type) {
        return getThemeColor(type).hashCode();
    }

    public int getThemeColorInt(final float colorOffset, final ThemeType type) {
        return getThemeColor(colorOffset, type, 1).hashCode();
    }

    public int getThemeColorInt(final float colorOffset, final ThemeType type, final float timeMultiplier) {
        return getThemeColor(colorOffset, type, timeMultiplier).hashCode();
    }

    public Color getThemeColor(final float colorOffset, final ThemeType type) {
        return getThemeColor(colorOffset, type, 1);
    }

    public static Color getThemeColor(final ThemeType type, final float colorOffset) {
        return getThemeColor(colorOffset, type, 1);
    }

    public Color getThemeColor(float colorOffset, final ThemeType type, final float timeMultiplier) {
        if (timer.hasReached(50 * 5)) {
            timer.reset();
            theme = ((ModeValue) Objects.requireNonNull(StarX.moduleManager.getSetting("ClientSettings", "Theme"))).getMode();
            color = new Color(StarX.CLIENT_THEME_COLOR);
        }

        if (theme == null || color == null) return color;

        float colorOffsetMultiplier = 1;

        if (type == ThemeType.GENERAL) {
            switch (theme) {
                case "Rise":
                case "Comfort":
                case "Minecraft":
                case "Never Lose":
                    colorOffsetMultiplier = 2.2f;
                    break;

                case "Simple": {
                    if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Rainbow")) {
                        colorOffsetMultiplier = 5f;
                    } else if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Fade")) {
                        colorOffsetMultiplier = 2.2f;
                    }
                    break;
                }

                case "Modern":
                case "Rise Rainbow":
                case "Minecraft Rainbow":
                case "StarX":
                    colorOffsetMultiplier = 5f;
                    break;
            }
        }

        colorOffsetMultiplier *= (float) ModuleInstance.getNumber("ClientSettings", "Index Times").getValue();
        colorOffset *= colorOffsetMultiplier;

        final double timer = (System.currentTimeMillis() / 1E+8 * timeMultiplier) * 4E+5;

        switch (type) {
            case GENERAL:
            case ARRAYLIST:
                switch (theme) {
                    case "Rise":
                    case "Comfort":
                    case "Minecraft":
                    case "Never Lose": {
                        final float offset1 = (float) (Math.abs(Math.sin(timer + colorOffset * 0.45)) / 2) + 1f;
                        color = ColorUtil.liveColorBrighter(StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR, offset1);
                        break;
                    }

                    case "Simple": {
                        if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Rainbow")) {
                            color = new Color(ColorUtil.getColor(-(1 + colorOffset * 1.7f), 0.7f, 1));
                        } else if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Fade")) {
                            final float offset1 = (float) (Math.abs(Math.sin(timer * 0.5 + colorOffset * 0.45)) / 2.2f) + 1f;
                            color = ColorUtil.liveColorBrighter(StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR, offset1);
                        } else {
                            color = ColorUtils.INSTANCE.interpolateColorsBackAndForth(30, (int) colorOffset * 4, StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR, StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR_2, true);
                        }
                        break;
                    }

                    case "Modern":
                    case "Rise Rainbow":
                    case "Minecraft Rainbow":
                    case "StarX":
                        color = new Color(ColorUtil.getColor(-(1 + colorOffset * 1.7f), 0.7f, 1));
                        break;
                }

                break;

            case LOGO:
                switch (theme) {
                    case "Rise":
                    case "Comfort":
                    case "Minecraft":
                        color = new Color(StarX.CLIENT_THEME_COLOR_BRIGHT);
                        break;

                    case "Simple": {
                        if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Rainbow")) {
                            color = new Color(ColorUtil.getColor(1 + colorOffset * 1.4f, 0.5f, 1));
                        } else if (ModuleInstance.getMode("ClientSettings", "Color Type").getMode().equals("Fade")) {
                            color = new Color(StarX.CLIENT_THEME_COLOR_BRIGHT);
                        } else {
                            color = ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 1, StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR, StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR_2, true);
                        }
                    }

                    case "Modern":
                    case "Rise Rainbow":
                    case "Minecraft Rainbow":
                    case "StarX":
                        color = new Color(ColorUtil.getColor(1 + colorOffset * 1.4f, 0.5f, 1));
                        break;
                }

                break;

            default:
            case FLAT_COLOR:
                color = new Color(StarX.CLIENT_THEME_COLOR);
        }

        return color;
    }
}