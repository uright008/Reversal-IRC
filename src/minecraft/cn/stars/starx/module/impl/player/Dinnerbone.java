/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.player;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;

@ModuleInfo(name = "Dinnerbone", chineseName = "玩家倒立", description = "Make the player upside down", chineseDescription = "使玩家模型倒立", category = Category.PLAYER)
public class Dinnerbone extends Module {
    private final BoolValue self = new BoolValue("Self", this, true);
}
