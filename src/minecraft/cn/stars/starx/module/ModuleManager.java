package cn.stars.starx.module;

import cn.stars.starx.StarX;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.util.StarXLogger;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class ModuleManager {
    /**
     * This is the list where we store our module instances.
     */
    public Module[] moduleList;

    /**
     * We cache some modules here to improve performance.
     */
    private Module lastGetModule;
    private String lastGetModuleName;

    private String getSettingName, getSettingSettingName;
    private Setting getSettingSetting;

    private List<Module> enabledModules = new ArrayList<>();
    private boolean edited = true;

    /**
     * When the class is instantiated we register all of our modules.
     * Its handled here as we do not need a new method for registering.
     */

    /**
     * This method returns all the current registered and enabled modules.
     *
     * @return all enabled modules.
     */
    public void registerModules(Module[] modules) {
        try {
            this.moduleList = modules;
        }
        catch (Exception e) {
            StarXLogger.error("An error has occurred while loading StarX." + e);
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
        if (getSettingName != null && getSettingSettingName != null && getSettingSetting != null) {
            if (getSettingName.equals(moduleName) && getSettingSettingName.equals(settingName)) {
                return getSettingSetting;
            }
        }

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
