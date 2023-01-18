package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "NoBob", description = "Disable the walking bob effect", category = Category.RENDER)
public class NoBob extends Module {
    @Override
    public void onPreMotion(PreMotionEvent event) {
        mc.thePlayer.distanceWalkedModified = 0f;
    }
}
