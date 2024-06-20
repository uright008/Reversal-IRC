package cn.stars.starx.module.impl.player;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;

@ModuleInfo(name = "HealthWarn", description = "Give a warning to you on low health",
        chineseDescription = "在低血量时向你发送警告", category = Category.PLAYER)
public class HealthWarn extends Module {
    private final NumberValue health = new NumberValue("Health", this, 10, 1, 20, 1);
    private boolean canWarn;
    @Override
    public void onEnable() {
        canWarn = true;
    }
    @Override
    public void onDisable() {
        canWarn = true;
    }
    @Override
    public void onPreMotion(PreMotionEvent event) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            if (canWarn) {
                registerNotification("You dont have enough health!","HP Warning", NotificationType.WARNING, 3000);
                canWarn = false;
            }
        } else {
            canWarn = true;
        }
    }
}
