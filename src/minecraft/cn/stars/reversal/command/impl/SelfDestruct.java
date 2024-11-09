package cn.stars.reversal.command.impl;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

@CommandInfo(name = "SelfDestruct", description = "Make the client look like vanilla", syntax = ".selfdestruct", aliases = "selfdestruct")
public final class SelfDestruct extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Display.setTitle("Minecraft 1.8.8");
        Minecraft.getMinecraft().setWindowIcon();
        Reversal.isDestructed = true;
    }
}
