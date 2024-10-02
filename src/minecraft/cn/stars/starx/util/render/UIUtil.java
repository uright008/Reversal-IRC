package cn.stars.starx.util.render;

import cn.stars.starx.GameInstance;
import cn.stars.starx.ui.curiosity.CuriosityTextButton;
import cn.stars.starx.util.shader.RiseShaders;
import cn.stars.starx.util.shader.base.ShaderRenderType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import static cn.stars.starx.GameInstance.*;
import static cn.stars.starx.GameInstance.UI_BLOOM_RUNNABLES;

@Getter
@Setter
@UtilityClass
public class UIUtil {
    public void renderButton(CuriosityTextButton[] buttons, int mouseX, int mouseY, float partialTicks) {
        // blur
        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);
        // bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

        GameInstance.clearRunnables();

        for (CuriosityTextButton button : buttons) {
            button.draw(mouseX, mouseY, partialTicks);
        }

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();
    }

    public void onButtonClick(CuriosityTextButton[] buttons, int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            for (CuriosityTextButton menuButton : buttons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
    }
}
