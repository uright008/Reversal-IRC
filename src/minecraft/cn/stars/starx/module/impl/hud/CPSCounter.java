package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.ClickEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.TickEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "CPSCounter", description = "Show your CPS on screen", category = Category.HUD)
public class CPSCounter extends Module {
    public CPSCounter() {
        setCanBeEdited(true);
        setWidth(60);
        setHeight(30);
        this.Lclicks = new ArrayList<>();
        this.Rclicks = new ArrayList<>();
    }
    private final List<Long> Lclicks;
    private final List<Long> Rclicks;
    private final BoolValue showBackground = new BoolValue("Show Background",this ,true);

    public int x;
    public int y;
    private int width;
    private int height;

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        GL11.glPushMatrix();
        if (this.showBackground.isEnabled()) {
            Gui.drawRect((int) getXPosition() - 3, (int) getYPosition() - 2, (int) (getXPosition() + width + 2), (int) (getYPosition() + mc.fontRendererObj.FONT_HEIGHT + 1), 0x6F000000);
            String string = this.Lclicks.size() + " | " + this.Rclicks.size() + " CPS";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff);
            this.width = mc.fontRendererObj.getStringWidth(string);
        } else {
            String string = "[" + this.Lclicks.size() + " | " + this.Rclicks.size() + " CPS]";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff, true);
            this.width = mc.fontRendererObj.getStringWidth(string);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void onTick(TickEvent event) {
        this.Lclicks.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        this.Rclicks.removeIf(t -> t < System.currentTimeMillis() - 1000L);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getType() == ClickEvent.ClickType.LEFT) {
            this.Lclicks.add(System.currentTimeMillis());
        }
        if (event.getType() == ClickEvent.ClickType.RIGHT) {
            this.Rclicks.add(System.currentTimeMillis());
        }
    }

    // No more using "*" because position wrong when restart
    public double getXPosition() {
        return getX();
    }
    public double getYPosition() {
        return getY();
    }

}
