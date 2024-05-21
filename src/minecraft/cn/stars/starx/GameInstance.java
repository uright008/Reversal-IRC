package cn.stars.starx;

import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.shader.RiseShaders;
import cn.stars.starx.util.shader.base.ShaderRenderType;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public interface GameInstance {
    Minecraft mc = Minecraft.getMinecraft();
    StarX instance = StarX.INSTANCE;

    TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    TTFFontRenderer comfortaaNigger = CustomFont.FONT_MANAGER.getFont("Comfortaa 26");
    TTFFontRenderer comfortaaBig = CustomFont.FONT_MANAGER.getFont("Comfortaa 32");
    TTFFontRenderer altoSmall = CustomFont.FONT_MANAGER.getFont("Biko 18");
    TTFFontRenderer altoCock = CustomFont.FONT_MANAGER.getFont("Biko 28");
    TTFFontRenderer skeetBig = CustomFont.FONT_MANAGER.getFont("Skeet 18");
    TTFFontRenderer museo = CustomFont.FONT_MANAGER.getFont("Museo 20");
    TTFFontRenderer eaves = CustomFont.FONT_MANAGER.getFont("Eaves 18");
    TTFFontRenderer skeet = CustomFont.FONT_MANAGER.getFont("SkeetBold 12");
    TTFFontRenderer gs = CustomFont.FONT_MANAGER.getFont("GoogleSans 18");
    TTFFontRenderer gsTitle = CustomFont.FONT_MANAGER.getFont("GoogleSans 24");
    TTFFontRenderer gsBig = CustomFont.FONT_MANAGER.getFont("GoogleSans 36");

    List<Runnable> UI_BLOOM_RUNNABLES = new ArrayList<>();
    List<Runnable> UI_POST_BLOOM_RUNNABLES = new ArrayList<>();
    List<Runnable> UI_RENDER_RUNNABLES = new ArrayList<>();
    List<Runnable> UI_BLUR_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_PRE_RENDER_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_BLUR_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_POST_BLOOM_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_OUTLINE_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_RENDER_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_POST_RENDER_RUNNABLES = new ArrayList<>();

    static void render2DRunnables(float partialTicks, boolean shaders) {

        NORMAL_PRE_RENDER_RUNNABLES.forEach(Runnable::run);

        if (shaders) {
            RiseShaders.OUTLINE_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_OUTLINE_RUNNABLES);
            RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);
        }

        NORMAL_RENDER_RUNNABLES.forEach(Runnable::run);

        NORMAL_POST_RENDER_RUNNABLES.forEach(Runnable::run);

        if (shaders) {
            RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);
        }

        UI_RENDER_RUNNABLES.forEach(Runnable::run);

        if (mc.currentScreen != null) {

            RiseShaders.UI_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, UI_BLOOM_RUNNABLES);

            RiseShaders.UI_POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, UI_POST_BLOOM_RUNNABLES);
        }

    }

    static void render3DRunnables(float partialTicks) {
        RiseShaders.OUTLINE_SHADER.run(ShaderRenderType.CAMERA, partialTicks, NORMAL_OUTLINE_RUNNABLES);
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.CAMERA, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);
        RiseShaders.UI_BLOOM_SHADER.run(ShaderRenderType.CAMERA, partialTicks, UI_BLOOM_RUNNABLES);
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.CAMERA, partialTicks, NORMAL_BLUR_RUNNABLES);
    }

    static void clearRunnables() {
        NORMAL_BLUR_RUNNABLES.clear();
        NORMAL_POST_BLOOM_RUNNABLES.clear();
        NORMAL_OUTLINE_RUNNABLES.clear();
        NORMAL_RENDER_RUNNABLES.clear();
        UI_BLOOM_RUNNABLES.clear();
        UI_RENDER_RUNNABLES.clear();
        NORMAL_PRE_RENDER_RUNNABLES.clear();
        NORMAL_POST_RENDER_RUNNABLES.clear();
        UI_POST_BLOOM_RUNNABLES.clear();
    }

    default Module getModule(final Class<? extends Module> clazz) {
        for (final Module module : StarX.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }

        return null;
    }

    public static Module getModuleS(final Class<? extends Module> clazz) {
        for (final Module module : StarX.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }

        return null;
    }


    default boolean getBoolean(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((BoolValue) setting).isEnabled();
            }
        }

        return false;
    }

    public static boolean getBooleanS(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : getModuleS(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((BoolValue) setting).isEnabled();
            }
        }

        return false;
    }

    default String getMode(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((ModeValue) setting).getMode();
            }
        }

        return null;
    }

    public static String getModeS(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : getModuleS(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((ModeValue) setting).getMode();
            }
        }

        return null;
    }

    default double getNumber(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((NumberValue) setting).getValue();
            }
        }

        return 0;
    }

    public static double getNumberS(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : getModuleS(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((NumberValue) setting).getValue();
            }
        }

        return 0;
    }
}
