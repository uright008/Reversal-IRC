package cn.stars.reversal.event.impl;

import cn.stars.reversal.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

@Getter
@Setter
@AllArgsConstructor
public final class Render2DEvent extends Event {
    private float partialTicks;
    public ScaledResolution scaledResolution;
}
