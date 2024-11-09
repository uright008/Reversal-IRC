/*
 * Reversal Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.reversal.module.impl.combat;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.event.impl.AttackEvent;
import cn.stars.reversal.event.impl.UpdateEvent;
import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.util.math.MathUtil;
import cn.stars.reversal.util.math.TimeUtil;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "ExperimentReachDistanceChecker", chineseName = "实验性攻击距离检测器", description = "For test test test", chineseDescription = "测试测试测试", category = Category.COMBAT)
public class ExperimentReachDistanceChecker extends Module {
    EntityPlayer target;
    TimeUtil timeUtil = new TimeUtil();
    int countInSecond = 0;

    @Override
    protected void onEnable() {
        Reversal.showMsg("§c§l警告!!! 功能仅供娱乐!!!");
        Reversal.showMsg("§c§l数据可能由于延迟等原因与实际不符!");
        Reversal.showMsg("§c§l请勿用于实际评估!");
        Reversal.showMsg("§e计算过程: dist - 0.3 - (my_ping + his_ping) / 1000 * dist");
    }

    @Override
    public void onAttack(AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer && event.getTarget() != target) {
            target = (EntityPlayer) event.getTarget();
            Reversal.showMsg("§a锁定目标: §e§l" + event.getTarget().getName());
        }
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (target != null && (mc.thePlayer.getSmoothDistanceToEntity(target) >= 8 || target.isDead || target.isInvisible())) {
            Reversal.showMsg("§e移除选定目标!");
            target = null;
            return;
        }
        if (target != null && mc.thePlayer.hurtTime >= 9) {
            double dist = mc.thePlayer.getSmoothDistanceToEntity(target) - 0.3; // 获取的是玩家中心位置 碰撞箱为0.6x0.6
            double ping_dist = dist - (mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getResponseTime() + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime()) / 1000.0 * dist; // ping predict
            double final_dist = MathUtil.round(Math.abs(ping_dist), 2);
            if (countInSecond <= 1) {
                if (final_dist < 0) {
                    Reversal.showMsg("§e受到攻击! 距离: §e§l" + final_dist + " §c§lWTF???");
                } else if (final_dist > 0 && final_dist <= 3) {
                    Reversal.showMsg("§e受到攻击! 距离: §e§l" + final_dist + " §a§l正常");
                } else if (final_dist > 3 && final_dist <= 3.5) {
                    Reversal.showMsg("§e受到攻击! 距离: §e§l" + final_dist + " §c§l666这个入是桂");
                } else {
                    Reversal.showMsg("§e受到攻击! 距离: §e§l" + final_dist + " §4§l?");
                }
                countInSecond = 0;
            } else {
                Reversal.showMsg("§e受到攻击! 距离: §e§l" + final_dist + " §e§lROD/PING?");
                countInSecond = 0;
            }
            countInSecond++;
        }
        if (timeUtil.hasReached(800)) {
            countInSecond = 0;
            timeUtil.reset();
        }
    }
}
