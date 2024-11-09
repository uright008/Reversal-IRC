package cn.stars.reversal.setting.impl;

import cn.stars.reversal.module.Module;
import cn.stars.reversal.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoolValue extends Setting {

    public boolean enabled;

    public BoolValue(final String name, final Module parent, final boolean enabled) {
        this.name = name;
        parent.settings.add(this);
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
