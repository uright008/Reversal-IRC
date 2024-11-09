package cn.stars.reversal.module.impl.hud;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Scoreboard", chineseName = "记分板", description = "Show the scoreboard",
        chineseDescription = "显示计分板", category = Category.HUD)
public class Scoreboard extends Module {
    public Scoreboard() {
        // TODO: Add editor
        setCanBeEdited(false);
        setX(new ScaledResolution(mc).getScaledWidth() - 30);
        setY(new ScaledResolution(mc).getScaledHeight() / 2);
    }
}
