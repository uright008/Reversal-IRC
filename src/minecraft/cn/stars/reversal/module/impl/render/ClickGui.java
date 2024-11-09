/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package cn.stars.reversal.module.impl.render;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.event.impl.PreMotionEvent;
import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.setting.impl.BoolValue;
import cn.stars.reversal.setting.impl.ModeValue;
import cn.stars.reversal.setting.impl.NumberValue;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGui", chineseName = "点击界面", description = "Opens a Gui where you can toggle modules and change their settings",
        chineseDescription = "显示一个可以让你管理功能的界面", category = Category.RENDER, defaultKey = Keyboard.KEY_RSHIFT)
public final class ClickGui extends Module {

    private final ModeValue mode = new ModeValue("Mode", this, "Modern", "Modern", "MomoTalk");

    private final ModeValue theme = new ModeValue("Theme", this, "Deep Blue", "Deep Blue",
            "Rural Amethyst", "Rustic Desert", "Orchid Aqua", "Alyssum Pink", "Sweet Grape Vine", "Disco");
    private final BoolValue transparency = new BoolValue("Transparency", this, false);
    private final BoolValue blur = new BoolValue("Blur", this, false);
    private final NumberValue scale = new NumberValue("Scale", this, 0.7, 0.3, 1, 0.05);

    private final NumberValue speed = new NumberValue("Scroll Speed", this, 2.0, 0.5, 10.0, 1.0);

    public static float speedValue;


    @Override
    public void onUpdateAlwaysInGui() {
        transparency.hidden = !mode.is("Reversal");
        blur.hidden = !mode.is("Reversal");

        speedValue = (float)(speed.getValue());
    }

    @Override
    protected void onEnable() {
        switch (mode.getMode()) {
            case "MomoTalk": {
                mc.displayGuiScreen(Reversal.mmtClickGUI);
                break;
            }

            case "Modern": {
                mc.displayGuiScreen(Reversal.modernClickGUI);
                break;
            }
        }

        toggleModule();
        Reversal.saveAll();
    }

    @Override
    protected void onDisable() {
        Reversal.saveAll();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        //Invmove for clickgui
   /*    if (!(mc.currentScreen instanceof GuiChat)
                && !Objects.requireNonNull(Reversal.moduleManager.getModule("InvMove")).isEnabled()) {
            for (final KeyBinding a : affectedBindings) {
                a.setKeyPressed(GameSettings.isKeyDown(a));
            }
        } */
    }
}
