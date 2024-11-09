package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

@Getter
@Setter
@AllArgsConstructor
public final class AttackEvent extends Event {
    public Entity target;
}