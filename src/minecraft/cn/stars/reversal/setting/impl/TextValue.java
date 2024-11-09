package cn.stars.reversal.setting.impl;

import cn.stars.reversal.module.Module;
import cn.stars.reversal.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextValue extends Setting {

    public String text;

    public TextValue(final String name, final Module parent, final String text) {
        this.name = name;
        parent.settings.add(this);
        this.text = text;
    }

    public String readText() {
        return text;
    }
}
