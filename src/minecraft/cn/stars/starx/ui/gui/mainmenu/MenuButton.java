package cn.stars.starx.ui.gui.mainmenu;


import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.render.RenderUtil;

public class MenuButton extends MenuComponent implements MenuColors {

    private final Runnable runnable;
    public boolean isHovering;

    private final Animation animation = new Animation(Easing.EASE_OUT_QUINT, 500);
    private final Animation hoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250);

    public MenuButton(double x, double y, double width, double height, Runnable runnable) {
        super(x, y, width, height);
        this.runnable = runnable;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        isHovering = RenderUtil.isHovered(this.getX(), this.getY(), this.getWidth(), this.getHeight(), mouseX, mouseY);
        this.hoverAnimation.run(isHovering ? 120 : 45);
    }

    public void runAction() {
        this.runnable.run();
    }

    public Animation getAnimation() {
        return animation;
    }

    public Animation getHoverAnimation() {
        return hoverAnimation;
    }

    public boolean isHovering() {
        return isHovering;
    }
}
