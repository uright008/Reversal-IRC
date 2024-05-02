package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "HUD", description = "Show a hud on your screen", category = Category.HUD)
public class HUD extends Module {
    public HUD() {
        setWidth(0);
        setHeight(0);
        setCanBeEdited(false);
    }
}
