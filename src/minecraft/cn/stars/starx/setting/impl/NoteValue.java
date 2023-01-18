package cn.stars.starx.setting.impl;

import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteValue extends Setting {
    public NoteValue(final String note, final Module parent) {
        this.name = note;
        parent.settings.add(this);
    }
}
