package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.World;

@Getter
@Setter
@AllArgsConstructor
public class WorldEvent extends Event {
    private World world;
}
