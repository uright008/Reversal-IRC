/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.combat;

import cn.stars.starx.event.impl.ClickEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.misc.SoundUtil;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "ClickSound", chineseName = "点击声音", description = "Play sound when you click", chineseDescription = "当你点击时播放声音", category = Category.COMBAT)
public class ClickSound extends Module {
    private final ModeValue type = new ModeValue("Type", this, "Normal", "Normal", "Jitter", "Double");
    private final NumberValue volume = new NumberValue("Volume", this, 0.5, 0.1, 2, 0.1);
    private final NumberValue variation = new NumberValue("Variation", this, 5, 0, 100, 1);

    @Override
    public void onClick(ClickEvent event) {
        if (event.getType() != ClickEvent.ClickType.MIDDLE) {
            switch (type.getMode()) {
                case "Normal":
                    SoundUtil.playSound("starx.click.nc", (float) volume.getValue(), RandomUtils.nextFloat(1.0F, 1 + (float) variation.getValue() / 100f));
                    break;
                case "Jitter":
                    SoundUtil.playSound("starx.click.jc", (float) volume.getValue(), RandomUtils.nextFloat(1.0F, 1 + (float) variation.getValue() / 100f));
                    break;
                case "Double":
                    SoundUtil.playSound("starx.click.dbc", (float) volume.getValue(), RandomUtils.nextFloat(1.0F, 1 + (float) variation.getValue() / 100f));
                    break;
            }
        }
    }
}
