package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class KeyEvent extends Event {
    private int key;
}
