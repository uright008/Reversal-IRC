package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "NoBob", chineseName = "移除摇晃", description = "Disable the walking bob effect",
        chineseDescription = "关闭走路时手臂的摇晃动画", category = Category.RENDER)
public class NoBob extends Module {
    @Override
    public void onPreMotion(PreMotionEvent event) {
        mc.thePlayer.distanceWalkedModified = 0f;
    }
}
