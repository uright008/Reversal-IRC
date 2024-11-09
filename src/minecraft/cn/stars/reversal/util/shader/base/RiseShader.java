package cn.stars.reversal.util.shader.base;

import cn.stars.reversal.GameInstance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class RiseShader implements GameInstance {
    public boolean active;

    public abstract void run(ShaderRenderType type, float partialTicks, List<Runnable> runnable);

    public abstract void update();
}
