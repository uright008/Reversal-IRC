package cn.stars.starx.util.misc;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.setting.impl.TextValue;
import lombok.NonNull;

@NonNull
public class ModuleInstance {
    public static Module getModule(String moduleName) {
        return StarX.moduleManager.getModule(moduleName);
    }
    public static Module getModule(Class<? extends Module> clazz) {
        return StarX.moduleManager.getByClass(clazz);
    }
    public static Module getModuleClass(Class clazz) {
        return StarX.moduleManager.getModule(clazz);
    }
    public static ModeValue getMode(String moduleName, String settingName) throws ClassCastException {
        return (ModeValue) StarX.moduleManager.getSetting(moduleName, settingName);
    }
    public static BoolValue getBool(String moduleName, String settingName) throws ClassCastException {
        return (BoolValue) StarX.moduleManager.getSetting(moduleName, settingName);
    }
    public static TextValue getText(String moduleName, String settingName) throws ClassCastException {
        return (TextValue) StarX.moduleManager.getSetting(moduleName, settingName);
    }
    public static NumberValue getNumber(String moduleName, String settingName) throws ClassCastException {
        return (NumberValue) StarX.moduleManager.getSetting(moduleName, settingName);
    }
}
