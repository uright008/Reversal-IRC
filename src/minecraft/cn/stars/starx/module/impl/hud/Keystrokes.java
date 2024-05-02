package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;

@ModuleInfo(name = "Keystrokes", description = "Show the WASD operation on screen.", category = Category.HUD)
public class Keystrokes extends Module {
    public final BoolValue custom = new BoolValue("Custom Name", this,false);
    public Keystrokes() {
        setCanBeEdited(true);
        setX(1);
        setY(5);
        setWidth(100);
        setHeight(100);
    }
}
