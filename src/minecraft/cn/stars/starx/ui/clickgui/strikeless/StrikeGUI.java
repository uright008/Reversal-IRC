package cn.stars.starx.ui.clickgui.strikeless;

import cn.stars.starx.StarX;
import cn.stars.starx.module.Category;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StrikeGUI extends GuiScreen {

    public static float scrollHorizontal;
    private final List<ClickFrame> frames = new ArrayList<>();
    private float lastScrollHorizontal;

    public void updateScroll() {
        if (GuiInventory.isCtrlKeyDown()) {
            final float partialTicks = mc == null || mc.timer == null ? 1.0F : mc.timer.renderPartialTicks;

            final float lastLastScrollHorizontal = lastScrollHorizontal;
            lastScrollHorizontal = scrollHorizontal;
            final float wheel = Mouse.getDWheel();
            scrollHorizontal += wheel / 10.0F;
            if (wheel == 0) scrollHorizontal -= (lastLastScrollHorizontal - scrollHorizontal) * 0.6 * partialTicks;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        if (frames.size() <= 0) {
            int index = -1;
            for (final Category category : Category.values()) {

                final ClickFrame frame = new ClickFrame(category, 5 + (++index * (ClickFrame.entryWidth - 10)), 20);

                frames.add(frame);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.rect(0.0,0.0, sr.getScaledWidth_double(), sr.getScaledHeight_double(), new Color(0,0,0,100));
        frames.forEach(frame -> frame.draw(this, mouseX, mouseY));
        frames.forEach(frame -> frame.drawDescriptions(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        frames.forEach(frame -> frame.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
    }

    @Override
    public void onGuiClosed() {
    }
}
