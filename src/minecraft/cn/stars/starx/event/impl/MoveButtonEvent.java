package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class MoveButtonEvent extends Event {
    private Button left;
    private Button right;
    private Button backward;
    private Button forward;
    private boolean sneak;
    private boolean jump;

    public void setForward(final boolean forward) {
        this.getForward().button = forward;
    }

    public void setBackward(final boolean backward) {
        this.getBackward().button = backward;
    }

    public void setLeft(final boolean left) {
        this.getLeft().button = left;
    }

    public void setRight(final boolean right) {
        this.getRight().button = right;
    }
}
