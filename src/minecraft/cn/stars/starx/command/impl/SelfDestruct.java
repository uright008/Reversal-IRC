package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.api.CommandInfo;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

@CommandInfo(name = "SelfDestruct", description = "Make the client look like vanilla", syntax = ".selfdestruct", aliases = "selfdestruct")
public final class SelfDestruct extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Display.setTitle("Minecraft 1.8.8");
        Minecraft.getMinecraft().setWindowIcon();
        StarX.isDestructed = true;
    }
}
