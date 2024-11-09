package cn.stars.reversal.music.ui.component;


import cn.stars.reversal.GameInstance;
import cn.stars.reversal.util.render.RenderUtil;

/**
 * @author ChengFeng
 * @since 2024/8/12
 **/
public abstract class Button implements GameInstance {
    protected float posX, posY;
    public float width, height;
    protected String text;
    public boolean hovering;

    public abstract void draw();

    public void updateState(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;
        hovering = RenderUtil.isHovered(x, y, width, height, mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }
}
