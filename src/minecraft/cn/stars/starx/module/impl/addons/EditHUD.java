package cn.stars.starx.module.impl.addons;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.ui.gui.GuiEditHUD;
import cn.stars.starx.ui.gui.GuiScreenshotViewer;
import cn.stars.starx.ui.gui.breakout.BreakoutGuiIngameMenu;

@ModuleInfo(name = "EditHUD", description = "Edit the hud position",category = Category.ADDONS)
public class EditHUD extends Module {
    @Override
    public void onEnable() {
        mc.displayGuiScreen(new BreakoutGuiIngameMenu());
        setEnabled(false);
    }
}
