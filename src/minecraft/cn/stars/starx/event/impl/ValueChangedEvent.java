/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import cn.stars.starx.module.Module;
import cn.stars.starx.setting.Setting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValueChangedEvent extends Event {
    public Module module;
    public Setting setting;
}
