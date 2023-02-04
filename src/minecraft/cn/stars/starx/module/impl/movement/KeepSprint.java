package cn.stars.starx.module.impl.movement;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;

@ModuleInfo(name = "KeepSprint", description = "Let you not stop sprint when hitting", category = Category.MOVEMENT)
public class KeepSprint extends Module {
    private final NoteValue tip = new NoteValue("Using this feature may result in a ban!", this);
}
