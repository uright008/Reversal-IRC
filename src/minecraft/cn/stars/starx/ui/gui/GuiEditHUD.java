package cn.stars.starx.ui.gui;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.shader.round.RoundedUtils;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class GuiEditHUD extends GuiScreen {
    TTFFontRenderer gs = CustomFont.FONT_MANAGER.getFont("GoogleSans 18");
    @Override
    public void initGui() {
        for(Module m : StarX.moduleManager.moduleList) {
            m.setDragging(false);
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(Module m : StarX.moduleManager.moduleList) {
            if(m.isEnabled() && m.getModuleInfo().category().equals(Category.HUD) && m.isCanBeEdited()) {

                boolean isInside = RenderUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth(), m.getHeight()) &&
                        Arrays.stream(StarX.moduleManager.moduleList).filter(m2 -> m2.isEnabled() && m2.getModuleInfo().category().equals(Category.HUD) && mouseX >= m2.getX() && mouseX <= m2.getX() + m2.getWidth() && mouseY >= m2.getY() && mouseY <= m2.getY() + m2.getHeight()).findFirst().get().equals(m);
                m.editOpacityAnimation.setAnimation(isInside ?  255 : 0, 12);

                RoundedUtils.drawRoundOutline(m.getX() - 4, m.getY() - 4, (m.getWidth()) + 8, (m.getHeight()) + 8, 6, 1, new Color(255, 255, 255, 0), new Color(255, 255, 255, (int) m.editOpacityAnimation.getValue()));

                if (isInside) {
                    gs.drawString(m.getModuleInfo().name(), m.getX() + m.getWidth() - gs.getWidth(m.getModuleInfo().name()), m.getY() + m.getHeight() - 7, Color.WHITE.getRGB());
                }
                if(m.isDragging()) {
                    m.setX(mouseX + m.getDraggingX());
                    m.setY(mouseY + m.getDraggingY());
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {

        for(Module m : StarX.moduleManager.moduleList) {
            if(m.isEnabled() && m.getModuleInfo().category().equals(Category.HUD) && m.isCanBeEdited()) {

                boolean isInside = RenderUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth(), m.getHeight()) &&
                        Arrays.stream(StarX.moduleManager.moduleList).filter(m2 -> m2.isEnabled() && m2.getModuleInfo().category().equals(Category.HUD) && mouseX >= m2.getX() && mouseX <= m2.getX() + m2.getWidth() && mouseY >= m2.getY() && mouseY <= m2.getY() + m2.getHeight()).findFirst().get().equals(m);

                if(isInside) {
                    m.setDragging(true);
                    m.setDraggingX(m.getX() - mouseX);
                    m.setDraggingY(m.getY() - mouseY);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(Module m : StarX.moduleManager.moduleList){
            m.setDragging(false);
        }
    }

    @Override
    public void onGuiClosed() {
        for(Module m : StarX.moduleManager.moduleList){
            m.setDragging(false);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
