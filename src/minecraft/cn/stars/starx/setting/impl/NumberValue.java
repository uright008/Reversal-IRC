package cn.stars.starx.setting.impl;

import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public final class NumberValue extends Setting {
    public double value, minimum, maximum, increment;
    public double renderPercentage, percentage;
    List<String> replacements;

    public NumberValue(final String name, final Module parent, final double value, final double minimum, final double maximum, final double increment) {
        this.name = name;
        parent.settings.add(this);
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public NumberValue(final String name, final Module parent, final double value, final double minimum, final double maximum, final double increment, final String... replacements) {
        this.name = name;
        parent.settings.add(this);
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.replacements = Arrays.asList(replacements);
    }

    public float getFloat() {
        return (float) this.value;
    }
    public double getDouble() {
        return (double) this.value;
    }
    public int getInt() {
        return (int) this.value;
    }
}
