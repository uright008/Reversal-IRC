package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.blur.KawaseBloom;
import cn.stars.starx.util.render.blur.KawaseBlur;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;

import java.awt.*;

@ModuleInfo(name = "ClientSettings", description = "Some settings to change your hud.",
        chineseDescription = "客户端的整体视觉效果设置", category = Category.HUD)
public final class ClientSettings extends Module {
    private final ModeValue mode = new ModeValue("Theme", this, "Modern",
            "Rise Rainbow", "Rise", "Comfort",
            "Minecraft", "Minecraft Rainbow", "Never Lose", "Skeet", "StarX", "Modern");
    private final ModeValue list = new ModeValue("List Animation", this, "StarX", "StarX", "Slide");
    private final NumberValue red = new NumberValue("Red", this, 19, 0, 255, 1);
    private final NumberValue green = new NumberValue("Green", this, 150, 0, 255, 1);
    private final NumberValue blue = new NumberValue("Blue", this, 255, 0, 255, 1);

    private final BoolValue chatBackground = new BoolValue("Chat Background", this, false);
    private final BoolValue enableNoti = new BoolValue("Show Notifications", this, false);
    private final BoolValue chineseDescription = new BoolValue("Chinese Description", this, true);
    private final BoolValue blur = new BoolValue("Blur", this, false);

    public static String theme;
    ScaledResolution sr = new ScaledResolution(mc);

    public static int red0, green0, blue0;

    public ClientSettings() {
        setWidth(0);
        setHeight(0);
        setCanBeEdited(false);
    }

    @Override
    public void onUpdateAlways() {
        theme = mode.getMode();

        red0 = (int) red.getValue();
        green0 = (int) green.getValue();
        blue0 = (int) blue.getValue();


        if (!isEnabled()) toggleModule();

        setSuffix(mode.getMode());
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);


    }
}
