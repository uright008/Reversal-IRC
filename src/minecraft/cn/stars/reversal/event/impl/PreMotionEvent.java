package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class PreMotionEvent extends Event {
    private float yaw, pitch;
    private boolean ground;
    private double x, y, z;
}
