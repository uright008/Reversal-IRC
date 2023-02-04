package cn.stars.starx.module.impl.addons;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.ui.gui.GuiEditHUD;
import cn.stars.starx.ui.gui.GuiScreenshotViewer;

@ModuleInfo(name = "EditHUD", description = "Edit the hud position",category = Category.ADDONS)
public class EditHUD extends Module {
    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiEditHUD());
        setEnabled(false);
    }
}
