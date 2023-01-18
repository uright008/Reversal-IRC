package cn.stars.starx.event.impl;

import cn.stars.starx.event.Event;
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
