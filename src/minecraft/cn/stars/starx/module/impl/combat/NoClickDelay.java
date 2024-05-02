package cn.stars.starx.module.impl.combat;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "NoClickDelay", description = "Remove the delay of clicking", category = Category.COMBAT)
public class NoClickDelay extends Module {

    @Override
    public void onPreMotion(PreMotionEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return;

            mc.leftClickCounter = 0;
        }
    }
}
