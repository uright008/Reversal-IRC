package cn.stars.reversal.module.impl.hud;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.setting.impl.BoolValue;

@ModuleInfo(name = "TextGui", chineseName = "客户端标题", description = "Display a text on your hud",
        chineseDescription = "显示客户端LOGO", category = Category.HUD)
public class TextGui extends Module {
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
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
