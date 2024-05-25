package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.AttackEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.event.impl.WorldEvent;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "SessionInfo", description = "Show your game stats", category = Category.HUD)
public class SessionInfo extends Module {
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
    private final BoolValue shadow = new BoolValue("Shadow", this, true);
    private final BoolValue line = new BoolValue("Line", this, false);
    private TimeUtil timer = new TimeUtil();
    int second = 0;
    int minute = 0;
    int hour = 0;
    int killed = 0;
    ArrayList<EntityLivingBase> attackedEntityList = new ArrayList<>();
    ArrayList<EntityLivingBase> attackedEntityListToRemove = new ArrayList<>();
    public SessionInfo() {
        setCanBeEdited(true);
        setWidth(180);
        setHeight(80);
        setX(100);
        setY(100);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!getModule("HUD").isEnabled()) return;
        int x = getX() + 4;
        int y = getY() + 4;
        TTFFontRenderer psb = CustomFont.FONT_MANAGER.getFont("PSB 20");
        TTFFontRenderer psm = CustomFont.FONT_MANAGER.getFont("PSM 18");
        TTFFontRenderer icon = CustomFont.FONT_MANAGER.getFont("Mi 24");
        TTFFontRenderer iconSmall = CustomFont.FONT_MANAGER.getFont("Mi 18");
        Color color = rainbow.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250,250,250,200);


        RenderUtil.roundedRectangle(x - 3, y - 3, 150, 65, 4, new Color(0,0,0, 80));
        RenderUtil.roundedOutlineRectangle(x - 3, y - 3, 150, 65, 3, 1, color);

        if (line.isEnabled()) RenderUtil.rect(x - 2, y + 10, 148, 0.7, color);

        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                RenderUtil.roundedOutlineRectangle(x - 3, y - 3, 150, 65, 3, 1, color);
                if (line.isEnabled()) RenderUtil.rect(x - 2, y + 10, 148, 0.7, color);
            });
        }

        // 顶部
        psb.drawString("Session Info", x + 15, y - 0.7f, color.getRGB());
        icon.drawString("I", x, y - 0.3f, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                psb.drawString("Session Info", x + 15, y - 0.7f, color.getRGB());
                icon.drawString("I", x, y - 0.3f, color.getRGB());
            });
        }

        // 第一行 游玩时间
        updatePlayTime();
        String playtime = hour + "h " + minute + "m " + second + "s";
        iconSmall.drawString("e", x , y + 12, color.getRGB());
        psm.drawString("Play Time", x + 12, y + 11, color.getRGB());
        psm.drawString(playtime, x + 145 - psm.getWidth(playtime), y + 11, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                iconSmall.drawString("e", x , y + 12, color.getRGB());
                psm.drawString("Play Time", x + 12, y + 11, color.getRGB());
                psm.drawString(playtime, x + 145 - psm.getWidth(playtime), y + 11, color.getRGB());
            });
        }

        // 第二行 击杀数量
        String kills = String.valueOf(killed);
        iconSmall.drawString("a", x , y + 22, color.getRGB());
        psm.drawString("Killed", x + 12, y + 21, color.getRGB());
        psm.drawString(kills, x + 145 - psm.getWidth(kills), y + 21, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                iconSmall.drawString("a", x , y + 22, color.getRGB());
                psm.drawString("Killed", x + 12, y + 21, color.getRGB());
                psm.drawString(kills, x + 145 - psm.getWidth(kills), y + 21, color.getRGB());
            });
        }

        // 第三行 HurtTime
        String hurtTime = String.valueOf(mc.thePlayer.hurtTime);
        iconSmall.drawString("c", x , y + 32, color.getRGB());
        psm.drawString("HurtTime", x + 12, y + 31, color.getRGB());
        psm.drawString(hurtTime, x + 145 - psm.getWidth(hurtTime), y + 31, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                iconSmall.drawString("c", x , y + 32, color.getRGB());
                psm.drawString("HurtTime", x + 12, y + 31, color.getRGB());
                psm.drawString(hurtTime, x + 145 - psm.getWidth(hurtTime), y + 31, color.getRGB());
            });
        }

        // 第四行 速度
        String speed = String.valueOf(MathUtil.round(mc.thePlayer.getSpeed(), 1));
        iconSmall.drawString("b", x , y + 42, color.getRGB());
        psm.drawString("Speed", x + 12, y + 41, color.getRGB());
        psm.drawString(speed, x + 145 - psm.getWidth(speed), y + 41, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                iconSmall.drawString("b", x , y + 42, color.getRGB());
                psm.drawString("Speed", x + 12, y + 41, color.getRGB());
                psm.drawString(speed, x + 145 - psm.getWidth(speed), y + 41, color.getRGB());
            });
        }

        // 第五行 血量
        String health = String.valueOf(MathUtil.round(mc.thePlayer.getHealth(), 1));
        iconSmall.drawString("s", x , y + 52, color.getRGB());
        psm.drawString("HP", x + 12, y + 51, color.getRGB());
        psm.drawString(health, x + 145 - psm.getWidth(health), y + 51, color.getRGB());
        if (shadow.isEnabled()) {
            // Shadow
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                iconSmall.drawString("s", x , y + 52, color.getRGB());
                psm.drawString("HP", x + 12, y + 51, color.getRGB());
                psm.drawString(health, x + 145 - psm.getWidth(health), y + 51, color.getRGB());
            });
        }
    }

    // 计时器
    private void updatePlayTime() {

        if (mc.theWorld != null) {

            if (timer.hasReached(1000)) {

                second += 1;
                timer.reset();
            }
            if (second == 60) {
                minute += 1;
                second = 0;
            }
            if (minute == 60) {
                hour += 1;
                minute = 0;
            }
        }
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        attackedEntityList.forEach(i -> {
            if (i.isDead) {
                killed += 1;
                attackedEntityListToRemove.add(i);
            }
        });
        if (!attackedEntityListToRemove.isEmpty()) {
            attackedEntityList.removeAll(attackedEntityListToRemove);
            attackedEntityListToRemove.clear();
        }
    }

    @Override
    public void onAttack(AttackEvent event) {
        Entity target = event.getTarget();

        if (target instanceof EntityLivingBase) {
            if (!attackedEntityList.contains(target)) {
                attackedEntityList.add((EntityLivingBase) target);
            }
        }
    }

    @Override
    public void onWorld(WorldEvent event) {
        attackedEntityList.clear();
    }
}
