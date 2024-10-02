package cn.stars.starx.music.thread;

import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.ui.MusicPlayerScreen;
import cn.stars.starx.music.ui.gui.MusicPlayerGUI;
import cn.stars.starx.music.ui.gui.impl.PlayListGUI;
import lombok.Getter;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class SearchMusicThread extends Thread {
    private final MusicPlayerScreen parent;
    @Getter
    private MusicPlayerGUI gui;

    public SearchMusicThread(MusicPlayerScreen parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        gui = new PlayListGUI(MusicAPI.search(parent.getSearchField().getText()), parent.getCurrentGUI());
    }
}
