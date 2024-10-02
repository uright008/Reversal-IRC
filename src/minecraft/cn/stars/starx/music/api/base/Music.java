/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.music.api.base;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Music {
    String name;
    String artist;
    String album_name;
    long id;
    int duration;
    File file;
    boolean free;
    public File coverImage;

    public DynamicTexture texture;
    public boolean hasTranslate = true;
    public String songURL;
    public List<LyricLine> lyrics = new ArrayList<>();
    public List<LyricLine> translatedLyrics = new ArrayList<>();
    public Map<LyricLine, LyricLine> translateMap = new HashMap<>();

    public Music(String name, String artist, String album_name, long id, int duration, File file, boolean fee) {
        this.name = name;
        this.artist = artist;
        this.album_name = album_name;
        this.id = id;
        this.duration = duration;
        this.file = file;
        this.free = fee;
    }

    public Music(String name, String artist, String album_name, long id, int duration, boolean fee) {
        this.name = name;
        this.artist = artist;
        this.album_name = album_name;
        this.id = id;
        this.duration = duration;
        this.file = null;
        this.free = fee;
    }

    public void generateTranslateMap() {
    }
}
