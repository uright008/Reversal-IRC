package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.PacketReceiveEvent;
import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.math.TimeUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

@ModuleInfo(name = "TimeTraveller", description = "Travel the time to your own", category = Category.RENDER)
public class TimeTraveller extends Module {
    private final NumberValue time = new NumberValue("Time", this, 0, 0, 22999, 1);
    private final NumberValue timeSpeed = new NumberValue("Time Speed", this, 0, 0, 20, 0.1);
    private final ModeValue weather = new ModeValue("Weather", this, "None", "None", "Sun", "Rain", "Thunder", "Snow");
    private final NumberValue weatherSt = new NumberValue("Weather Strength", this, 1, 0, 1, 0.1);
    private final NumberValue lnSt = new NumberValue("Lightning Strength", this, 1, 0, 3, 0.1);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate)
            event.setCancelled(true);
        if (event.getPacket() instanceof S2BPacketChangeGameState && !weather.getMode().equals("None")) {
            S2BPacketChangeGameState p = ((S2BPacketChangeGameState) event.getPacket());
            if (p.getGameState() == 7 || p.getGameState() == 8) { // change weather packet
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        mc.theWorld.setWorldTime((long) ((time.getValue() + (timer.getElapsedTime() * timeSpeed.getValue()))));
        switch (weather.getMode()) {
            case "Sun":
                mc.theWorld.setRainStrength(0f);
                mc.theWorld.setThunderStrength(0f);
                break;

            case "Rain":
                mc.theWorld.setRainStrength((float) weatherSt.getValue());
                mc.theWorld.setThunderStrength(0f);
                break;

            case "Snow":
                // TODO: Snow Code is in World.java
                mc.theWorld.setRainStrength((float) weatherSt.getValue());
                mc.theWorld.setThunderStrength(0f);
                // TODO: The Code is in BiomeGenBase.java
           //     mc.theWorld.getBiomeGenForCoords(mc.thePlayer.getPosition()).setEnableSnow();
                // How
                break;

            case "Thunder":
                mc.theWorld.setRainStrength((float) weatherSt.getValue());
                mc.theWorld.setThunderStrength((float) lnSt.getValue());
                break;

        }
    }
}
