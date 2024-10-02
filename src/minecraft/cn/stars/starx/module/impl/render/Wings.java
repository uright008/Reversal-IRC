package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.util.render.WingUtils;

@ModuleInfo(name = "Wings", chineseName = "翅膀", description = "Render a wing on your back",
        chineseDescription = "在你的背上渲染一个翅膀", category = Category.RENDER)
public class Wings extends Module {

    @Override
    public void onRender3D(Render3DEvent event) {
        WingUtils wingUtils = new WingUtils();
        if (mc.thePlayer != null && !mc.thePlayer.isDead && !mc.thePlayer.isInvisible()) {
            wingUtils.renderWings(event.getPartialTicks());
        }
    }
}
