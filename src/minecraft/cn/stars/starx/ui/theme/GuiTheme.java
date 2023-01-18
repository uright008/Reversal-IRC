package cn.stars.starx.ui.theme;

import cn.stars.starx.util.render.ColorUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class GuiTheme {
    public Theme currentTheme = Theme.LIGHTMODE;

    public Color getThemeColor(final Theme theme) {
        switch (Objects.requireNonNull(theme)) {
            case DARKMODE:
                return new Color(60, 60, 60);
        }

        return new Color(ColorUtil.getRainbow());
    }

    public Color getThemeColor() {
        return getThemeColor(currentTheme);
    }
}