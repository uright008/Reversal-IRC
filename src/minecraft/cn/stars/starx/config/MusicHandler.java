package cn.stars.starx.config;

import cn.stars.starx.LonelyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.music.MusicUtil;
import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.api.user.User;
import cn.stars.starx.music.thread.GetPlayListsThread;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.misc.FileUtil;
import cn.stars.starx.util.render.ThemeUtil;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@UtilityClass
public final class MusicHandler {

    private final String s = File.separator;

    public void save() {
        FileUtil.saveFile("MusicAccount.txt", true, MusicUtil.gson.fromJson(MusicUtil.gson.toJson(MusicAPI.user), JsonObject.class) + "\r\n");
    }


    public void load() {
        final String config = FileUtil.loadFile("MusicAccount.txt");
        if (config == null || !LonelyAPI.hasJavaFX) {
            return;
        }
        MusicAPI.user = MusicUtil.gson.fromJson(config, User.class);
    }
}
