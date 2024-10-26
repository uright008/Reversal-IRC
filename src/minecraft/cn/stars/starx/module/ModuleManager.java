package cn.stars.starx.module;

import cn.stars.starx.setting.Setting;
import cn.stars.starx.util.misc.ClassUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public final class ModuleManager {
    public Module[] moduleList = new Module[0];
    ArrayList<Module> modulesToAdd = new ArrayList<>();


    private Module lastGetModule;
    private String lastGetModuleName;

    private String getSettingName, getSettingSettingName;
    private Setting getSettingSetting;

    private List<Module> enabledModules = new ArrayList<>();
    private boolean edited = true;


    /**
     * 遍历impl下所有继承Module的class并注册
     */
    public void registerModules() {
        for (Module module : ClassUtil.instantiateList(ClassUtil.resolvePackage(this.getClass().getPackage().getName() + ".impl", Module.class))) {
            moduleList = Arrays.stream(Stream.concat(Arrays.stream(moduleList), Stream.of(module))
                    .toArray(Module[]::new)).sorted(Comparator.comparing(m -> m.getModuleInfo().name())).toArray(Module[]::new);
        }

        for (Module module : moduleList) {
            module.onLoad();
        }
    }

    public void registerModules(Module[] modules) {
        moduleList = modules;

        for (Module module : moduleList) {
            module.onLoad();
        }
    }

    public List<Module> getEnabledModules() {
        if (edited) {
            enabledModules = new ArrayList<>();

            for (final Module module : moduleList) {
                if (module.isEnabled()) {
                    enabledModules.add(module);
                }
            }

            edited = false;
        }

        return enabledModules;
    }

    public Module getByClass(Class<? extends Module> MClass) {
        for (Module module : moduleList) {
            if (module.getClass() == MClass)
                return module;
        }
        return null;
    }

    public <T extends Module> T getModule(final Class<T> cls) {
        for (final Module m : this.moduleList) {
            if (m.getClass() == cls) {
                return (T)m;
            }
        }
        return null;
    }

    public List<Module> getModulesByCategory(final Category category) {
        final List<Module> modules = new ArrayList<>();
        for (final Module module : moduleList) {
            if (module.getModuleInfo().category() == category) modules.add(module);
        }
        return modules;
    }

    /**
     * This method returns the instance of a module gotten by the name.
     *
     * @param name The wanted module instances name.
     * @return The wanted module instance.
     */

    public Module getModule(final String name) {
        if (lastGetModuleName != null && lastGetModule != null) {
            if (lastGetModuleName.equalsIgnoreCase(name)) {
                return lastGetModule;
            }
        }

        for (final Module module : moduleList) {
            if (module.getModuleInfo().name().equalsIgnoreCase(name)) {
                lastGetModuleName = name;
                lastGetModule = module;
                return module;
            }
        }

        return null;
    }

    /**
     * This method gets the requested setting from the module given as a parameter.
     *
     * @param moduleName  The module the setting will be gotten from.
     * @param settingName The requested settings name.
     * @return The setting.
     */
    public Setting getSetting(final String moduleName, final String settingName) {
    /*    if (getSettingName != null && getSettingSettingName != null && getSettingSetting != null) {
            if (getSettingName.equals(moduleName) && getSettingSettingName.equals(settingName)) {
                return getSettingSetting;
            }
        } */

        for (final Module m : moduleList) {
            if (m.getModuleInfo().name().equalsIgnoreCase(moduleName)) {
                for (final Setting s : m.getSettings()) {
                    if (s.getName().equalsIgnoreCase(settingName)) {
                        getSettingName = moduleName;
                        getSettingSettingName = settingName;
                        getSettingSetting = s;

                        return s;
                    }
                }

            }
        }

        return null;
    }
}
