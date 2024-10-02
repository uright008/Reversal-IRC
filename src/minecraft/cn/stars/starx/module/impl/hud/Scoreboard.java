package cn.stars.starx.module.impl.hud;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.List;

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
