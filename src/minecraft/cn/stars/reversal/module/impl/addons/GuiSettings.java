package cn.stars.reversal.module.impl.addons;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.setting.impl.BoolValue;
import cn.stars.reversal.setting.impl.ModeValue;
import cn.stars.reversal.setting.impl.NoteValue;

@ModuleInfo(name = "GuiSettings", chineseName = "界面设置", description = "Change some mc guis",
        chineseDescription = "改变一些原版的GUI", category = Category.ADDONS)
public class GuiSettings extends Module {

    @Override
    public void onUpdateAlways() {
        if (!isEnabled()) toggleModule();
    }

    private final BoolValue allGuisShaderBackground = new BoolValue("Shader In All Guis", this, false);
    private final BoolValue curiosityStyle = new BoolValue("Curiosity Style", this, true);
    private final ModeValue curiosityMenuButtonPosition = new ModeValue("Button Position", this, "Middle", "Down", "Middle");
    private final NoteValue note = new NoteValue("If the buttons in the main menu doesn't fit you, change the option above to move their positions.", this);
}
