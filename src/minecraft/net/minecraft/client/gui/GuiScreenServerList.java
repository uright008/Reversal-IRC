package net.minecraft.client.gui;

import java.awt.*;
import java.io.IOException;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.ui.curiosity.CuriosityTextButton;
import cn.stars.reversal.util.render.RenderUtil;
import cn.stars.reversal.util.render.RoundedUtil;
import cn.stars.reversal.util.shader.RiseShaders;
import cn.stars.reversal.util.shader.base.ShaderRenderType;
import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.input.Keyboard;

import static cn.stars.reversal.GameInstance.*;
import static cn.stars.reversal.GameInstance.UI_BLOOM_RUNNABLES;

public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen field_146303_a;
    private final ServerData field_146301_f;
    private GuiTextField field_146302_g;
    private CuriosityTextButton selectButton, cancelButton;
    private CuriosityTextButton[] buttons;

    public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_)
    {
        this.field_146303_a = p_i1031_1_;
        this.field_146301_f = p_i1031_2_;
    }

    public void updateScreen()
    {
        this.field_146302_g.updateCursorCounter();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        selectButton = new CuriosityTextButton(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20, () -> {
            if (!this.field_146302_g.getText().isEmpty()) {
                this.field_146301_f.serverIP = this.field_146302_g.getText();
                this.field_146303_a.confirmClicked(true, 0);
            }
        }, "连接服务器", "", true, 1, 75, 5, 20);
        cancelButton = new CuriosityTextButton(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, () -> this.field_146303_a.confirmClicked(false, 0), "取消", "", true, 1, 90, 5, 20);
        this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
        this.field_146302_g.setMaxStringLength(128);
        this.field_146302_g.setFocused(true);
        this.field_146302_g.setText(this.mc.gameSettings.lastServer);
        buttons = new CuriosityTextButton[] {selectButton, cancelButton};
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.field_146302_g.getText();
        this.mc.gameSettings.saveOptions();
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.field_146302_g.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28 || keyCode == 156)
        {
            selectButton.runAction();
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (CuriosityTextButton menuButton : this.buttons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
        this.field_146302_g.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        // blur
        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);

        // bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

        GameInstance.clearRunnables();

        RoundedUtil.drawRound(width / 2f - 225, 10, 450, height - 15, 4, new Color(30, 30, 30, 160));
        GameInstance.NORMAL_BLUR_RUNNABLES.add(() -> RoundedUtil.drawRound(width / 2f - 225, 10, 450, height - 15, 4, Color.BLACK));
        RenderUtil.rect(width / 2f - 225, 30, 450, 0.5, new Color(220, 220, 220, 240));

        for (CuriosityTextButton button : buttons) {
            button.draw(mouseX, mouseY, partialTicks);
        }

        GameInstance.regular24Bold.drawCenteredString("直接连接", width / 2f, 16, new Color(220, 220, 220, 240).getRGB());
        GameInstance.regular20.drawString("输入服务器IP", this.width / 2 - 100, 105, new Color(220, 220, 220, 240).getRGB());

        this.field_146302_g.drawTextBox(mouseX, mouseY);

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
