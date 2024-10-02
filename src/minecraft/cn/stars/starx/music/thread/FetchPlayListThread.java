package cn.stars.starx.music.thread;

import cn.stars.starx.StarX;
import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.api.base.Music;
import cn.stars.starx.music.api.base.PlayList;
import cn.stars.starx.music.ui.component.impl.MusicButton;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
@Getter
public class FetchPlayListThread extends Thread {
    private final PlayList playList;
    private final List<MusicButton> buttonsTemp;
    private List<MusicButton> target;
    private boolean noMore = false;

    public FetchPlayListThread(PlayList playList) {
        this.playList = playList;
        this.buttonsTemp = new ArrayList<>();
    }

    public FetchPlayListThread(PlayList playList, List<MusicButton> target) {
        this.buttonsTemp = new ArrayList<>();
        this.playList = playList;
        this.target = target;
    }

    @Override
    public void run() {
        List<Music> temp = new ArrayList<>(playList.getMusicList());
        MusicAPI.fetchMusicList(playList, temp.size());
        int index = temp.size() + 1;
        for (Music music : playList.getMusicList()) {
            if (temp.contains(music)) continue;
            buttonsTemp.add(new MusicButton(music, index, playList));
            index++;
        }
        noMore = temp.size() == playList.getMusicList().size();
        if (target != null) target.addAll(buttonsTemp);
        StarX.musicManager.screen.player.setMusicList(playList.getMusicList());
        MusicAPI.fixCoverImage(playList);
    }
}
