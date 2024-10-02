package net.minecraft.client.gui;

import cn.stars.starx.GameInstance;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.awt.*;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        int i = y + slotHeight / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2 + 5;
        RenderUtil.rect(this.mc.currentScreen.width / 2f - 225, i - 10, 450, 0.5, new Color(220, 220, 220, 240));
        GameInstance.regular20.drawCenteredString(I18n.format("正在搜索局域网服务器...", new Object[0]), this.mc.currentScreen.width / 2, i,  new Color(220, 220, 220, 240).getRGB());

        RenderUtils.drawLoadingCircle2(this.mc.currentScreen.width / 2, i + 15, 4, new Color(220, 220, 220, 220));
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }
}
