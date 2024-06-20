package cn.stars.starx.module.impl.render;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "ChunkAnimator", description = "Make the chunk loading progress with animation.",
        chineseDescription = "为区块加载增加动画", category = Category.RENDER)
public class ChunkAnimator extends Module {
    private final NumberValue type = new NumberValue("Type", this, 0, 0, 4, 1);
}
