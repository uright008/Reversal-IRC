package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.util.render.WingUtils;

@ModuleInfo(name = "Wings", description = "Render a wing on your back", category = Category.RENDER)
public class Wings extends Module {
    @Override
    public void onRender3DEvent(Render3DEvent event) {
        WingUtils wingUtils = new WingUtils();
        wingUtils.renderWings(event.getPartialTicks());
    }
}
