package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Scoreboard", description = "Show the scoreboard", category = Category.HUD)
public class Scoreboard extends Module {
    public Scoreboard() {
        // TODO: Add editor
        setCanBeEdited(false);
        setX(new ScaledResolution(mc).getScaledWidth() - 30);
        setY(new ScaledResolution(mc).getScaledHeight() / 2);
    }
}
