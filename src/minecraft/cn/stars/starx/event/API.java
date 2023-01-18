package cn.stars.starx.event;

import cn.stars.starx.GameInstance;
import cn.stars.starx.event.impl.PacketReceiveEvent;
import cn.stars.starx.event.impl.PacketSendEvent;
import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.TeleportEvent;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.player.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class API implements GameInstance {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static final List<Packet<?>> packet = new ArrayList();
    private static final TimeUtil stopwatch = new TimeUtil();

    private static Vec3 initialPosition;

    private static long nextSend;

    public static boolean canSend;

    public static int newKey;

    public static void onUpdate(final PreMotionEvent event) {

        if (packet.size() > 50) {
            while (!packet.isEmpty()) {
                PacketUtil.sendPacketWithoutEvent(packet.remove(0));
            }
        }
    }

    public static void onSend(final PacketSendEvent event) {

        final Packet<?> p = event.getPacket();

        if (p instanceof C03PacketPlayer) {
            final C03PacketPlayer wrapper = (C03PacketPlayer) p;

            if (mc.thePlayer.ticksExisted == 1) {
                initialPosition = new Vec3(wrapper.x + MathUtil.getRandom(-1000000, 1000000), wrapper.y + MathUtil.getRandom(-1000000, 1000000), wrapper.z + MathUtil.getRandom(-1000000, 1000000));
            } else if (mc.thePlayer.sendQueue.doneLoadingTerrain && initialPosition != null && mc.thePlayer.ticksExisted < 100) {
                wrapper.x = initialPosition.xCoord;
                wrapper.y = initialPosition.yCoord;
                wrapper.z = initialPosition.zCoord;
            }
        }

        if (p instanceof C0FPacketConfirmTransaction) {
            packet.add(p);
            event.setCancelled(true);
        }
        if (p instanceof C00PacketKeepAlive) {
            newKey++;
            if (newKey % 3 == 0) {
                packet.add(p);
                event.setCancelled(true);
            }
        }
    }

    public static void onReceive(final PacketReceiveEvent event) {
    }

    public static void onTeleport(final TeleportEvent event) {

        if (mc.thePlayer.sendQueue.doneLoadingTerrain) {
            if (mc.thePlayer.ticksExisted < 100) {
                for (int i = 0; i < 10; i++) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
                }

                PacketUtil.sendPacketWithoutEvent(event.getResponse());

                if (mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ()) < 3) {
                    event.setCancelled(true);
                }
            } else {
                event.setPosX(event.getPosX() - Double.MIN_VALUE);
                event.setPosZ(event.getPosZ() + Double.MIN_VALUE);
            }
        }
    }
}