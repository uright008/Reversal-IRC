package cn.stars.starx.setting.impl;

import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
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
