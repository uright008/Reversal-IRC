package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Arraylist", description = "Show the modules you enabled",
        chineseDescription = "显示你开启的功能", category = Category.HUD)
public class Arraylist extends Module {
    private final BoolValue glowShadow = new BoolValue("Glow Shadow", this, false);
    private final BoolValue noRenderModules = new BoolValue("No Render Modules", this, false);
    final ScaledResolution SR = new ScaledResolution(mc);
    final float offset = 6;
    final float arraylistX = SR.getScaledWidth() - offset;

    public Arraylist() {
        setCanBeEdited(true);
        setX((int) arraylistX);
    }
}
