package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;

@ModuleInfo(name = "TextGui", description = "Display a text on your hud",
        chineseDescription = "显示客户端LOGO", category = Category.HUD)
public class TextGui extends Module {
    private final BoolValue shadow = new BoolValue("Shadow", this, true);
    public final BoolValue custom = new BoolValue("Custom Name", this,false);
    public TextGui() {
        setCanBeEdited(true);
        setX(1);
        setY(5);
        setWidth(50);
        setHeight(30);
    }
}
