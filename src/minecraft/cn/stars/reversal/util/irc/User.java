/*
 * Reversal Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.reversal.util.irc;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.module.impl.player.IRC;
import cn.stars.reversal.util.misc.ModuleInstance;
import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Datapackage;
import com.blogspot.debukkitsblog.net.Executable;

import java.net.Socket;

public class User extends Client {
    public User(String host, int port, String username) {
        super(host, port, username);

        registerMethod("Message", new Executable() {
            @Override
            public void run(Datapackage msg, Socket socket) {
                if (ModuleInstance.getModule(IRC.class).isEnabled()) {
                    Reversal.showMsg("[IRC] " + msg.get(1));
                }
            }
        });

        start();
    }
}