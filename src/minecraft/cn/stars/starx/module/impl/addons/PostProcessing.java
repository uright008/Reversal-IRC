package cn.stars.starx.module.impl.addons;

import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.blur.KawaseBloom;
import cn.stars.starx.util.render.blur.KawaseBlur;
import net.minecraft.client.shader.Framebuffer;

@ModuleInfo(name = "PostProcessing", description = "Add blur and bloom effects", chineseDescription = "增加模糊和阴影效果", category = Category.ADDONS)
public class PostProcessing extends Module
{
    private final BoolValue blur = new BoolValue("Blur", this, false);
    private final NumberValue iterations = new NumberValue("Blur Iterations", this, 2, 1, 8, 1);
    private final NumberValue offset = new NumberValue("Blur Offset", this, 3, 1, 10, 1);
    private final BoolValue bloom = new BoolValue("Bloom", this, false);
    private final NumberValue shadowRadius = new NumberValue("Bloom Iterations", this, 3, 1, 8, 1);
    private final NumberValue shadowOffset = new NumberValue("Bloom Offset", this, 1, 1, 10, 1);

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public void blurScreen() {
        if (!this.isEnabled()) return;
        if (blur.isEnabled()) {
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);

            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(false);
            Shader3DEvent event = new Shader3DEvent();
            event.call();
            doBlur();
            stencilFramebuffer.unbindFramebuffer();


            KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, (int) iterations.getValue(), (int) offset.getValue());

        }


        if (bloom.isEnabled()) {
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(false);

            Shader3DEvent event = new Shader3DEvent();
            event.call();
            doBloom();

            stencilFramebuffer.unbindFramebuffer();

            KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, (int) shadowRadius.getValue(), (int) shadowOffset.getValue());

        }
    }

    public void doBlur() {
        MODERN_BLUR_RUNNABLES.forEach(Runnable::run);
        MODERN_BLUR_RUNNABLES.clear();
    }

    public void doBloom() {
        MODERN_BLOOM_RUNNABLES.forEach(Runnable::run);
        MODERN_BLOOM_RUNNABLES.clear();
    }
}
