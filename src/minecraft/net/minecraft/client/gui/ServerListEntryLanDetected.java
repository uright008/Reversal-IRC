package net.minecraft.client.gui;

import cn.stars.starx.GameInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

import java.awt.*;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer field_148292_c;
    protected final Minecraft mc;
    protected final LanServerDetector.LanServer field_148291_b;
    private long field_148290_d = 0L;

    protected ServerListEntryLanDetected(GuiMultiplayer p_i45046_1_, LanServerDetector.LanServer p_i45046_2_)
    {
        this.field_148292_c = p_i45046_1_;
        this.field_148291_b = p_i45046_2_;
        this.mc = Minecraft.getMinecraft();
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        GameInstance.regular20Bold.drawString(I18n.format("lanServer.title"), x + 32 + 3, y + 1, new Color(220, 220, 220, 240).getRGB());
        GameInstance.regular16.drawString(this.field_148291_b.getServerMotd(), x + 32 + 3, y + 12, new Color(220, 220, 220, 240).getRGB());

        if (this.mc.gameSettings.hideServerAddress)
        {
            GameInstance.regular16.drawString(I18n.format("selectServer.hiddenAddress"), x + 32 + 3, y + 12 + 11, new Color(220, 220, 220, 240).getRGB());
        }
        else
        {
            GameInstance.regular16.drawString(this.field_148291_b.getServerIpPort(), x + 32 + 3, y + 12 + 11, new Color(220, 220, 220, 240).getRGB());
        }
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        this.field_148292_c.selectServer(slotIndex);

        if (Minecraft.getSystemTime() - this.field_148290_d < 250L)
        {
            this.field_148292_c.connectToSelected();
        }

        this.field_148290_d = Minecraft.getSystemTime();
        return false;
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

    public LanServerDetector.LanServer getLanServer()
    {
        return this.field_148291_b;
    }
}
