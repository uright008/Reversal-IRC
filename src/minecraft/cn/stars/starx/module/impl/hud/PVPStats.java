package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.*;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.font.MFont;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "PVPStats", chineseName = "战斗数据", description = "Show your pvp stats",
        chineseDescription = "显示你的战斗数据", category = Category.HUD)
public class PVPStats extends Module {
    private final BoolValue warnPearl = new BoolValue("Warn Pearl", this, false);

    private final MFont icon = FontManager.getMi(24);

    private EntityPlayer target = null;
    private int myHits = 0;
    private int enemyHits = 0;
    private boolean isPearlAppeared = false;

    TimeUtil timeUtil = new TimeUtil();

    public PVPStats() {
        setCanBeEdited(true);
        setWidth(180);
        setHeight(100);
        setX(100);
        setY(100);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 4;
        int y = getY() + 4;

        if (target != null) {
            RenderUtil.roundedRectangle(x - 4, y - 4, 148, 48, 3f, ColorUtil.empathyColor());
            regular16.drawString(">  Current", x + 2, y + 11, new Color(250, 250, 250, 200).getRGB());
            regular16.drawString(target.getName(), x + 143 - regular16.width(target.getName()), y + 11, new Color(250, 250, 250, 200).getRGB());

            regular16.drawString(">  My Hits", x + 2, y + 23, new Color(250, 250, 250, 200).getRGB());
            regular16.drawString(myHits / 2 + "", x + 143 - regular16.width(myHits / 2 + ""), y + 23, new Color(250, 250, 250, 200).getRGB());

            regular16.drawString(">  Enemy Hits", x + 2, y + 35, new Color(250, 250, 250, 200).getRGB());
            regular16.drawString(enemyHits / 2 + "", x + 143 - regular16.width(enemyHits / 2 + ""), y + 35, new Color(250, 250, 250, 200).getRGB());
        } else {
            RenderUtil.roundedRectangle(x - 4, y - 4, 148, 24, 3f, ColorUtil.empathyColor());
            regular16.drawString(">  Not In Combat...", x + 2, y + 11, new Color(250, 250, 250, 200).getRGB());
        }

        // 顶部
        regular20Bold.drawString("PVP Stats", x + 15, y, new Color(250, 250, 250, 200).getRGB());
        icon.drawString("I", x, y - 0.3f, new Color(250, 250, 250, 200).getRGB());
        RenderUtil.roundedRectangle(x - 4.5, y - 1.5, 1.5, regular16.height() - 2.5, 1f, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (timeUtil.hasReached(5000L)) {
            isPearlAppeared = false;
            timeUtil.reset();
        }

        if (target != null) {
            if (mc.thePlayer.hurtTime >= 9) enemyHits = enemyHits + 2;
            if (target.hurtTime >= 9) myHits++;
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityEnderPearl && !isPearlAppeared && warnPearl.isEnabled()) {
                    StarX.notificationManager.registerNotification("Pearl detected! (Position: " + entity.posX + "," + entity.posY + "," + entity.posZ + ")", "PVPStats", 2000L, NotificationType.WARNING);
                    isPearlAppeared = true;
                }
            }
        }
    }

    @Override
    public void onAttack(AttackEvent event) {
        if (event.target instanceof EntityPlayer) {
            if (event.target != target) {
                resetStats();
                target = (EntityPlayer) event.target;
            }
        }
    }

    @Override
    public void onShader3D(Shader3DEvent event) {
        int x = getX() + 4;
        int y = getY() + 4;
        if (target != null) {
            RenderUtil.roundedRectangle(x - 4, y - 4, 148, 48, 3f, ColorUtil.empathyGlowColor());
        } else {
            RenderUtil.roundedRectangle(x - 4, y - 4, 148, 24, 3f, ColorUtil.empathyGlowColor());
        }
        RenderUtil.roundedRectangle(x - 4.5, y - 1.5, 1.5, regular16.height() - 2.5, 1f, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
    }

    @Override
    protected void onEnable() {
        resetStats();
    }

    @Override
    public void onWorld(WorldEvent event) {
        resetStats();
    }

    private void resetStats() {
        target = null;
        isPearlAppeared = false;
        myHits = 0;
        enemyHits = 0;
        timeUtil.reset();
    }
}
