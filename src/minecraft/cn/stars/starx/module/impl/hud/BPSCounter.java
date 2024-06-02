package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "BPSCounter", description = "Show your BPS on screen", category = Category.HUD)
public class BPSCounter extends Module {
    private final BoolValue shadow = new BoolValue("Shadow", this, true);
    public BPSCounter() {
        setCanBeEdited(true);
        setX(5);
        setY(300);
        setWidth(100);
        setHeight(30);
    }

}
