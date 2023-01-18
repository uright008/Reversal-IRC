package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.BlockPos;

@Getter
@Setter
@AllArgsConstructor
public final class BlockBreakEvent extends Event {
    private final BlockPos blockPos;
}
