/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.hud;

import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

@ModuleInfo(name = "PotionEffects", chineseName = "效果显示", description = "Display your potion effects on the screen", chineseDescription = "在屏幕上显示药水效果", category = Category.HUD)
public class PotionEffects extends Module {
    public PotionEffects() {
        setX(100);
        setY(100);
        setCanBeEdited(true);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX();
        int y = getY();
        for (PotionEffect potionEffect : mc.thePlayer.getActivePotionEffects()) {
            psb20.drawString(getPotionName(potionEffect), x, y, new Color(255,255,255,255).getRGB());
            y += 10;
        }
    }

    public String getPotionName(PotionEffect effect) {
        switch (effect.getPotionID()) {
            case 1: {
                return "Speed";
            }
            case 2: {
                return "Slowness";
            }
        }
        return "";
    }
}
