package cn.stars.reversal.music;

import cn.stars.reversal.music.api.MusicAPI;
import cn.stars.reversal.music.thread.GetPlayListsThread;
import cn.stars.reversal.music.ui.MusicPlayerScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public class MusicManager {
    public MusicPlayerScreen screen;

    public void initGUI() {
        screen = new MusicPlayerScreen();

        // 如果已经登录，获取歌单列表
        if (MusicAPI.user.isLoggedIn()) {
            new GetPlayListsThread().start();
            MusicAPI.user.setAvatarTexture(new DynamicTexture(MusicUtil.downloadImage(MusicAPI.user.getAvatarUrl(), 200, 200)));
        }
    }
}
