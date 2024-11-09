package cn.stars.reversal.music.ui;

import net.minecraft.util.ResourceLocation;

public enum MusicCategory {
    RECOMMENDED("home.png"),
    LIKED("favorite.png");

    public final ResourceLocation icon;
    MusicCategory(String iconFile) {
        icon = new ResourceLocation("reversal/images/music/" + iconFile);
    }
}
