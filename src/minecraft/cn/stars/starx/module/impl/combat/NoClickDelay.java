package cn.stars.starx.module.impl.combat;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "NoClickDelay", chineseName = "移除点击延迟", description = "Remove the delay of clicking",
        chineseDescription = "移除特殊情况下出现的鼠标点击延迟", category = Category.COMBAT)
public class NoClickDelay extends Module {

    @Override
    public void onPreMotion(PreMotionEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return;

            mc.leftClickCounter = 0;
        }
    }
}
