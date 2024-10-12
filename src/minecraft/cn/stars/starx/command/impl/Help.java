package cn.stars.starx.command.impl;

import cn.stars.starx.StarX;
import cn.stars.starx.command.Command;
import cn.stars.starx.command.CommandManager;
import cn.stars.starx.command.api.CommandInfo;
import net.minecraft.util.EnumChatFormatting;

@CommandInfo(name = "Help", description = "Sends all of the commands that currently exists in chat", syntax = ".help", aliases = "help")
public final class Help extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        StarX.showMsg(EnumChatFormatting.WHITE + "All available commands:");

        for (final Command cmd : CommandManager.commandList) {
            final String description = cmd.getCommandInfo().description();
            final String alias = cmd.getCommandInfo().aliases()[0];

            if (!alias.contains("help")) {
                StarX.showMsg(alias + ": " + description);
            }
        }
    }
}
