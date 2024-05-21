package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

@ModuleInfo(name = "ClientSettings", description = "Some settings to change your hud.", category = Category.HUD)
public final class ClientSettings extends Module {
    private final ModeValue mode = new ModeValue("Theme", this, "Rise Rainbow",
            "Rise Rainbow", "Rise", "Comfort",
            "Minecraft", "Minecraft Rainbow", "Never Lose", "Skeet", "StarX", "Modern");
    private final ModeValue list = new ModeValue("List Animation", this, "StarX", "StarX", "Slide");
    private final NumberValue red = new NumberValue("Red", this, 19, 0, 255, 1);
    private final NumberValue green = new NumberValue("Green", this, 150, 0, 255, 1);
    private final NumberValue blue = new NumberValue("Blue", this, 255, 0, 255, 1);

    private final BoolValue logo = new BoolValue("Logo", this, true);
    private final BoolValue outLine = new BoolValue("Arraylist Outline", this, false);
    private final BoolValue customHotbar = new BoolValue("Custom Hotbar", this, true);
    private final BoolValue smoothHotbar = new BoolValue("Smooth Hotbar", this, true);
    private final BoolValue armorHUD = new BoolValue("Armor HUD", this, false);
    private final BoolValue chatBackground = new BoolValue("Chat Background", this, false);
    private final BoolValue customChatFont = new BoolValue("Custom Chat Font", this, false);
    private final BoolValue enableNoti = new BoolValue("Show Notifications", this, false);
    private final ModeValue cape = new ModeValue("Cape", this, "Electric Sky", "Night", "Electric Sky");

    public static boolean customChat, customHotbarEnabled;

    public static String theme;

    public static int red0, green0, blue0;

    public ClientSettings() {
        setWidth(0);
        setHeight(0);
        setCanBeEdited(false);
    }

    @Override
    public void onUpdateAlways() {
        theme = mode.getMode();

        customChat = customChatFont.isEnabled();
        customHotbarEnabled = customHotbar.isEnabled();

        red0 = (int) red.getValue();
        green0 = (int) green.getValue();
        blue0 = (int) blue.getValue();

        outLine.hidden = !mode.is("Rice")
                && !mode.is("Rise Christmas")
                && !mode.is("Rise Blend")
                && !mode.is("Never Lose");


        if (!isEnabled()) toggleModule();
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);

        final int posX = (mc.displayWidth / (mc.gameSettings.guiScale * 2));
        final int x = posX + 80, y = sr.getScaledHeight() - 30;

        if (armorHUD.isEnabled() && !StarX.isDestructed) {
            RenderUtil.drawArmorHUD(x, y);
        }


    }

 //   @Override
 //   public void onBlur(final BlurEvent event) {
 //       IngameGUI.onBlur();
 //   }

 //   @Override
 //   public void onFadingOutline(final FadingOutlineEvent event) {
 //       if (outLine.isEnabled()) {
 //           IngameGUI.onFadeOutline();
 //       }
 //   }
}
