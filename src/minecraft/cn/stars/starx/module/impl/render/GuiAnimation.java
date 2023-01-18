package cn.stars.starx.module.impl.render;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "GuiAnimation", description = "Gives Guis an animation", category = Category.RENDER)
public class GuiAnimation extends Module {
    private final BoolValue clickGui = new BoolValue("ClickGui", this, true);
    public static boolean clickGuiValue;
    private final BoolValue inventories = new BoolValue("Inventories", this, true);
    public static boolean inventoriesValue;

    private final NumberValue startingSize = new NumberValue("Starting Size", this, 0.1, 0, 1, 0.01);
    public static float startingSizeValue;
    private final NumberValue speed = new NumberValue("Speed", this, 0.1, 0.01, 1, 0.01);
    public static float speedValue;

    @Override
    public void onUpdateAlwaysInGui() {
        super.onUpdateAlwaysInGui();

        clickGuiValue = clickGui.isEnabled();
        inventoriesValue = inventories.isEnabled();
        startingSizeValue = (float) startingSize.getValue();
        speedValue = (float) speed.getValue();
    }
}
