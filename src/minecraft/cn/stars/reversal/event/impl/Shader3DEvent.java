package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Shader3DEvent extends Event {
    boolean isBloom;
}
