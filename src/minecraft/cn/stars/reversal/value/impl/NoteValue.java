package cn.stars.reversal.value.impl;

import cn.stars.reversal.module.Module;
import cn.stars.reversal.value.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteValue extends Value {
    public NoteValue(final String note, final Module parent) {
        this.name = note;
        parent.settings.add(this);
        parent.settingsMap.put(name.toLowerCase(), this);
    }
}
