package cn.stars.starx.module.impl.addons;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;

@ModuleInfo(name = "Test", description = "A test module", category = Category.ADDONS)
public class Test extends Module {
    public final BoolValue testMsg = new BoolValue("TestMsg", this, false);
}
