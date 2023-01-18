package cn.stars.starx.module.impl.addons;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "NoClickDelay", description = "Remove the delay of clicking", category = Category.ADDONS)
public class NoClickDelay extends Module {
 //   private final NumberValue fixSpeed = new NumberValue("Fix Speed", this, 0, 0, 10, 1);

    @Override
    public void onPreMotion(PreMotionEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return;

            mc.leftClickCounter = 0;
        }
    }
}
