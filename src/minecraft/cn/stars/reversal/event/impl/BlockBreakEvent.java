package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Getter
@Setter
@AllArgsConstructor
public final class BlockBreakEvent extends Event {
    private final BlockPos blockPos;
    private final EnumFacing blockfacing;
}
