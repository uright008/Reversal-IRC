package cn.stars.starx.ui.curiosity.impl;

import cn.stars.starx.GameInstance;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.font.modern.MFont;
import cn.stars.starx.ui.curiosity.CuriosityTextButton;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.ColorUtils;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RoundedUtil;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.gui.GuiProtocolSelector;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;
import java.io.IOException;

@NativeObfuscation
public class CuriosityViaMenu extends GuiScreen implements GuiYesNoCallback {
    private final GuiScreen parent;
    public SlotList list;

    MFont regular = FontManager.getRegular(20);
    MFont eaves = FontManager.getEaves(64);
    MFont psm = FontManager.getPSM(24);
    MFont psb = FontManager.getPSB(24);
    private final Animation fontAnimation = new Animation(Easing.EASE_OUT_SINE, 400);

    private CuriosityTextButton back;
    private CuriosityTextButton[] buttons;
    private String tipString = "";

    public CuriosityViaMenu(GuiScreen parent) {
        this.parent = parent;
    }

    @SneakyThrows
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(partialTicks, mouseX, mouseY);

        updatePostProcessing(true, partialTicks);

        back.draw(mouseX, mouseY, partialTicks);

        RoundedUtil.drawRound(width / 2f - 200, height / 2f - 240, 400, 400, 4, new Color(30, 30, 30, 200));
        RenderUtil.image(new ResourceLocation("starx/images/curiosity.png"), width / 2f - 180, height / 2f - 260, 100, 100);
        eaves.drawString("STARX | ViaVersion", width / 2f - 100, height / 2f - 220, new Color(220, 220, 220, 240).getRGB());
        regular.drawCenteredString("一个可以使你加入高于1.8.x版本服务器的强大工具",
                width / 2f, height / 2f - 180, new Color(220, 220, 220, 240).getRGB());
        regular.drawCenteredString("由EnZaXD/Flori2007驱动 / 由Stars编写GUI",
                width / 2f, height / 2f - 165, new Color(220, 220, 220, 240).getRGB());

        RenderUtil.rect(width / 2f - 200, height / 2f - 150, 400, 0.5, new Color(220, 220, 220, 240));

        GameInstance.NORMAL_BLUR_RUNNABLES.add(() -> RoundedUtil.drawRound(width / 2f - 200, height / 2f - 240, 400, 400, 4, Color.BLACK));

        regular.drawCenteredString(tipString, width / 2f, height / 2f + 110, new Color(220, 220, 220, 240).getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        list.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.scissor(width / 2f - 200, height / 2f - 148, 400, 308);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        updatePostProcessing(false, partialTicks);
    }

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        list = new SlotList(mc, width, height, centerY - 170, centerY + 150);

        this.back = new CuriosityTextButton(centerX - 60, centerY + 180, 120, 35, () -> mc.displayGuiScreen(parent),
                "返回主菜单", "g", true, 12, 38, 11);

        this.buttons = new CuriosityTextButton[] { this.back };
    }

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

    public void handleMouseInput() throws IOException {
        list.handleMouseInput();
        if (Mouse.getEventButton() == 0) {
            for (CuriosityTextButton menuButton : this.buttons) {
                if (RenderUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), this)) {
                    mc.getSoundHandler().playButtonPress();
                    menuButton.runAction();
                    break;
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        list.actionPerformed(button);
    }

    class SlotList extends GuiSlot {

        public SlotList(Minecraft mc, int width, int height, int top, int bottom) {
            super(mc, width, height, top + 30, bottom, 18);
        }

        @Override
        protected int getSize() {
            return ViaLoadingBase.PROTOCOLS.size();
        }

        @Override
        protected void elementClicked(int i, boolean b, int i1, int i2) {
            final ProtocolVersion protocolVersion = ViaLoadingBase.PROTOCOLS.get(i);
            ViaLoadingBase.getInstance().reload(protocolVersion);
        }

        @Override
        protected boolean shouldRenderOverlay() {
            return false;
        }

        @Override
        protected boolean isSelected(int i) {
            return false;
        }

        @Override
        protected void drawBackground() {
        }

        @Override
        protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5) {
            if (ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion()) == i) {
                psb.drawCenteredString(">  " + ViaLoadingBase.PROTOCOLS.get(i).getName() + "  <", width / 2f, i2 + 2, Color.WHITE.getRGB());
            } else {
                psm.drawCenteredString(ViaLoadingBase.PROTOCOLS.get(i).getName(), width / 2f, i2 + 2, Color.GRAY.getRGB());
            }
        }
    }
}
