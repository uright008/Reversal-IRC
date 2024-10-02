package cn.stars.starx.music.thread;

import cn.stars.starx.StarX;
import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.api.base.LyricLine;
import cn.stars.starx.music.api.base.LyricPair;
import cn.stars.starx.music.api.base.Music;
import cn.stars.starx.music.api.base.PlayList;

import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class ChangeMusicThread extends Thread {
    private final Music music;
    private final PlayList playList;

    public ChangeMusicThread(Music music, PlayList playList) {
        this.music = music;
        this.playList = playList;
        setName("Music-ChangeMusic");
    }

    public ChangeMusicThread(Music music) {
        this.music = music;
        this.playList = null;
        setName("Music-ChangeMusic");
    }

    @Override
    public void run() {
        if (music.getSongURL() == null) {
            music.setSongURL(MusicAPI.getMusicURL(music.getId(), false));
        }
        List<LyricLine> lyrics = music.getLyrics();
        LyricPair pair = null;
        if (lyrics.isEmpty()) {
            // 请求逐字歌词
            pair = MusicAPI.getLyrics(music.getId());
            music.setLyrics(pair.getLyrics());
            // 纠正低级格式的duration
        //    music.correctLyricDuration();
            if (music.isHasTranslate()) {
                music.setTranslatedLyrics(pair.getTranslatedLyrics());
                if (!music.getTranslatedLyrics().isEmpty()) {
                    music.generateTranslateMap();
                } else music.setHasTranslate(false);
            }
        }

        List<LyricLine> translatedLyrics = music.getTranslatedLyrics();
        if (translatedLyrics.isEmpty() && music.isHasTranslate()) {
            // 请求翻译
            music.setTranslatedLyrics(pair == null? MusicAPI.getLyrics(music.getId()).getTranslatedLyrics() : pair.getTranslatedLyrics());
            if (!music.getTranslatedLyrics().isEmpty()) {
                music.generateTranslateMap();
            } else music.setHasTranslate(false);
        }

        StarX.musicManager.screen.player.setMusic(music);
        if (playList != null) {
            StarX.musicManager.screen.player.setMusicList(playList.getMusicList());
        }
    }
}
