package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.util.render.*;

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
    }


    @Override
    public void onShader3D(Shader3DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        RoundedUtil.drawRound(x, y, 100, 50, 4, Color.BLACK);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        RoundedUtil.drawRound(x, y, 100, 50, 4, new Color(10,10,10,150));

    }

    @Override
    protected void onEnable() {
    }
}
