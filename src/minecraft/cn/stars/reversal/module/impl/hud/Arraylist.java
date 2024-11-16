package cn.stars.reversal.module.impl.hud;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.value.impl.BoolValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Arraylist", chineseName = "功能列表", description = "Show the modules you enabled",
        chineseDescription = "显示你开启的功能", category = Category.HUD)
public class Arraylist extends Module {
    public final BoolValue glowShadow = new BoolValue("Glow Shadow", this, false);
    public final BoolValue noRenderModules = new BoolValue("No Render Modules", this, false);
    public final BoolValue chinese = new BoolValue("Simple Chinese", this, true);
    final ScaledResolution SR = new ScaledResolution(mc);
    final float offset = 6;
    final float arraylistX = SR.getScaledWidth() - offset;

    public Arraylist() {
        setCanBeEdited(true);
        setX((int) arraylistX);
    }
}
