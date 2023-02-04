package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "TextGui", description = "Display a text on your hud", category = Category.HUD)
public class TextGui extends Module {
    public TextGui() {
        setX(1);
        setY(5);
        setWidth(50);
        setHeight(30);
    }
}
