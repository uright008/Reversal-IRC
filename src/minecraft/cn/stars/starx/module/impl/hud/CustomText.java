/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;

import java.awt.*;

@ModuleInfo(name = "CustomText", chineseName = "自定义文字", description = "Show a custom text on screen", chineseDescription = "在屏幕上显示自定义文字", category = Category.HUD)
public class CustomText extends Module {
    private final NoteValue note = new NoteValue("Use \".setText <text>\" to set text.", this);
    private final NumberValue size = new NumberValue("Size", this, 16, 4, 64, 1);
    private final BoolValue bold = new BoolValue("Bold", this, false);
    private final BoolValue gradient = new BoolValue("Gradient", this, false);

    public CustomText() {
        setX(100);
        setY(100);
        setCanBeEdited(true);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 1;
        int y = getY() + 1;
        if (bold.isEnabled()) {
            if (gradient.isEnabled()) {
                float off = 0;
                for (int i = 0; i < StarX.customText.length(); i++) {
                    final Color c = ThemeUtil.getThemeColor(ThemeType.ARRAYLIST, i);

                    final String character = String.valueOf(StarX.customText.charAt(i));
                    FontManager.getRegularBold((int)size.getValue()).drawString(character, x + off, y, c.getRGB());
                    off += FontManager.getRegularBold((int)size.getValue()).width(character);
                }
            } else {
                final Color c = ThemeUtil.getThemeColor(ThemeType.ARRAYLIST);
                FontManager.getRegularBold((int)size.getValue()).drawString(StarX.customText, x, y, c.getRGB());
            }
        } else {
            if (gradient.isEnabled()) {
                float off = 0;
                for (int i = 0; i < StarX.customText.length(); i++) {
                    final Color c = ThemeUtil.getThemeColor(ThemeType.ARRAYLIST, i);

                    final String character = String.valueOf(StarX.customText.charAt(i));
                    FontManager.getRegularBold((int)size.getValue()).drawString(character, x + off, y, c.getRGB());
                    off += FontManager.getRegular((int)size.getValue()).width(character);
                }
            } else {
                final Color c = ThemeUtil.getThemeColor(ThemeType.ARRAYLIST);
                FontManager.getRegular((int)size.getValue()).drawString(StarX.customText, x, y, c.getRGB());
            }
        }
        setWidthAndHeight();
    }

    private void setWidthAndHeight() {
        if (bold.isEnabled()) {
            setWidth((int) (FontManager.getRegularBold((int)size.getValue()).width(StarX.customText) + 2));
            setHeight((int) (FontManager.getRegularBold((int)size.getValue()).height() + 1));
        } else {
            setWidth((int) (FontManager.getRegular((int)size.getValue()).width(StarX.customText) + 2));
            setHeight((int) (FontManager.getRegular((int)size.getValue()).height() + 1));
        }
    }

}
