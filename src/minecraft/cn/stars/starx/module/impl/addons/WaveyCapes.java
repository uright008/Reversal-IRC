package cn.stars.starx.module.impl.addons;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "WaveyCapes", description = "ewarser", category = Category.ADDONS)
public class WaveyCapes extends Module {
    public static int abc = 1;
    private final BoolValue wind = new BoolValue("Wind", this, true);
    private final ModeValue style = new ModeValue("Cape Style", this, "Smooth", "Blocky", "Smooth");
    private final ModeValue cm = new ModeValue("Cape Movement", this, "Vanilla", "Basic simulation", "Vanilla");
    private final NumberValue gravity = new NumberValue("Gravity", this,  25, 5, 32, 1);
    private final NumberValue hm = new NumberValue("Height Multiplier", this, 6, 4 , 16, 1);

}
