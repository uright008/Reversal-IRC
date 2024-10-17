package cn.stars.starx.config;

import cn.stars.starx.RainyAPI;
import cn.stars.starx.music.MusicUtil;
import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.api.user.User;
import cn.stars.starx.util.misc.FileUtil;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public final class MusicHandler {

    private final String s = File.separator;

    public void save() {
        FileUtil.saveFile("MusicAccount.txt", true, MusicUtil.gson.fromJson(MusicUtil.gson.toJson(MusicAPI.user), JsonObject.class) + "\r\n");
    }


    public void load() {
        final String config = FileUtil.loadFile("MusicAccount.txt");
        if (config == null || !RainyAPI.hasJavaFX) {
            return;
        }
        MusicAPI.user = MusicUtil.gson.fromJson(config, User.class);
    }
}
