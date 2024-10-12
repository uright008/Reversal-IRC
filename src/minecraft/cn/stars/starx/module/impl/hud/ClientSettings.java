package cn.stars.starx.module.impl.hud;

import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "ClientSettings", chineseName = "主界面设置", description = "Some settings to change your hud.",
        chineseDescription = "客户端的整体视觉效果设置", category = Category.HUD)
public final class ClientSettings extends Module {
    private final ModeValue mode = new ModeValue("Theme", this, "Simple",
            "Minecraft", "StarX", "Modern", "Simple");
    private final ModeValue colorStyle = new ModeValue("Color Type", this, "Rainbow", "Rainbow", "Double", "Fade");
    private final ModeValue list = new ModeValue("List Animation", this, "StarX", "StarX", "Slide");
    private final NumberValue red = new NumberValue("Red", this, 19, 0, 255, 1);
    private final NumberValue green = new NumberValue("Green", this, 150, 0, 255, 1);
    private final NumberValue blue = new NumberValue("Blue", this, 255, 0, 255, 1);
    private final NumberValue red2 = new NumberValue("Red2", this, 19, 0, 255, 1);
    private final NumberValue green2 = new NumberValue("Green2", this, 150, 0, 255, 1);
    private final NumberValue blue2 = new NumberValue("Blue2", this, 255, 0, 255, 1);
    private final NumberValue indexTimes = new NumberValue("Index Times", this, 1, 1, 10, 0.1);
    private final NumberValue indexSpeed = new NumberValue("Index Speed", this, 1, 1, 5, 0.1);

    private final BoolValue chatBackground = new BoolValue("Chat Background", this, false);
    private final BoolValue enableNoti = new BoolValue("Show Notifications", this, false);
    private final BoolValue chineseDescription = new BoolValue("Chinese Description", this, true);
    private final BoolValue thunderHack = new BoolValue("ThunderHack", this, true);

    public static String theme;

    public static int red0, green0, blue0;
    public static int red1, green1, blue1;

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

        red1 = (int) red2.getValue();
        green1 = (int) green2.getValue();
        blue1 = (int) blue2.getValue();


        if (!isEnabled()) toggleModule();

        setSuffix(mode.getMode());
    }
}
