package cn.stars.starx.event;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.*;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.hud.ClientSettings;
import cn.stars.starx.ui.clickgui.ClickGUI;
import cn.stars.starx.ui.clickgui.modern.ModernClickGUI;
import cn.stars.starx.ui.clickgui.strikeless.StrikeGUI;
import cn.stars.starx.ui.hud.Hud;
import cn.stars.starx.ui.notification.NotificationManager;
import cn.stars.starx.util.player.PlayerUtil;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public final class EventHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayer target = null;
    public static boolean canUpdateDeaths;

    public static void handle(final Event e) {
        final Module[] modules = StarX.INSTANCE.getModuleManager().getModuleList();

        if (e instanceof Render2DEvent) {
            final Render2DEvent event = ((Render2DEvent) e);

            RenderUtil.delta2DFrameTime = (System.currentTimeMillis() - RenderUtil.last2DFrame) / 10F;
            RenderUtil.last2DFrame = System.currentTimeMillis();

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onRender2D(event);
                }
            }
            NotificationManager.onRender2D(event);
        } else if (e instanceof Render3DEvent) {
            final Render3DEvent event = ((Render3DEvent) e);

            RenderUtil.delta3DFrameTime = (System.currentTimeMillis() - RenderUtil.last3DFrame) / 10F;
            RenderUtil.last3DFrame = System.currentTimeMillis();

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onRender3D(event);
                }
            }
        } else if (e instanceof BlurEvent) {
            final BlurEvent event = ((BlurEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlur(event);
                }
            }
        } else if (e instanceof FadingOutlineEvent) {
            final FadingOutlineEvent event = ((FadingOutlineEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onFadingOutline(event);
                }
            }
            Hud.onFadeOutline();
        } else if (e instanceof Shader3DEvent) {
            final Shader3DEvent event = ((Shader3DEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onShader3D(event);
                }
            }
        } else if (e instanceof PreBlurEvent) {
            final PreBlurEvent event = ((PreBlurEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPreBlur(event);
                }
            }
        } else if (e instanceof PacketReceiveEvent) {
            final PacketReceiveEvent event = ((PacketReceiveEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPacketReceive(event);
                }
            }
        } else if (e instanceof PacketSendEvent) {
            final PacketSendEvent event = ((PacketSendEvent) e);


            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPacketSend(event);
                }
            }

        } else if (e instanceof PostMotionEvent) {
            final PostMotionEvent event = ((PostMotionEvent) e);

            //Statistics
            if (target != null && !mc.theWorld.playerEntities.contains(target) && mc.thePlayer.getDistance(target.posX, mc.thePlayer.posY, target.posZ) < 30) {
                StarX.totalKills++;
                target = null;
            }

            if (mc.thePlayer.ticksExisted == 1) {
                PlayerUtil.worldChanges++;
            }

            final double d = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);

            StarX.amountOfModulesOn = 0;

            if (mc.thePlayer.onGround)
                StarX.distanceRan += d;

            if (mc.thePlayer.getHealth() <= 0) {
                if (canUpdateDeaths) {
                    StarX.totalDeaths++;
                    canUpdateDeaths = false;
                }
            } else
                canUpdateDeaths = true;

            for (final Module module : modules) {
                if (module.isEnabled()) {

                    StarX.amountOfModulesOn++;

                    module.onPostMotion(event);
                }
            }
        } else if (e instanceof PreMotionEvent) {
            final PreMotionEvent event = ((PreMotionEvent) e);

            /* Used to reset PlayerUtil.isOnServer() */
            if (mc.thePlayer.ticksExisted == 1) {
                PlayerUtil.serverResponses.clear();
                PlayerUtil.sentEmail = false;
            }

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPreMotion(event);
                }

                /* Calls events that are always used called whether the module is on or not*/
                if (mc.currentScreen instanceof ClickGUI || mc.currentScreen instanceof StrikeGUI || mc.currentScreen instanceof ModernClickGUI) {
                    module.onUpdateAlwaysInGui();
                }
                module.onUpdateAlways();
            }
        } else if (e instanceof TickEvent) {
            final TickEvent event = (TickEvent) e;
            for (final Module module : modules) {
                if (module.isEnabled()) {

                    module.onTick(event);
                }
            }
        } else if (e instanceof ClickEvent) {
            final ClickEvent event = (ClickEvent) e;
            for (final Module module : modules) {
                if (module.isEnabled()) {

                    module.onClick(event);
                }
            }
        } else if (e instanceof KeyEvent) {
            final KeyEvent event = ((KeyEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onKey(event);
                }

                if (module.getKeyBind() == event.getKey()) {
                    module.toggleModule();
                }
            }
        } else if (e instanceof StrafeEvent) {
            final StrafeEvent event = ((StrafeEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onStrafe(event);
                }
            }
        } else if (e instanceof CanPlaceBlockEvent) {
            final CanPlaceBlockEvent event = ((CanPlaceBlockEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onCanPlaceBlock(event);
                }
            }
        } else if (e instanceof BlockBreakEvent) {
            final BlockBreakEvent event = ((BlockBreakEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlockBreak(event);
                }
            }
        } else if (e instanceof AlphaEvent){
            final AlphaEvent event = (AlphaEvent) e;

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onAlpja(event);
                }
            }
        } else if (e instanceof AttackEvent) {
            final AttackEvent event = ((AttackEvent) e);

            //Statistics
            final Entity entity = event.getTarget();
            if (entity instanceof EntityPlayer) {
                target = (EntityPlayer) entity;
            }

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onAttack(event);
                }
            }
        } else if (e instanceof MoveButtonEvent) {
            final MoveButtonEvent event = ((MoveButtonEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onMoveButton(event);
                }
            }
        } else if (e instanceof MoveEvent) {
            final MoveEvent event = ((MoveEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onMove(event);
                }
            }
        } else if (e instanceof WorldEvent) {
            final WorldEvent event = ((WorldEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onWorld(event);
                }
            }
        } else if (e instanceof UpdateEvent) {
            final UpdateEvent event = ((UpdateEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onUpdate(event);
                }
            }
        } else if (e instanceof BlockCollideEvent) {
            final BlockCollideEvent event = ((BlockCollideEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlockCollide(event);
                }
            }
        } else if (e instanceof TeleportEvent) {
            final TeleportEvent event = ((TeleportEvent) e);


            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onTeleport(event);
                }
            }
        }

        final int r = ClientSettings.red0;
        final int g = ClientSettings.green0;
        final int b = ClientSettings.blue0;

        final Color bright = new Color(Math.min(r + 26, 255), Math.min(g + 45, 255), Math.min(b + 13, 255));

        StarX.CLIENT_THEME_COLOR = new Color(r, g, b, 255).getRGB();
        StarX.CLIENT_THEME_COLOR_BRIGHT = bright.hashCode();
        StarX.CLIENT_THEME_COLOR_BRIGHT_COLOR = bright;
    }
}
