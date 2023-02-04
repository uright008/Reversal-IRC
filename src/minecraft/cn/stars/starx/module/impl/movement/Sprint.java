package cn.stars.starx.module.impl.movement;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.TextValue;
import cn.stars.starx.util.player.MoveUtil;

import java.awt.*;

@ModuleInfo(name = "Sprint", description = "Always sprint when you walking", category = Category.MOVEMENT)
public class Sprint extends Module {
    private final BoolValue toggle = new BoolValue("Toggle", this, false);
    private final TextValue aaa = new TextValue("Test", this, "Test");
    private boolean sprintState;

    @Override
    public void onPreMotion(PreMotionEvent event) {
        if (toggle.isEnabled()) {
            if (mc.gameSettings.keyBindSprint.isPressed()) {
                sprintState = !sprintState;
            }
            if (MoveUtil.isMoving() && sprintState && !isOtherKeyPressed()) mc.thePlayer.setSprinting(true);
        } else {
            if (MoveUtil.isMoving() && !isOtherKeyPressed()) mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        mc.fontRendererObj.drawString("State:" + sprintState, scaledWidth / 2, scaledHeight / 2, Color.WHITE.getRGB());
    }

    private boolean isOtherKeyPressed() {
        return !mc.gameSettings.keyBindForward.isKeyDown() && (mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown());
    }
}
