/*
 * Reversal Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.setting.Setting;
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
