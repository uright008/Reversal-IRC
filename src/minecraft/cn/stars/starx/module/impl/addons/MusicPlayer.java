/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.addons;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;

@ModuleInfo(name = "MusicPlayer", chineseName = "音乐播放器", description = "Play netease musics", chineseDescription = "播放网易云歌曲", category = Category.ADDONS)
public class MusicPlayer extends Module {
    @Override
    protected void onEnable() {
        if (StarX.hasJavaFX) mc.displayGuiScreen(StarX.musicManager.screen);
        else StarX.showMsg("未在你使用的Java版本上找到有效的JavaFX,无法使用MusicPlayer! 请安装后重试。");
        toggleModule();
    }
}
