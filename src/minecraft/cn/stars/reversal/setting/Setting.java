package cn.stars.reversal.setting;

import cn.stars.reversal.util.animation.rise.Animation;
import cn.stars.reversal.util.animation.rise.Easing;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Setting {
    public String name;
    public boolean hidden;

    public float guiX;
    public float guiY;

    public Animation yAnimation = new Animation(Easing.EASE_OUT_EXPO, 500);
}
