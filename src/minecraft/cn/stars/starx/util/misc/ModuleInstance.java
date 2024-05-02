package cn.stars.starx.util.misc;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.setting.impl.TextValue;
import lombok.NonNull;

@NonNull
public class ModuleInstance {
    public static Module getModule(String moduleName) {
        return StarX.INSTANCE.moduleManager.getModule(moduleName);
    }
    public static Module getModule(Class<? extends Module> clazz) {
        return StarX.INSTANCE.moduleManager.getByClass(clazz);
    }
    public static ModeValue getMode(String moduleName, String settingName) {
        return (ModeValue) StarX.INSTANCE.moduleManager.getSetting(moduleName, settingName);
    }
    public static BoolValue getBool(String moduleName, String settingName) {
        return (BoolValue) StarX.INSTANCE.moduleManager.getSetting(moduleName, settingName);
    }
    public static TextValue getText(String moduleName, String settingName) {
        return (TextValue) StarX.INSTANCE.moduleManager.getSetting(moduleName, settingName);
    }
    public static NumberValue getNumber(String moduleName, String settingName) {
        return (NumberValue) StarX.INSTANCE.moduleManager.getSetting(moduleName, settingName);
    }
}
