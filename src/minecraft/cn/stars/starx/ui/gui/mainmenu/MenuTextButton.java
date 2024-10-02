package cn.stars.starx.ui.gui.mainmenu;

import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.font.modern.MFont;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.RenderUtil;

import java.awt.*;


public class MenuTextButton extends MenuButton {

    private static final MFont FONT_RENDERER = FontManager.getRegular(20);
    private static final MFont ICON_RENDERER = FontManager.getMi(36);

    public boolean left = false;
    public String name;
    public String icon;
    public int textY;
    public int textX;

    public MenuTextButton(double x, double y, double width, double height, Runnable runnable, String name, String icon) {
        super(x, y, width, height, runnable);
        this.name = name;
        this.icon = icon;
    }

    public MenuTextButton(double x, double y, double width, double height, Runnable runnable, String name, String icon, boolean left, int textX, int textY) {
        super(x, y, width, height, runnable);
        this.name = name;
        this.icon = icon;
        this.left = left;
        this.textX = textX;
        this.textY = textY;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // Runs the animation update - keep this
        super.draw(mouseX, mouseY, partialTicks);
        // Colors for rendering
        final double value = getY();
        final double progress = value / this.getY();
        final Color bloomColor = ColorUtil.withAlpha(Color.BLACK, (int) (progress * 150));
        final Color fontColor = ColorUtil.withAlpha(TEXT_PRIMARY, (int) this.getHoverAnimation().getValue() + 100);

        // Renders the background of the button
        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.roundedRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 8, Color.WHITE));
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> RenderUtil.roundedRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 7, bloomColor));

        // Renders the button text
        UI_BLOOM_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 6,
                    ColorUtil.withAlpha(BUTTON, (int) this.getHoverAnimation().getValue() - 15));
//            RenderUtil.roundedOutlineRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 5, 0.5f, ColorUtil.withAlpha(Color.WHITE, (int) ((int) this.getHoverAnimation().getValue() / 1.7f)));
            RenderUtil.roundedOutlineGradientRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 5, 1,
                    ColorUtil.withAlpha(BORDER_ONE, (int) (this.getHoverAnimation().getValue() * 2 + 10)),
                    ColorUtil.withAlpha(BORDER_TWO, (int) (this.getHoverAnimation().getValue() * 2 + 10)));
            if (this.left) {
                ICON_RENDERER.drawString(this.icon, (float) (this.getX() + textX), (float) (value + textY - 1), fontColor.getRGB());
                FONT_RENDERER.drawString(this.name, (float) (this.getX() + textX + ICON_RENDERER.getWidth(this.icon) + 6), (float) (value + textY + 2), fontColor.getRGB());
            } else {
                ICON_RENDERER.drawCenteredString(this.icon, (float) (this.getX() + this.getWidth() / 2.0F), (float) (value + this.getHeight() / 2.0F - 15), fontColor.getRGB());
                FONT_RENDERER.drawCenteredString(this.name, (float) (this.getX() + this.getWidth() / 2.0F), (float) (value + this.getHeight() / 2.0F + 5), fontColor.getRGB());
            }
        });
    }
}
