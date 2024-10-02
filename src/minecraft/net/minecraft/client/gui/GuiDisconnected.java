package net.minecraft.client.gui;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import cn.stars.starx.GameInstance;
import cn.stars.starx.util.Transformer;
import cn.stars.starx.ui.curiosity.CuriosityTextButton;
import cn.stars.starx.ui.gui.GuiMainMenu;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RoundedUtil;
import cn.stars.starx.util.shader.RiseShaders;
import cn.stars.starx.util.shader.base.ShaderRenderType;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

import static cn.stars.starx.GameInstance.*;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private CuriosityTextButton reconnectButton, cancelButton;
    private CuriosityTextButton[] buttons;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = (int) (this.multilineMessage.size() * regular16.height());
        reconnectButton = new CuriosityTextButton(this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + regular16.height(), 200, 20, () -> this.mc.displayGuiScreen(new GuiMultiplayer(Transformer.transformMainMenu())), "返回主菜单", "", true, 1, 75, 5, 20);
        cancelButton = new CuriosityTextButton(this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + regular16.height() + 25, 200, 20, () -> this.mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, mc.getCurrentServerData())), "重连", "", true, 1, 90, 5, 20);
        buttons = new CuriosityTextButton[]{reconnectButton, cancelButton};
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (CuriosityTextButton menuButton : this.buttons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        try {
            float maxLength = Math.max(regular24Bold.width(this.reason), 300);
            this.drawDefaultBackground();
            // blur
            RiseShaders.GAUSSIAN_BLUR_SHADER.update();
            RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);

            // bloom
            RiseShaders.POST_BLOOM_SHADER.update();
            RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

            GameInstance.clearRunnables();

            if (this.multilineMessage != null) {
                for (String s : this.multilineMessage) {
                    maxLength = Math.max(maxLength, regular16.width(s));
                }
            }

            RoundedUtil.drawRound(this.width / 2 - maxLength / 2 - 30, this.height / 2 - this.field_175353_i / 2 - regular16.height() * 2 - 10, maxLength + 60, 100 + 10 * multilineMessage.size(), 4, new Color(30, 30, 30, 160));
            RenderUtil.rect(this.width / 2 - maxLength / 2 - 30, this.height / 2 - this.field_175353_i / 2 - regular16.height() * 2 + 10, maxLength + 60, 0.5, new Color(220, 220, 220, 240));

            for (CuriosityTextButton button : buttons) {
                button.draw(mouseX, mouseY, partialTicks);
            }

            regular24Bold.drawCenteredString(this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - regular16.height() * 2 - 5, new Color(220, 220, 220, 240).getRGB());
            int i = this.height / 2 - this.field_175353_i / 2;

            if (this.multilineMessage != null) {
                for (String s : this.multilineMessage) {
                    regular16.drawCenteredString(s, this.width / 2, i, new Color(220, 220, 220, 240).getRGB());
                    maxLength = Math.max(maxLength, regular16.width(s));
                    i += regular16.height();
                }
            }


            UI_BLOOM_RUNNABLES.forEach(Runnable::run);
            UI_BLOOM_RUNNABLES.clear();

            super.drawScreen(mouseX, mouseY, partialTicks);
        } catch (StackOverflowError error) {

        }
    }
}
