package cn.stars.starx.module.impl.addons;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.ui.gui.GuiScreenshotViewer;

@ModuleInfo(name = "ScreenshotViewer", description = "View your screenshot fast in game",
        chineseDescription = "在游戏内查看你的截图", category = Category.ADDONS)
public class ScreenshotViewer extends Module {
    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiScreenshotViewer());
        setEnabled(false);
    }
}
