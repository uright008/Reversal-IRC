package cn.stars.starx.module.impl.movement;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.TextValue;
import cn.stars.starx.util.player.MoveUtil;
import net.minecraft.potion.Potion;

import java.awt.*;

@ModuleInfo(name = "Sprint", description = "Always sprint when you walking", category = Category.MOVEMENT)
public class Sprint extends Module {
}
