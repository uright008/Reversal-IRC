package cn.stars.reversal.setting.impl;

import cn.stars.reversal.module.Module;
import cn.stars.reversal.setting.Setting;
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
