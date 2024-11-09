/*
 * Reversal Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.reversal.module.impl.player;

import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.event.impl.UpdateEvent;
import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.util.irc.User;

@ModuleInfo(name = "IRC", chineseName = "聊天频道", description = "Private Reversal chat channel", chineseDescription = "仅Reversal用户可见的聊天频道", category = Category.PLAYER)
public class IRC extends Module {
    boolean isTrying = false;

    public void onUpdate(UpdateEvent event) {
        if (RainyAPI.ircUser == null && !isTrying) {
            new Thread(() -> {
                RainyAPI.ircUser = new User("irc-chat.6667890.xyz", 27810, mc.session.getUsername());
            }).start();
            isTrying = true;
        }
    }

}
