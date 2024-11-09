package cn.stars.reversal.util.player;

import cn.stars.reversal.util.misc.TransUtils;
import net.minecraft.potion.Potion;

public class PotionData {
    public final Potion potion;
    public int maxTimer = 0;
    public float animationX = 0;
    public final TransUtils translate;
    public final int level;
    public PotionData(Potion potion, TransUtils translate, int level) {
        this.potion = potion;
        this.translate = translate;
        this.level = level;
        this.animationX = translate.getX();
    }

    public float getAnimationX() {
        return animationX;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getMaxTimer() {
        return maxTimer;
    }
}