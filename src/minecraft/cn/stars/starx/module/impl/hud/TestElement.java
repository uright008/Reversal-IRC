package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "TestElement", description = "Only for test",
        chineseDescription = "仅供测试,别开", category = Category.HUD)
public class TestElement extends Module {
    private final NoteValue note = new NoteValue("Only for test purpose. DO NOT enable this.", this);
    private final NumberValue speed = new NumberValue("Scroll Speed", this, 2.0, 0.5, 9.0, 1.0);
    List<String> strings = new ArrayList<>();
    public TestElement() {
        setCanBeEdited(true);
        setX(100);
        setY(100);
        setWidth(100);
        setHeight(100);
        strings.add("测试中文");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        FontManager.getRegular(24).drawString("--- Modern MFont Renderer ---", x, y + 20, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
        strings.forEach(i -> {
            FontManager.getRegular(20).drawString(i, x, y + 35 + 15 * strings.indexOf(i), new Color(255,255,255,250).getRGB());
        });

        RenderUtil.roundedRectangle(x - 10, y - 10, 300, 50, 3, Color.YELLOW);
        RenderUtils.drawShadow(x - 10, y - 10, 300, 50);
    }

    @Override
    protected void onEnable() {
    }
}
