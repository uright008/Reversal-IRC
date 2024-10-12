/*
 * StarX Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.starx.module.impl.player;

import cn.stars.starx.LonelyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.util.irc.User;

@ModuleInfo(name = "IRC", chineseName = "聊天频道", description = "Private StarX chat channel", chineseDescription = "仅StarX用户可见的聊天频道", category = Category.PLAYER)
public class IRC extends Module {
    boolean isTrying = false;

    public void onUpdate(UpdateEvent event) {
        if (LonelyAPI.ircUser == null && !isTrying) {
            new Thread(() -> {
                LonelyAPI.ircUser = new User("irc-chat.6667890.xyz", 27810, mc.session.getUsername());
            }).start();
            isTrying = true;
        }
    }

}
