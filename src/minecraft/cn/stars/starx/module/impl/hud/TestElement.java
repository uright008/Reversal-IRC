package cn.stars.starx.module.impl.hud;

import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.ColorUtils;
import cn.stars.starx.util.render.RoundedUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "TestElement", chineseName = "测试功能", description = "Only for test",
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
    }


    @Override
    public void onShader3D(Shader3DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        RoundedUtil.drawGradientRound(x, y, 124, 84, 4,
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 1000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 2000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 4000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 3000, new Color(0, 200, 200, 255), Color.BLACK, true));
        RoundedUtil.drawRound(x + 1, y + 1, 122, 82, 4, new Color(0,0,0, 200));
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        RoundedUtil.drawGradientRound(x, y, 124, 84, 4,
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 1000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 2000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 4000, new Color(0, 200, 200, 255), Color.BLACK, true),
                ColorUtils.INSTANCE.interpolateColorsBackAndForth(4, 3000, new Color(0, 200, 200, 255), Color.BLACK, true));
        RoundedUtil.drawRound(x + 1, y + 1, 122, 82, 4, new Color(0,0,0, 200));

    }

    @Override
    protected void onEnable() {
    }
}
